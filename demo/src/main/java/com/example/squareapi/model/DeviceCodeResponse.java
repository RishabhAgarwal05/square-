package com.example.squareapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceCodeResponse {

    @JsonProperty("device_code")
    private DeviceCode deviceCode;

    // Getter and Setter for deviceCode
    public DeviceCode getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(DeviceCode deviceCode) {
        this.deviceCode = deviceCode;
    }

    // Inner class to represent the device_code object
    public static class DeviceCode {

        private String id;
        private String name;
        private String code;
        private String productType;
        private String locationId;
        private String pairBy;
        private String createdAt;
        private String status;
        private String statusChangedAt;

        // Getters and Setters for all fields

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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @JsonProperty("product_type")
        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        @JsonProperty("location_id")
        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        @JsonProperty("pair_by")
        public String getPairBy() {
            return pairBy;
        }

        public void setPairBy(String pairBy) {
            this.pairBy = pairBy;
        }

        @JsonProperty("created_at")
        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @JsonProperty("status_changed_at")
        public String getStatusChangedAt() {
            return statusChangedAt;
        }

        public void setStatusChangedAt(String statusChangedAt) {
            this.statusChangedAt = statusChangedAt;
        }
    }
}
