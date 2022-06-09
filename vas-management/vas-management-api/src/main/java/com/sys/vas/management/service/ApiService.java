package com.sys.vas.management.service;

import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.request.ActionUpdateRequestDto;
import com.sys.vas.management.dto.request.CreateApiRequestDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApiService {

    private ApiRepository apiRepository;

    ApiService(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    public List<ApiEntity> getAllApis() {
        return apiRepository.findAll();
    }

    public long create(CreateApiRequestDto requestDto) {
        ApiEntity apiEntity = new ApiEntity();
        apiEntity.setName(requestDto.getName());
        apiEntity.setDescription(requestDto.getDescription());
        apiEntity.setVersion(requestDto.getVersion());

        return apiRepository.save(apiEntity).getId();
    }

    public ApiEntity getApiById(long id) {
        return apiRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "API not found for ID:" + id));
    }

}
