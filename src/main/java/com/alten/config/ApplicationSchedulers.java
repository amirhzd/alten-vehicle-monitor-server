package com.alten.config;

import com.alten.service.VehicleStatusWebsocketManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author amir
 *
 */
@Configuration
@EnableScheduling
public class ApplicationSchedulers {

    private final VehicleStatusWebsocketManager vehicleStatusWebsocketManager;
    private final VehicleStatusFaker vehicleStatusFaker;

    public ApplicationSchedulers(VehicleStatusWebsocketManager vehicleStatusWebsocketManager, VehicleStatusFaker vehicleStatusFaker) {
        this.vehicleStatusWebsocketManager = vehicleStatusWebsocketManager;
        this.vehicleStatusFaker = vehicleStatusFaker;
    }


    /**
     * schedules the process of sending the latest statuses of vehicles to the subscribed web-sockets every minute.
     */
    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void scheduleUpdatingWebsocketSubscribers() {
        vehicleStatusWebsocketManager.updateSubscribers();
    }

    /**
     * schedules the process of generating fake statues for all the vehicles in the DB and sending them to the
     * specified mqtt queue from which the new statues are imported into the DB.
     */
    @Scheduled(fixedDelay = 60_000, initialDelay = 30_000)
    public void scheduleVehicleStatusFaker() {
        vehicleStatusFaker.start();
    }

}
