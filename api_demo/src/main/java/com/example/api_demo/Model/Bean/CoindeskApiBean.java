package com.example.api_demo.Model.Bean;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class CoindeskApiBean {
    private Long id;
    private Time time;
    private String disclaimer;
    private String chartName;
    private Map<String, BpiDetail> bpi;

    @Getter
    @Setter
    public static class Time {
        private String updated;
        private String updatedISO;
        private String updateduk;
    }

    @Getter
    @Setter
    public static class BpiDetail {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private double rate_float;
    }
}
