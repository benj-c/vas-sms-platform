package com.sys.vas.management.service;

import com.sys.vas.management.dto.KeywordDto;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.entity.ActionEntity;
import com.sys.vas.management.dto.entity.KeywordEntity;
import com.sys.vas.management.dto.request.CreateKeywordRequestDto;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.repository.ActionRepository;
import com.sys.vas.management.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KeywordService {

    private KeywordRepository keywordRepository;
    private ActionRepository actionRepository;

    public KeywordService(
            KeywordRepository keywordRepository,
            ActionRepository actionRepository
    ) {
        this.keywordRepository = keywordRepository;
        this.actionRepository = actionRepository;
    }

    public List<KeywordDto> getKeywordsByActionId(Long sid) {
        return keywordRepository.findByActionId(sid);
//        if (acts.isEmpty()) {
//            throw new ApiException(ResponseCodes.KEYWORD_NOT_FOUND, "keywords not found for action:" + sid);
//        }
//        return acts;
    }

    @Transactional
    public void update(KeywordDto requestDto) {
        KeywordEntity fEntity = keywordRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ResponseCodes.KEYWORD_NOT_FOUND, "keyword not found for id:" + requestDto.getId()));
        if (requestDto.getFirstKey() != null) {
            fEntity.setFirstKey(requestDto.getFirstKey());
        }
        if (requestDto.getRegex() != null) {
            fEntity.setRegEx(requestDto.getRegex());
        }
        keywordRepository.save(fEntity);
    }

    public void create(CreateKeywordRequestDto requestDto) {
        ActionEntity actionEntity = actionRepository.findById(requestDto.getActionId())
                .orElseThrow(() -> new ApiException(ResponseCodes.ACTION_NOT_FOUND, "action not found for id:" + requestDto.getActionId()));

        KeywordEntity keywordEntity = new KeywordEntity();
        keywordEntity.setRegEx(requestDto.getRegex());
        keywordEntity.setFirstKey(requestDto.getFirstKey());
        keywordEntity.setAction(actionEntity);

        keywordRepository.save(keywordEntity);
    }
}
