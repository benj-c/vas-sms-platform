package com.sys.vas.management.service;

import com.sys.vas.management.dto.*;
import com.sys.vas.management.dto.entity.ActionEntity;
import com.sys.vas.management.dto.entity.ApiEntity;
import com.sys.vas.management.dto.entity.ServiceEntity;
import com.sys.vas.management.dto.request.CreateServiceRequestDto;
import com.sys.vas.management.dto.request.ServiceUpdateRequestDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ServiceRepository;
import com.sys.vas.management.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VasService {

    private ServiceRepository serviceRepository;
    private SysActionLogService sysActionLogService;

    public VasService(ServiceRepository serviceRepository, SysActionLogService sysActionLogService) {
        this.serviceRepository = serviceRepository;
        this.sysActionLogService = sysActionLogService;
    }

    /**
     * @return
     */
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(s -> ServiceDto.builder()
                        .name(s.getName())
                        .description(s.getDescription())
                        .id(s.getId())
                        .active(!s.getDisable())
                        .build()
                )
                .collect(Collectors.toList());
    }

    /**
     * @param id
     * @return
     */
    public ServiceDto getServiceDetailById(long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResponseCodes.SERVICE_NOT_FOUND, "could not find service"));

        return ServiceDto.builder()
                .id(serviceEntity.getId())
                .active(!serviceEntity.getDisable())
                .description(serviceEntity.getDescription())
                .name(serviceEntity.getName())
                .disabledSms(serviceEntity.getDisableSms())
                .createdDate(serviceEntity.getCreatedDate())
                .build();
    }

    /**
     * @param requestDto
     */
    @Transactional
    public void update(ServiceUpdateRequestDto requestDto) {
        ServiceEntity fEntity = serviceRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ResponseCodes.SERVICE_NOT_FOUND, "service not found for id:" + requestDto.getId()));
        if (requestDto.getName() != null) {
            fEntity.setName(requestDto.getName());
        }
        if (requestDto.getDescription() != null) {
            fEntity.setDescription(requestDto.getDescription());
        }
        if (requestDto.getDisableSms() != null) {
            fEntity.setDisableSms(requestDto.getDisableSms());
        }
        if (requestDto.getActive() != fEntity.getDisable()) {
            fEntity.setDisable(requestDto.getActive());
        }
        serviceRepository.save(fEntity);
        sysActionLogService.logEvent(
                UserAction.builder()
                        .type("updated")
                        .target(requestDto.getName())
                        .comment(requestDto.getDescription())
                        .build()
        );
    }

    /**
     *
     * @param requestDto
     * @return
     */
    @Transactional
    public long create(CreateServiceRequestDto requestDto) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setDisable(false);
        serviceEntity.setName(requestDto.getName());
        serviceEntity.setDescription(requestDto.getDescription());
        serviceEntity.setCreatedDate(LocalDate.now());
        serviceEntity.setDisableSms(requestDto.getDisabledSms());

        long id = serviceRepository.save(serviceEntity).getId();
        sysActionLogService.logEvent(
                UserAction.builder()
                        .type("created")
                        .target(requestDto.getName())
                        .comment(requestDto.getDescription())
                        .build()
        );
        return id;
    }

    public ExportXmlDto createXml(long sId) {
        ServiceEntity serviceEntity = serviceRepository.findById(sId)
                .orElseThrow(() -> new ApiException(ResponseCodes.SERVICE_NOT_FOUND, "could not find service"));

        ExportXmlDto dto = null;
        try {
            dto = XmlUtil.generate(serviceEntity);
        } catch (IOException e) {
            log.error("Exception", e);
            throw new ApiException(ResponseCodes.INTERNAL_SERVER_ERROR, "xml generation error");
        } catch (ParserConfigurationException e) {
            log.error("Exception", e);
            throw new ApiException(ResponseCodes.INTERNAL_SERVER_ERROR, "xml generation error");
        } catch (TransformerException e) {
            log.error("Exception", e);
            throw new ApiException(ResponseCodes.INTERNAL_SERVER_ERROR, "xml generation error");
        }
        return dto;

//        String xml = prepareExportData(serviceEntity);
//        ByteArrayResource resource = new ByteArrayResource(xml.getBytes(StandardCharsets.UTF_8));
//        return new ExportXmlDto(resource, serviceEntity.getName());
    }

    public String prepareExportData(ServiceEntity serviceEntity) throws JAXBException {
        ServiceDetailDto serviceDetailDto = new ServiceDetailDto();
        serviceDetailDto.setDisabledSms(serviceEntity.getDisableSms());
        serviceDetailDto.setName(serviceEntity.getName());
        serviceDetailDto.setCreatedDate(serviceEntity.getCreatedDate());
        serviceDetailDto.setDescription(serviceEntity.getDescription());
        serviceDetailDto.setActive(serviceEntity.getDisable());

        List<ServiceDetailDto.Action> actionList = new ArrayList<>();
        for (ActionEntity actionEntity : serviceEntity.getActions()) {
            ApiEntity api = actionEntity.getApi();
            ServiceDetailDto.Action action = ServiceDetailDto.Action.builder()
                    .api(ServiceDetailDto.Action.Api.builder()
                            .id(api.getId())
                            .xml(api.getXml())
                            .description(api.getDescription())
                            .name(api.getName())
                            .build()
                    )
                    .id(actionEntity.getId())
                    .description(actionEntity.getDescription())
                    .build();
            List<ServiceDetailDto.Action.Keyword> keywordList = actionEntity.getKeywords()
                    .stream()
                    .map(key -> ServiceDetailDto.Action.Keyword.builder()
                            .firstKey(key.getFirstKey())
                            .regEx(key.getRegEx())
                            .id(key.getId())
                            .build()
                    ).collect(Collectors.toList());
            action.setKeywords(keywordList);
            actionList.add(action);
        }
        serviceDetailDto.setActions(actionList);

        StringWriter sw = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(ServiceDetailDto.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(serviceDetailDto, sw);
        return sw.toString();
    }
}
