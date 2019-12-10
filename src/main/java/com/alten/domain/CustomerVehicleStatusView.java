package com.alten.domain;

import com.alten.domain.enumeration.VehicleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class CustomerVehicleStatusView {

    private VehicleStatus status;

    private LocalDateTime timestamp;

    private String vehicleId;

    private String registrationPlate;

    private String fullName;

    @JsonIgnore
    private Long customerId;

    public CustomerVehicleStatusView() {
    }

    public CustomerVehicleStatusView(String status, LocalDateTime timestamp, String vehicleId, String registrationPlate, String fullName, Long customerId) {
        this.status = VehicleStatus.valueOf(status);
        this.timestamp = timestamp;
        this.vehicleId = vehicleId;
        this.registrationPlate = registrationPlate;
        this.fullName = fullName;
        this.customerId = customerId;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
