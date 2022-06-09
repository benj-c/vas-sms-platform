package com.sys.vas.management.service;

import com.sys.vas.management.dto.entity.ActionEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;

@Slf4j
public class AbstractEntityListener {

//    <T> T getEntityName(Object o) {
//        if (o instanceof ActionEntity) {
//            return (ActionEntity) o;
//        }
//    }

    @PrePersist
    void onPrePersist(Object obj) {
        log.info("Entity is about to be saved:{}", obj);
    }

    @PostUpdate
    void onPostUpdate(Object obj) {
        log.info("Entity updated:{}", obj);
    }
}
