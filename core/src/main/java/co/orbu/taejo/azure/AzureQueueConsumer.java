package co.orbu.taejo.azure;

import co.orbu.taejo.azure.controller.ServiceBusController;
import co.orbu.taejo.integration.module.config.QueueConfig;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;

@Component
public class AzureQueueConsumer {

    private final ServiceBusController serviceBusController;

    @Inject
    public AzureQueueConsumer(ServiceBusController serviceBusController) {
        Assert.notNull(serviceBusController);
        this.serviceBusController = serviceBusController;
    }

    @JmsListener(destination = QueueConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        serviceBusController.sendMessage(message);
    }

}
