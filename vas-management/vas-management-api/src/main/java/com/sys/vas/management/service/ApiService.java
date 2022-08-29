package com.sys.vas.management.service;

import com.sys.vas.management.dto.*;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.entity.ApiHistoryEntity;
import com.sys.vas.management.dto.request.AddApiCommitRequest;
import com.sys.vas.management.dto.request.CreateApiRequestDto;
import com.sys.vas.management.dto.request.UpdateApiDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ApiHistoryRepository;
import com.sys.vas.management.repository.ApiRepository;
import com.sys.vas.management.util.ApiUtil;
import com.sys.vas.management.util.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class ApiService extends SysActionLogService {

    private ApiRepository apiRepository;
    private ApiHistoryRepository apiHistoryRepository;

    public ApiService(
            ApiRepository apiRepository,
            ApiHistoryRepository apiHistoryRepository
    ) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
    }

    /**
     * @return
     */
    public List<ApiResponseDto> getAllApis() {
        return apiRepository.getActiveApis();
    }

    /**
     * @param requestDto
     * @return
     */
    @Transactional
    public ApiCreateResponseDto create(CreateApiRequestDto requestDto) {
        ApiHistoryEntity apiHistory = new ApiHistoryEntity();
        apiHistory.setCommitMessage("Initial commit");
        apiHistory.setXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        apiHistory.setIsActive(true);
        apiHistory.setCommitedDateTime(LocalDateTime.now());
        apiHistory.setCommitId(UUID.randomUUID().toString());
        apiHistory.setVersion(requestDto.getVersion());
        apiHistory.setAvgResTime(0l);
        apiHistory.setErrorCount(0l);
        apiHistory.setTotalRequestsCount(0l);

        Set<ApiHistoryEntity> hiss = new HashSet<>();
        hiss.add(apiHistory);

        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setName(requestDto.getName());
        apiEntity.setDescription(requestDto.getDescription());

        apiEntity.setApis(hiss);
        apiHistory.setApi(apiEntity);

        ApiHistoryEntity hs = apiHistoryRepository.save(apiHistory);
        logEvent(UserAction.builder()
                .type("created")
                .target(requestDto.getName())
                .comment(requestDto.getDescription())
                .build()
        );

        return ApiCreateResponseDto.builder()
                .id(hs.getApi().getId())
                .commitId(hs.getCommitId())
                .build();
    }

    /**
     * @param id
     * @return
     */
    public ApiXmlResponseDto getApiByIdAndCommit(long id, String commitId) {
        return apiRepository.findApiByIdAndVersion(id, commitId)
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + id));
    }

    /**
     * @param updateApiDto
     * @return
     */
    @Transactional
    public long update(UpdateApiDto updateApiDto) {
        ApiEntity fApi = apiRepository.findById(updateApiDto.getId())
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + updateApiDto.getId()));

        if (!fApi.getName().equalsIgnoreCase(updateApiDto.getName())) {
            fApi.setName(updateApiDto.getName());
        }
        if (!fApi.getDescription().equalsIgnoreCase(updateApiDto.getDescription())) {
            fApi.setDescription(updateApiDto.getDescription());
        }
        long id = apiRepository.save(fApi).getId();
        logEvent(UserAction.builder()
                .type("updated")
                .target(fApi.getName())
                .build()
        );
        return id;
    }

    /**
     * @param addApiCommitRequest
     * @return
     */
    @Transactional
    public String commit(AddApiCommitRequest addApiCommitRequest) throws IOException, NoSuchAlgorithmException {
        ApiEntity fApi = apiRepository.findById(addApiCommitRequest.getApiId())
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + addApiCommitRequest.getApiId()));

        String hash = ApiUtil.getMd5Hash(addApiCommitRequest.getXml());
        ApiHistoryEntity commit = new ApiHistoryEntity();
        commit.setCommitId(UUID.randomUUID().toString());
        commit.setVersion(addApiCommitRequest.getVersion());
        commit.setXml(addApiCommitRequest.getXml().replaceAll("&amp;|&", "&amp;"));
        commit.setIsActive(false);
        commit.setCommitMessage(addApiCommitRequest.getCommitMessage());
        commit.setCommitedDateTime(LocalDateTime.now());
        commit.setApi(fApi);

        String cmid = apiHistoryRepository.save(commit).getCommitId();
        logEvent(UserAction.builder()
                .type("updated")
                .target(fApi.getName())
                .comment("New API commit " + commit.getVersion())
                .build()
        );
        return cmid;
    }

    /**
     * @param id
     * @return
     */
    public List<ApiHistoryVersionDto> getApiVersionsById(Long id) {
        return apiHistoryRepository.findAllByApi(id);
    }

    /**
     * @param apiId
     * @param commitId
     */
    @Transactional
    public void deploy(long apiId, String commitId) throws IOException {
        List<ApiHistoryEntity> list = apiHistoryRepository.findAllByApiId(apiId);
        if (list.isEmpty()) {
            throw new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + apiId);
        }
        apiHistoryRepository.updateApiSetActive(apiId, commitId);
        WebClient.delete("http://localhost:8401/clearmem").create().exchange();
        logEvent(UserAction.builder()
                .type("deployed")
                .target(list.get(0).getApi().getName())
                .comment("Production deployment")
                .build()
        );
        logEvent(UserAction.builder()
                .type("cache clear")
                .target("commit: " + commitId)
                .comment("refreshed API cache on new deployment")
                .build()
        );
    }

}
