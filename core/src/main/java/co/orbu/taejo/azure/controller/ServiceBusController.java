package co.orbu.taejo.azure.controller;

import co.orbu.taejo.azure.config.AzureConfig;
import co.orbu.taejo.azure.config.AzureConfigDetail;
import co.orbu.taejo.azure.model.IncomingMessage;
import co.orbu.taejo.azure.model.OutgoingMessage;
import co.orbu.taejo.integration.module.CommandExecutor;
import co.orbu.taejo.integration.module.CommandExecutorFactory;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Main entry point for checking new messages coming from Slack.
 * <p>NOTE: This code will be replaced with direct connection to Slack instead of using Azure's Service Bus.</p>
 */
@Controller
public class ServiceBusController {

    private final CommandExecutorFactory commandExecutorFactory;
    private final ListableBeanFactory beanFactory;

    private final String receiveEntityPath;
    private final String sendEntityPath;
    private final Configuration configurationReceive;
    private final Configuration configurationSend;

    @Inject
    public ServiceBusController(AzureConfig azureConfig, CommandExecutorFactory commandExecutorFactory, ListableBeanFactory beanFactory) {
        this.commandExecutorFactory = commandExecutorFactory;
        this.beanFactory = beanFactory;

        AzureConfigDetail receive = azureConfig.getReceive();
        receiveEntityPath = receive.getEntityPath();
        configurationReceive = ServiceBusConfiguration.configureWithSASAuthentication(receive.getNamespace(), receive.getSasKeyName(),
                receive.getSasKey(), receive.getServiceBusRootUri());

        AzureConfigDetail send = azureConfig.getSend();
        sendEntityPath = send.getEntityPath();
        configurationSend = ServiceBusConfiguration.configureWithSASAuthentication(send.getNamespace(), send.getSasKeyName(),
                send.getSasKey(), send.getServiceBusRootUri());
    }

    @PostConstruct
    private void startReceivingMessages() {
        try {
            ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
            opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

            ServiceBusContract service = ServiceBusService.create(configurationReceive);

            String initCommand = "!" + receiveEntityPath + " ";

            while (true) {
                ReceiveQueueMessageResult messageResult = service.receiveQueueMessage(receiveEntityPath, opts);

                BrokeredMessage message = messageResult.getValue();
                if (message != null && message.getMessageId() != null) {
                    System.out.println("MessageID: " + message.getMessageId());
                    System.out.println("Date: " + message.getDate());

                    ObjectMapper mapper = new ObjectMapper();
                    IncomingMessage incomingMessage = mapper.readValue(message.getBody(), IncomingMessage.class);

                    String[] params = incomingMessage.getCommand().replace(initCommand, "").split(" ");
                    String result = executeCommand(params);
                    if (result != null) {
                        sendMessage(result);
                    }

                    service.deleteMessage(message);
                }
            }
        } catch (ServiceException | IOException e) {
            System.out.println("Exception encountered:");
            System.out.println(e.getMessage());
        }
    }

    @Nullable
    private String executeCommand(String[] params) {
        CommandExecutor commandExecutor = null;

        try {
            commandExecutor = commandExecutorFactory.getCommandExecutor(params[0]);
        } catch (NoSuchBeanDefinitionException e) {
            // unknown command, not interested in this
        }

        if (commandExecutor == null) {
            String[] beanNames = beanFactory.getBeanNamesForType(CommandExecutor.class);
            String names = String.join(", ", beanNames);
            return "Unknown command, available commands: " + names;
        }

        return commandExecutor.doCommand(params);
    }

    private void sendMessage(String msg) {
        OutgoingMessage outgoingMessage = new OutgoingMessage();
        outgoingMessage.setMessage(msg);

        sendMessage(outgoingMessage);
    }

    private void sendMessage(OutgoingMessage outgoingMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String outgoingMessageJson = mapper.writeValueAsString(outgoingMessage);
            System.out.println("Serialized message: " + outgoingMessageJson);

            BrokeredMessage message = new BrokeredMessage(outgoingMessageJson);

            message.setContentType("application/json");
            message.setLabel(sendEntityPath);

            ServiceBusContract service = ServiceBusService.create(configurationSend);
            service.sendQueueMessage(sendEntityPath, message);
        } catch (ServiceException | IOException e) {
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

}
