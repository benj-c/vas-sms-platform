package com.sys.vas.management.service;

import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.request.ActionUpdateRequestDto;
import com.sys.vas.management.dto.request.CreateApiRequestDto;
import com.sys.vas.management.dto.request.UpdateApiDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ApiRepository;
import com.sys.vas.management.util.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApiService {

    private ApiRepository apiRepository;
    private SysActionLogService sysActionLogService;

    ApiService(ApiRepository apiRepository, SysActionLogService sysActionLogService) {
        this.apiRepository = apiRepository;
        this.sysActionLogService = sysActionLogService;
    }

    public List<ApiEntity> getAllApis() {
        return apiRepository.findAll();
    }

    public long create(CreateApiRequestDto requestDto) {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setName(requestDto.getName());
        apiEntity.setDescription(requestDto.getDescription());
        apiEntity.setVersion(requestDto.getVersion());

        long id = apiRepository.save(apiEntity).getId();
        return id;
    }

    public ApiEntity getApiById(long id) {
        return apiRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + id));
    }

    public long update(UpdateApiDto updateApiDto) {
        ApiEntity fApi = apiRepository.findById(updateApiDto.getId())
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + updateApiDto.getId()));

        if (!fApi.getName().equalsIgnoreCase(updateApiDto.getName())) {
            fApi.setName(updateApiDto.getName());
        }
        if (!fApi.getDescription().equalsIgnoreCase(updateApiDto.getDescription())) {
            fApi.setDescription(updateApiDto.getDescription());
        }
        if (!fApi.getVersion().equalsIgnoreCase(updateApiDto.getVersion())) {
            fApi.setVersion(updateApiDto.getVersion());
        }
        if (!fApi.getXml().equalsIgnoreCase(updateApiDto.getXml())) {
            fApi.setXml(updateApiDto.getXml());
        }

        long id = apiRepository.save(fApi).getId();
        sysActionLogService.logEvent("API " + fApi.getName() + " was updated by " + ApiUtil.getAuthUserName());
        return id;
    }

}
