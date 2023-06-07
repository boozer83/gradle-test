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

    private MeteringDTO() {}
}
