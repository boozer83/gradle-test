package io.kakaoi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

public final class MeteringDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Data {
        private String message;
        private String serviceId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public String toString() {
            return "{" +
                "\"message\":\"" + message + '\"' +
                ",\"service_id\":\"" + serviceId + '\"' +
                '}';
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Log {

        private String kicDomainId;

        private int kicProductId;

        private String kicProjectId;

        private String kicRegionCode;

        private String kicResourceId;

        private Boolean isMt;

        private String comment;

        private List<Metadata> metadata;

        private String meteringStartDatetime;

        private String meteringInterval;

        private String valueOption;

        public String getKicDomainId() {
            return kicDomainId;
        }

        public void setKicDomainId(String kicDomainId) {
            this.kicDomainId = kicDomainId;
        }

        public int getKicProductId() {
            return kicProductId;
        }

        public void setKicProductId(int kicProductId) {
            this.kicProductId = kicProductId;
        }

        public String getKicProjectId() {
            return kicProjectId;
        }

        public void setKicProjectId(String kicProjectId) {
            this.kicProjectId = kicProjectId;
        }

        public String getKicRegionCode() {
            return kicRegionCode;
        }

        public void setKicRegionCode(String kicRegionCode) {
            this.kicRegionCode = kicRegionCode;
        }

        public String getKicResourceId() {
            return kicResourceId;
        }

        public void setKicResourceId(String kicResourceId) {
            this.kicResourceId = kicResourceId;
        }

        @JsonProperty("is_mt")
        public Boolean getMt() {
            return isMt;
        }

        @JsonProperty("is_mt")
        public void setMt(Boolean mt) {
            isMt = mt;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public List<Metadata> getMetadata() {
            return metadata;
        }

        public void setMetadata(List<Metadata> metadata) {
            this.metadata = metadata;
        }

        public String getMeteringStartDatetime() {
            return meteringStartDatetime;
        }

        public void setMeteringStartDatetime(String meteringStartDatetime) {
            this.meteringStartDatetime = meteringStartDatetime;
        }

        public String getMeteringInterval() {
            return meteringInterval;
        }

        public void setMeteringInterval(String meteringInterval) {
            this.meteringInterval = meteringInterval;
        }

        public String getValueOption() {
            return valueOption;
        }

        public void setValueOption(String valueOption) {
            this.valueOption = valueOption;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Metadata {
//        private String activeStartDatetime;
//
//        private String activeEndDatetime;

        private Boolean isBilling;

        private long value;

        private Object optional;

//        @JsonProperty("active_start_datetime")
//        public String getActiveStartDatetime() {
//            return activeStartDatetime;
//        }
//
//        @JsonProperty("active_start_datetime")
//        public void setActiveStartDatetime(String activeStartDatetime) {
//            this.activeStartDatetime = activeStartDatetime;
//        }
//
//        @JsonProperty("active_end_datetime")
//        public String getActiveEndDatetime() {
//            return activeEndDatetime;
//        }
//
//        @JsonProperty("active_end_datetime")
//        public void setActiveEndDatetime(String activeEndDatetime) {
//            this.activeEndDatetime = activeEndDatetime;
//        }

        @JsonProperty("is_billing")
        public Boolean getBilling() {
            return isBilling;
        }

        @JsonProperty("is_billing")
        public void setBilling(Boolean billing) {
            isBilling = billing;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public Object getOptional() {
            return optional;
        }

        public void setOptional(Object optional) {
            this.optional = optional;
        }
    }

    private MeteringDTO() {}
}
