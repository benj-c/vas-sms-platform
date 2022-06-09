package com.sys.vas.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "service")
public class ServiceDetailDto extends ServiceDto {

    private List<Action> actions;

    @Data
    @Builder
    public static class Action {
        private Long id;
        private String description;
        private Api api;
        private List<Keyword> keywords;

        @Data
        @Builder
        public static class Keyword {
            private Long id;
            private String firstKey;
            private String regEx;
        }

        @Data
        @Builder
        public static class Api {
            private Long id;
            private String description;
            private String name;
            private String xml;
        }
    }
}
