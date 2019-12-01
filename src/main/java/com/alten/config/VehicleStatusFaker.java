package com.alten.config;


import com.alten.domain.CustomerVehicle;
import com.alten.domain.enumeration.VehicleStatus;
import com.alten.repository.CustomerVehicleRepository;
import com.alten.service.dto.VehicleStatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.stream.ByteStreamReadingMessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Random;

/**
 * Call the start method to start the process and receive the data in another thread by calling getStreamReadingMessageSource
 * to get the internal input-stream wrapped inside a ByteStreamReadingMessageSource.
 * => This class must always remain singleton.
 *
 *  * @author amir
 */
@Component
public class VehicleStatusFaker {

    //ByteStreamReadingMessageSource
    private final MessageSource streamReadingMessageSource;
    private final PipedInputStream pipedInputStream;
    private final PipedOutputStream pipedOutputStream;
    private final CustomerVehicleRepository customerVehicleRepository;
    private final ApplicationProperties applicationProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VehicleStatusFaker(CustomerVehicleRepository customerVehicleRepository, ApplicationProperties applicationProperties) {
        this.customerVehicleRepository = customerVehicleRepository;
        this.applicationProperties = applicationProperties;
        pipedInputStream = new PipedInputStream();
        try {
            pipedOutputStream = new PipedOutputStream(pipedInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        streamReadingMessageSource = new ByteStreamReadingMessageSource(pipedInputStream);
    }


    /**
     * Generating fake statues for all the vehicles in the DB and sending them to the
     * specified mqtt queue from which the new statues are imported into the DB.
     */
    public void start() {
        List<CustomerVehicle> vehicleList = customerVehicleRepository.findAll();
        vehicleList.stream().forEach(customerVehicle -> {
            Random rand = new Random();
            boolean randBool = rand.nextBoolean();
            VehicleStatus vehicleStatus  = randBool ? VehicleStatus.CONNECTED : VehicleStatus.NOT_CONNECTED;
            VehicleStatusDTO vehicleStatusDTO = new VehicleStatusDTO(customerVehicle.getVehicleId(), vehicleStatus);
            try {
                String data = objectMapper.writer().writeValueAsString(vehicleStatusDTO);
                try {
                    if (data != null) {
                        pipedOutputStream.write(data.getBytes());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Thread.sleep(applicationProperties.getStatusFakerWritePipeDelay());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public MessageSource getStreamReadingMessageSource() {
        return streamReadingMessageSource;
    }

}
