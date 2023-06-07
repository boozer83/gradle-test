package io.kakaoi.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public final class TrailDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Data {

        private String eventVersion;

        private String eventTime;

        private String userName;

        private String userId;

        private String userAgent;

        private String sourceIpAddress;

        private String kicRegion;

        private String domainId;

        private String domainName;

        private String projectId;

        private String projectName;

        private String eventSource;

        private String eventName;

        private String resourceName;

        private String resourceId;

        private String resourceType;

        private String otherAdditionalInfo;

        public String getEventVersion() {
            return eventVersion;
        }

        public void setEventVersion(String eventVersion) {
            this.eventVersion = eventVersion;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getSourceIpAddress() {
            return sourceIpAddress;
        }

        public void setSourceIpAddress(String sourceIpAddress) {
            this.sourceIpAddress = sourceIpAddress;
        }

        public String getKicRegion() {
            return kicRegion;
        }

        public void setKicRegion(String kicRegion) {
            this.kicRegion = kicRegion;
        }

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getEventSource() {
            return eventSource;
        }

        public void setEventSource(String eventSource) {
            this.eventSource = eventSource;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getOtherAdditionalInfo() {
            return otherAdditionalInfo;
        }

        public void setOtherAdditionalInfo(String otherAdditionalInfo) {
            this.otherAdditionalInfo = otherAdditionalInfo;
        }

        @Override
        public String toString() {
            return "{" +
                "eventVersion='" + eventVersion + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", sourceIpAddress='" + sourceIpAddress + '\'' +
                ", kicRegion='" + kicRegion + '\'' +
                ", domainId='" + domainId + '\'' +
                ", domainName='" + domainName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", eventSource='" + eventSource + '\'' +
                ", eventName='" + eventName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", otherAdditionalInfo='" + otherAdditionalInfo + '\'' +
                '}';
        }
    }

    private TrailDTO() {}
}
