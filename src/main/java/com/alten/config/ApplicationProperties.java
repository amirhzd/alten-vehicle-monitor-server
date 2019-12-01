package com.alten.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Altenvehiclemonitorserver.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private int statusFakerReadPipeDelay;
    private int statusFakerWritePipeDelay;
    private MqttBorkerConfig mqttBorkerConfig;

    public int getStatusFakerReadPipeDelay() {
        return statusFakerReadPipeDelay;
    }

    public void setStatusFakerReadPipeDelay(int statusFakerReadPipeDelay) {
        this.statusFakerReadPipeDelay = statusFakerReadPipeDelay;
    }

    public int getStatusFakerWritePipeDelay() {
        return statusFakerWritePipeDelay;
    }

    public void setStatusFakerWritePipeDelay(int statusFakerWritePipeDelay) {
        this.statusFakerWritePipeDelay = statusFakerWritePipeDelay;
    }

    public MqttBorkerConfig getMqttBorkerConfig() {
        return mqttBorkerConfig;
    }

    public void setMqttBorkerConfig(MqttBorkerConfig mqttBorkerConfig) {
        this.mqttBorkerConfig = mqttBorkerConfig;
    }

    public static class MqttBorkerConfig {
        private String url;
        private String username;
        private String password;
        private String vehicleStatusTopic;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVehicleStatusTopic() {
            return vehicleStatusTopic;
        }

        public void setVehicleStatusTopic(String vehicleStatusTopic) {
            this.vehicleStatusTopic = vehicleStatusTopic;
        }
    }

}
