package com.sys.vas.management.service;

import com.sys.vas.management.dto.CxResponseDto;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.UserAction;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.entity.CxResponseEntity;
import com.sys.vas.management.dto.request.CreateCxResponseRequestDto;
import com.sys.vas.management.dto.request.UpdateCxResponseRequestDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ApiRepository;
import com.sys.vas.management.repository.CxResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CxResponseService extends SysActionLogService {

    private CxResponseRepository cxResponseRepository;
    private ApiRepository apiRepository;

    CxResponseService(
            CxResponseRepository cxResponseRepository,
            ApiRepository apiRepository
    ) {
        this.cxResponseRepository = cxResponseRepository;
        this.apiRepository = apiRepository;
    }

    /**
     *
     * @param apiId
     * @return
     */
    public List<CxResponseDto> getResponsesByApiId(long apiId) {
        return cxResponseRepository.findByApiId(apiId);
    }

    /**
     *
     * @param requestDto
     */
    @Transactional
    public void update(UpdateCxResponseRequestDto requestDto) {
        CxResponseEntity fEntity = cxResponseRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ResponseCodes.CX_RESPONSE_NOT_FOUND, "cx response not found"));
        if (requestDto.getResDesc() != null) {
            fEntity.setResDesc(requestDto.getResDesc());
        }
        if (requestDto.getResCode() != fEntity.getResCode()) {
            fEntity.setResCode(requestDto.getResCode());
        }
        if (requestDto.getSms() != null) {
            fEntity.setSms(requestDto.getSms());
        }
        cxResponseRepository.save(fEntity);
        logEvent(
                UserAction.builder()
                        .type("updated")
                        .target("an SMS")
                        .build()
        );
    }

    /**
     *
     * @param requestDto
     */
    @Transactional
    public void create(CreateCxResponseRequestDto requestDto) {
        ApiEntity apiEntity = apiRepository.findById(requestDto.getApiId())
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "api not found"));

        CxResponseEntity cxResponse = new CxResponseEntity();
        cxResponse.setResCode(requestDto.getResCode());
        cxResponse.setResDesc(requestDto.getResDesc());
        cxResponse.setSms(requestDto.getSms());
        cxResponse.setSendToOriginatedNo(1);
        cxResponse.setApi(apiEntity);

        cxResponseRepository.save(cxResponse);
        logEvent(
                UserAction.builder()
                        .type("added")
                        .target("new SMS")
                        .build()
        );
    }
}
