package com.alten.config;

import com.alten.service.VehicleStatusMessageProcessor;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageHandler;

/**
 * MQTT queue configuration
 * Outbound is used to get data from VehicleStatusFaker and Inbound is used to get the latest statuses from
 * the MQTT queue.
 *
 *  * @author amir
 */
@Configuration
public class MqttConfiguration {

    private final ApplicationProperties applicationProperties;
    private final VehicleStatusMessageProcessor vehicleStatusMessageProcessor;
    private final VehicleStatusFaker vehicleStatusFaker;

    public MqttConfiguration(ApplicationProperties applicationProperties, VehicleStatusMessageProcessor vehicleStatusMessageProcessor, VehicleStatusFaker vehicleStatusFaker) {
        this.applicationProperties = applicationProperties;
        this.vehicleStatusMessageProcessor = vehicleStatusMessageProcessor;
        this.vehicleStatusFaker = vehicleStatusFaker;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { applicationProperties.getMqttBorkerConfig().getUrl() });
        options.setUserName(applicationProperties.getMqttBorkerConfig().getUsername());
        options.setPassword(applicationProperties.getMqttBorkerConfig().getPassword().toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }


    @Bean
    public IntegrationFlow mqttOutFlow() {
        return IntegrationFlows.from(vehicleStatusFaker.getStreamReadingMessageSource(),
            e -> e.poller(Pollers.fixedDelay(applicationProperties.getStatusFakerReadPipeDelay())))
            .handle(mqttOutbound())
            .get();
    }

    @Bean
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("vehicleStatusPublisher", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(applicationProperties.getMqttBorkerConfig().getVehicleStatusTopic());
        return messageHandler;
    }


    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("vehicleStatusConsumer",
            mqttClientFactory(), applicationProperties.getMqttBorkerConfig().getVehicleStatusTopic());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(mqttInbound())
            .handle(vehicleStatusMessageProcessor)
            .get();
    }


}
