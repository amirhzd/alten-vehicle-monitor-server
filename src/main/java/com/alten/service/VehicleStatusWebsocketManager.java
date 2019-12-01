package com.alten.service;

import com.alten.domain.CustomerVehicleStatus;
import com.alten.repository.CustomerVehicleStatusRepository;
import com.alten.service.dto.CustomerVehicleStatusFilter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query and send the latest vehicle statuses to the subscribed web-sockets
 */
@Service
public class VehicleStatusWebsocketManager {

    private final WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap;
    private final CustomerVehicleStatusRepository customerVehicleStatusRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public VehicleStatusWebsocketManager(WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap, CustomerVehicleStatusRepository customerVehicleStatusRepository, SimpMessageSendingOperations messagingTemplate) {
        this.websocketSubscribersMap = websocketSubscribersMap;
        this.customerVehicleStatusRepository = customerVehicleStatusRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void updateSubscribers() {
        websocketSubscribersMap.getSubscriptions().entrySet().stream()
            .forEach(entry -> {
                List<CustomerVehicleStatus> statuses = customerVehicleStatusRepository.findAllByCustomerIdAndStatus(entry.getValue().getCustomerId()
                    , entry.getValue().getStatus());
                try {
                    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                        .create(SimpMessageType.MESSAGE);
                    headerAccessor.setSessionId(entry.getKey());
                    headerAccessor.setLeaveMutable(true);
                    messagingTemplate.convertAndSendToUser(entry.getKey(), "/queue/get-vehicle-statuses", statuses, headerAccessor.getMessageHeaders());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }

}
