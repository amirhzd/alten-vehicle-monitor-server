package com.alten.service.dto;

public class VehicleStatusDTO {
    private String vehicleId;

    public VehicleStatusDTO() {
    }

    public VehicleStatusDTO(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "VehicleStatusDTO{" +
            "vehicleId='" + vehicleId + '\'' +
            '}';
    }
}
