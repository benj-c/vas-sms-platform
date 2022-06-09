package com.sys.vas.management.service;

import com.sys.vas.management.dto.ActionByServiceDto;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.ServiceDto;
import com.sys.vas.management.dto.entity.ActionEntity;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.entity.ServiceEntity;
import com.sys.vas.management.dto.request.ActionUpdateRequestDto;
import com.sys.vas.management.dto.request.CreateActionRequestDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ActionRepository;
import com.sys.vas.management.repository.ApiRepository;
import com.sys.vas.management.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionService {

    private ActionRepository actionRepository;
    private ServiceRepository serviceRepository;
    private ApiRepository apiRepository;

    public ActionService(
            ActionRepository actionRepository,
            ServiceRepository serviceRepository,
            ApiRepository apiRepository
    ) {
        this.actionRepository = actionRepository;
        this.serviceRepository = serviceRepository;
        this.apiRepository = apiRepository;
    }

    /**
     *
     * @param requestDto
     */
    public void update(ActionUpdateRequestDto requestDto) {
        ActionEntity fEntity = actionRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ResponseCodes.ACTION_NOT_FOUND, "action not found for id:" + requestDto.getId()));
        if (requestDto.getDescription() != null) {
            fEntity.setDescription(requestDto.getDescription());
        }
        actionRepository.save(fEntity);
    }

    public List<ActionByServiceDto> getActionsByServiceId(Long sid) {
        return actionRepository.findByServiceId(sid);
//        if (acts.isEmpty()) {
//            throw new ApiException(ResponseCodes.ACTION_NOT_FOUND, "actions not found for service:" + sid);
//        }
//        return acts;
    }

    public void create(CreateActionRequestDto requestDto) {
        ServiceEntity serviceEntity = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new ApiException(ResponseCodes.SERVICE_NOT_FOUND, "service not found for id:" + requestDto.getServiceId()));

        ApiEntity apiEntity = apiRepository.findById(requestDto.getApiId())
                .orElseThrow(() -> new ApiException(ResponseCodes.API_NOT_FOUND, "api not found for id:" + requestDto.getApiId()));

        ActionEntity actionEntity = new ActionEntity();
        actionEntity.setDescription(requestDto.getDescription());
        actionEntity.setService(serviceEntity);
        actionEntity.setApi(apiEntity);

        actionRepository.save(actionEntity);
    }
}
