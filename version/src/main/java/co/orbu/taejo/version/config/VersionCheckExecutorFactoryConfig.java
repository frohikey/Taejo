package co.orbu.taejo.version.config;

import co.orbu.taejo.version.VersionCheckExecutorFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionCheckExecutorFactoryConfig {

    @Bean("versionCheckExecutorFactory")
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean serviceLocator = new ServiceLocatorFactoryBean();
        serviceLocator.setServiceLocatorInterface(VersionCheckExecutorFactory.class);
        return serviceLocator;
    }

}
