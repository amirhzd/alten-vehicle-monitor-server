package com.alten.service.dto;

import com.alten.domain.enumeration.VehicleStatus;

public class VehicleStatusDTO {
    private String vehicleId;
    private VehicleStatus status;

    public VehicleStatusDTO() {
    }

    public VehicleStatusDTO(String vehicleId, VehicleStatus status) {
        this.vehicleId = vehicleId;
        this.status = status;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VehicleStatusDTO{" +
            "vehicleId='" + vehicleId + '\'' +
            ", status=" + status +
            '}';
    }
}
