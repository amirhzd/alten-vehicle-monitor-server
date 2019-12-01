package com.alten.config;

import com.alten.domain.CustomerVehicle;
import com.alten.repository.CustomerVehicleRepository;
import com.alten.service.dto.VehicleStatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleStatusFakerTest {

    @Mock
    CustomerVehicleRepository customerVehicleRepository;

    static ApplicationProperties applicationProperties;

    ObjectMapper objectMapper = new ObjectMapper();

    VehicleStatusFaker vehicleStatusFaker;

    String[] fakeVehicleIds = {
        "YS2R4X20005399401",
        "VLUR4X20009093588",
        "VLUR4X20009048066",
        "YS2R4X20005388011",
        "YS2R4X20005387949"
    };

    @BeforeAll
    public static void initApp() {
        applicationProperties = new ApplicationProperties();
        applicationProperties.setStatusFakerReadPipeDelay(200);
        applicationProperties.setStatusFakerWritePipeDelay(300);
    }

    @BeforeEach
    public void init() {
        vehicleStatusFaker = new VehicleStatusFaker(customerVehicleRepository, applicationProperties);
        ArrayList<CustomerVehicle> customerVehicles = new ArrayList<>();
        Arrays.stream(fakeVehicleIds).forEach(vId -> customerVehicles.add(new CustomerVehicle(vId)));
        when(customerVehicleRepository.findAll()).thenReturn(customerVehicles);
    }

    @Test
    public void testFakerStart() throws InterruptedException {
        List<String> receivedFakeVehicleIds = new ArrayList<>();
        Thread readerThread = new Thread(() -> {
            MessageSource messageSource = vehicleStatusFaker.getStreamReadingMessageSource();
            try {
                while (receivedFakeVehicleIds.size() < 5) {
                    Message receive = messageSource.receive();
                    if (receive != null && receive.getPayload() != null) {
                        String s = new String((byte[]) receive.getPayload());
                        VehicleStatusDTO vehicleStatusDTO = objectMapper.readValue(s, VehicleStatusDTO.class);
                        receivedFakeVehicleIds.add(vehicleStatusDTO.getVehicleId());
                    }
                    Thread.sleep(applicationProperties.getStatusFakerReadPipeDelay());
                }
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        readerThread.start();
        vehicleStatusFaker.start();
        assertArrayEquals(fakeVehicleIds, receivedFakeVehicleIds.toArray(new String[receivedFakeVehicleIds.size()]));
    }
}
