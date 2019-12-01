package com.alten.service.dto;

import com.alten.domain.enumeration.VehicleStatus;

public class CustomerVehicleStatusFilter {
    private VehicleStatus status;
    private Long customerId;

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
