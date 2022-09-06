package io.kakaoi.service.dto;

import java.time.LocalDateTime;

public final class SampleDTO {

    public static class Info {

        private String id;

        private String name;

        private String description;

        private LocalDateTime createdDt;

        private String createdBy;

        private LocalDateTime updatedDt;

        private String updatedBy;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getCreatedDt() {
            return createdDt;
        }

        public void setCreatedDt(LocalDateTime createdDt) {
            this.createdDt = createdDt;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public LocalDateTime getUpdatedDt() {
            return updatedDt;
        }

        public void setUpdatedDt(LocalDateTime updatedDt) {
            this.updatedDt = updatedDt;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        @Override
        public String toString() {
            return "Info{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdDt=" + createdDt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedDt=" + updatedDt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
        }
    }

    private SampleDTO() {}
}
