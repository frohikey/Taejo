package co.orbu.taejo.azure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "azure")
@Component
public class AzureConfig {

    private AzureConfigDetail receive;
    private AzureConfigDetail send;

    public AzureConfigDetail getReceive() {
        return receive;
    }

    public void setReceive(AzureConfigDetail receive) {
        this.receive = receive;
    }

    public AzureConfigDetail getSend() {
        return send;
    }

    public void setSend(AzureConfigDetail send) {
        this.send = send;
    }

    @Override
    public String toString() {
        return "AzureConfig{" +
                "receive=" + receive +
                ", send=" + send +
                '}';
    }
}
