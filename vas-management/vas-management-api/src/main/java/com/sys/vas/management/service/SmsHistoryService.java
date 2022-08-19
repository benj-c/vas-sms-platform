package com.sys.vas.management.service;

import com.sys.vas.management.dto.entity.SmsHistoryEntity;
import com.sys.vas.management.repository.SmsHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsHistoryService {

    private SmsHistoryRepository historyRepository;

    SmsHistoryService(SmsHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<SmsHistoryEntity> getHistoryByMsisdn(String msisdn) {
        return historyRepository.findByMsisdnOrderByIdDesc(msisdn);
    }

    public void save() {

    }
}
