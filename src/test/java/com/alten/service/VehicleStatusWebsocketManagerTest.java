package com.alten.service;

import com.alten.domain.CustomerVehicleStatus;
import com.alten.domain.CustomerVehicleStatusView;
import com.alten.repository.CustomerVehicleStatusRepository;
import com.alten.service.dto.CustomerVehicleStatusFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VehicleStatusWebsocketManagerTest {

    VehicleStatusWebsocketManager vehicleStatusWebsocketManager;

    @Mock
    WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap;
    @Mock
    CustomerVehicleStatusRepository customerVehicleStatusRepository;
    @Mock
    SimpMessageSendingOperations messagingTemplate;

    String expectedCustomerVehicleId = "VLUR4X20009093588";

    @BeforeEach
    public void init() {
        Map<String, CustomerVehicleStatusFilter> subscriberMap = new HashMap<>();
        CustomerVehicleStatusFilter filter = new CustomerVehicleStatusFilter();
        filter.setCustomerId(1L);
        subscriberMap.put("34hj65", filter);
        when(websocketSubscribersMap.getSubscriptions()).thenReturn(subscriberMap);

        List<CustomerVehicleStatusView> statusList = new ArrayList<>();
        CustomerVehicleStatusView customerVehicleStatus = new CustomerVehicleStatusView();
        customerVehicleStatus.setVehicleId(expectedCustomerVehicleId);
        statusList.add(customerVehicleStatus);
        when(customerVehicleStatusRepository.findAllByCustomerIdAndStatus(eq(1L), any()))
            .thenReturn(statusList);

        vehicleStatusWebsocketManager = new VehicleStatusWebsocketManager(websocketSubscribersMap
            , customerVehicleStatusRepository, messagingTemplate);
    }

    @Test
    public void testUpdateSubscribers() {
        vehicleStatusWebsocketManager.updateSubscribers();
        ArgumentCaptor<List<CustomerVehicleStatusView>> statusListArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(messagingTemplate, times(1)).convertAndSendToUser(
            eq("34hj65")
            , anyString()
            , statusListArgumentCaptor.capture()
            , anyMap());
        assertEquals(expectedCustomerVehicleId, statusListArgumentCaptor.getValue().get(0).getVehicleId());
    }

}
