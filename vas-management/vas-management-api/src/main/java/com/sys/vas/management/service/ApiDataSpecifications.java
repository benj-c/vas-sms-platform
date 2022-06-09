package com.sys.vas.management.service;

import com.sys.vas.management.dto.entity.ApiEntity;
import org.springframework.data.jpa.domain.Specification;

public class ApiDataSpecifications {

    public static Specification<ApiEntity> withName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<ApiEntity> withName_(String f) {
        if (f == null) {
            return null;
        } else {
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("name").get("field"), f);
        }
    }

}
