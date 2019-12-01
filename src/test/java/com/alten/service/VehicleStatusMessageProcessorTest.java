package com.alten.service;

import com.alten.domain.CustomerVehicle;
import com.alten.domain.CustomerVehicleStatus;
import com.alten.domain.enumeration.VehicleStatus;
import com.alten.repository.CustomerVehicleRepository;
import com.alten.repository.CustomerVehicleStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleStatusMessageProcessorTest {

    VehicleStatusMessageProcessor vehicleStatusMessageProcessor;
    @Mock
    CustomerVehicleStatusRepository customerVehicleStatusRepository;
    @Mock
    CustomerVehicleRepository customerVehicleRepository;

    String msg = "{\"vehicleId\":\"VLUR4X20009093588\",\"status\":\"NOT_CONNECTED\"}";
    String vehicleId = "VLUR4X20009093588";

    @BeforeEach
    public void init() {
        List<CustomerVehicle> customerVehicles = new ArrayList<>();
        customerVehicles.add(new CustomerVehicle(vehicleId));
        when(customerVehicleRepository.findAllByVehicleId(any())).thenReturn(customerVehicles);

        vehicleStatusMessageProcessor = new VehicleStatusMessageProcessor(customerVehicleStatusRepository, customerVehicleRepository);
    }

    @Test
    public void testHandleMessageInternal() {
        Message<?> message = new Message<Object>() {
            @Override
            public Object getPayload() {
                return msg;
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
        vehicleStatusMessageProcessor.handleMessageInternal(message);
        ArgumentCaptor<CustomerVehicleStatus> customerVehicleStatusArgCaptor = ArgumentCaptor.forClass(CustomerVehicleStatus.class);
        verify(customerVehicleStatusRepository,times(1)).save(customerVehicleStatusArgCaptor.capture());
        assertEquals(vehicleId, customerVehicleStatusArgCaptor.getValue().getCustomerVehicle().getVehicleId());
        assertEquals(VehicleStatus.NOT_CONNECTED, customerVehicleStatusArgCaptor.getValue().getStatus());
    }

}
