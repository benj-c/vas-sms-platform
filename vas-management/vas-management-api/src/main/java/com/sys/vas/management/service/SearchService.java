package com.sys.vas.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SearchService {

    private static final String FILTER_API = "api";
    private static final String FILTER_SERVICE = "svr";
    private static final String FILTER_SMS_HISTORY = "smsh";

//    public List<Object> search(String query) {
//        String[] criteria = query.split(",");

//        Specification<CvEntity> spec = Specification.where(withFilterable());
//        for (int i = 0; i < criterias.length; i++) {
//            String[] qParts = criterias[i].split(":");
//            if (FILTER_JOB_SECTOR.equals(qParts[0])) {
//                spec = spec.and(withJobField(qParts[1]));
//            } else if (FILTER_DEGREE_LEVEL.equals(qParts[0])) {
//                spec = spec.and(withDegreeLevel(Integer.valueOf(qParts[1])));
//            } else if (FILTER_EDU_QUALIFICATION.equals(qParts[0])) {
//                spec = spec.and(withEduQualification(qParts[1]));
//            } else if (FILTER_PROFESSIONAL_QUALIFICATION.equals(qParts[0])) {
//                spec = spec.and(withEduQualification(qParts[1]));
//            } else if (FILTER_SKILL.equals(qParts[0])) {
//                spec = spec.and(withEduQualification(qParts[1]));
//            } else if (FILTER_WORK_EXPERIENCE.equals(qParts[0])) {
//                spec = spec.and(withEduQualification(qParts[1]));
//            }
//        }
//        return getCvRepository().findAll(spec);

//    }
}
