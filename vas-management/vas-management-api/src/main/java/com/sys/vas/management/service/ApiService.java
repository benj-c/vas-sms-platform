package com.sys.vas.management.service;

import com.sys.vas.management.dto.ApiHistoryVersionDto;
import com.sys.vas.management.dto.ApiResponseDto;
import com.sys.vas.management.dto.ApiXmlResponseDto;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.entity.ApiHistoryEntity;
import com.sys.vas.management.dto.request.AddApiCommitRequest;
import com.sys.vas.management.dto.request.CreateApiRequestDto;
import com.sys.vas.management.dto.request.UpdateApiDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ApiHistoryRepository;
import com.sys.vas.management.repository.ApiRepository;
import com.sys.vas.management.util.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ApiService {

    private ApiRepository apiRepository;
    private ApiHistoryRepository apiHistoryRepository;
    private SysActionLogService sysActionLogService;

    ApiService(
            ApiRepository apiRepository,
            ApiHistoryRepository apiHistoryRepository,
            SysActionLogService sysActionLogService
    ) {
        this.apiRepository = apiRepository;
        this.apiHistoryRepository = apiHistoryRepository;
        this.sysActionLogService = sysActionLogService;
    }

    /**
     *
     * @return
     */
    public List<ApiResponseDto> getAllApis() {
        return apiRepository.getActiveApis();
    }

    /**
     *
     * @param requestDto
     * @return
     */
    public long create(CreateApiRequestDto requestDto) {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setName(requestDto.getName());
        apiEntity.setDescription(requestDto.getDescription());
//        apiEntity.setVersion(requestDto.getVersion());

        long id = apiRepository.save(apiEntity).getId();
        return id;
    }

    /**
     *
     * @param id
     * @return
     */
    public ApiXmlResponseDto getApiByIdAndCommit(long id, String commitId) {
        return apiRepository.findApiByIdAndVersion(id, commitId)
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + id));
    }

    /**
     *
     * @param updateApiDto
     * @return
     */
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
        sysActionLogService.logEvent("API " + fApi.getName() + " was updated by " + ApiUtil.getAuthUserName());
        return id;
    }

    /**
     *
     * @param addApiCommitRequest
     * @return
     */
    public String commit(AddApiCommitRequest addApiCommitRequest) {
        ApiEntity fApi = apiRepository.findById(addApiCommitRequest.getApiId())
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + addApiCommitRequest.getApiId()));

        ApiHistoryEntity commit = new ApiHistoryEntity();
        commit.setCommitId(UUID.randomUUID().toString());
        commit.setVersion(addApiCommitRequest.getVersion());
        commit.setXml(addApiCommitRequest.getXml());
        commit.setIsActive(false);
        commit.setCommitMessage(addApiCommitRequest.getCommitMessage());
        commit.setCommitedDateTime(LocalDateTime.now());
        commit.setApi(fApi);

        return apiHistoryRepository.save(commit).getCommitId();
    }

    public void setActiveApi(long apiId, String commitId) {

    }

    /**
     *
     * @param id
     * @return
     */
    public List<ApiHistoryVersionDto> getApiVersionsById(Long id) {
        return apiHistoryRepository.findAllByApi(id);
    }

    /**
     *
     * @param apiId
     * @param commitId
     */
    @Transactional
    public void deploy(long apiId, String commitId) {
        apiHistoryRepository.updateApiSetActive(apiId, commitId);
    }
}
