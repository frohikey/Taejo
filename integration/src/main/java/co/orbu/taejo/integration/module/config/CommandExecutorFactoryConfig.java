package co.orbu.taejo.integration.module.config;

import co.orbu.taejo.integration.module.CommandExecutorFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandExecutorFactoryConfig {

    @Bean("commandExecutorFactory")
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean serviceLocator = new ServiceLocatorFactoryBean();
        serviceLocator.setServiceLocatorInterface(CommandExecutorFactory.class);
        return serviceLocator;
    }

}
