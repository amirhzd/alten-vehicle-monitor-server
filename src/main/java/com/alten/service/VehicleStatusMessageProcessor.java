package com.alten.service;

import com.alten.domain.CustomerVehicle;
import com.alten.domain.CustomerVehicleStatus;
import com.alten.repository.CustomerVehicleRepository;
import com.alten.repository.CustomerVehicleStatusRepository;
import com.alten.service.dto.VehicleStatusDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.integration.expression.ExpressionUtils;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Exclusive message parser from the messages received from MQTT queue to VehicleStatusDTO instances that are saved
 * in the DB finally.
 *
 * @author amir
 */
@Service
public class VehicleStatusMessageProcessor extends AbstractMessageHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CustomerVehicleStatusRepository customerVehicleStatusRepository;
    private final CustomerVehicleRepository customerVehicleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Expression expression;
    private final EvaluationContext evaluationContext;

    public VehicleStatusMessageProcessor(CustomerVehicleStatusRepository customerVehicleStatusRepository, CustomerVehicleRepository customerVehicleRepository) {
        this.customerVehicleStatusRepository = customerVehicleStatusRepository;
        this.customerVehicleRepository = customerVehicleRepository;
        this.expression = EXPRESSION_PARSER.parseExpression("payload");
        this.evaluationContext = ExpressionUtils.createStandardEvaluationContext();
    }

    private String getMessage(Message<?> message) {
        Object topicMessage = this.expression.getValue(this.evaluationContext, message);
        if (topicMessage instanceof Throwable) {
            log.error("getting message from mqtt message failed", topicMessage);
        }
        return (String) topicMessage;
    }

    @Override
    protected void handleMessageInternal(Message<?> message) {
        try {
            VehicleStatusDTO vehicleStatusDTO = objectMapper.readValue(getMessage(message), VehicleStatusDTO.class);
            List<CustomerVehicle> vehicles = customerVehicleRepository.findAllByVehicleId(vehicleStatusDTO.getVehicleId());
            if (vehicles != null && vehicles.size() > 0) {
                CustomerVehicle customerVehicle = vehicles.get(0);
                CustomerVehicleStatus customerVehicleStatus = new CustomerVehicleStatus();
                customerVehicleStatus.customerVehicle(customerVehicle);
                customerVehicleStatus.setStatus(vehicleStatusDTO.getStatus());
                customerVehicleStatus.setTimestamp(LocalDateTime.now());
                customerVehicleStatusRepository.save(customerVehicleStatus);
            }

        } catch (IOException e) {
            log.error("{} can not be converted to VehicleStatusDTO object", message, e);
            throw new RuntimeException(e);
        }
    }

}
