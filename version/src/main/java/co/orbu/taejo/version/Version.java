package co.orbu.taejo.version;

import co.orbu.taejo.integration.module.CommandExecutor;
import co.orbu.taejo.version.model.VersionInfo;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jms.Queue;
import java.util.List;

@Component
public class Version implements CommandExecutor {

    private final VersionReporter versionReporter;
    private final VersionCheckExecutorFactory versionCheckExecutorFactory;
    private final ListableBeanFactory beanFactory;
    private final JmsMessagingTemplate jmsMessagingTemplate;
    private final Queue queue;

    @Inject
    public Version(VersionReporter versionReporter, VersionCheckExecutorFactory versionCheckExecutorFactory, ListableBeanFactory beanFactory,
                   JmsMessagingTemplate jmsMessagingTemplate, Queue queue) {

        // TODO: too many stuff here, refactor this!

        Assert.notNull(versionReporter);
        this.versionReporter = versionReporter;

        Assert.notNull(versionCheckExecutorFactory);
        this.versionCheckExecutorFactory = versionCheckExecutorFactory;

        Assert.notNull(beanFactory);
        this.beanFactory = beanFactory;

        Assert.notNull(jmsMessagingTemplate);
        this.jmsMessagingTemplate = jmsMessagingTemplate;

        Assert.notNull(queue);
        this.queue = queue;
    }

    @Nullable
    @Override
    public String doCommand(@Nonnull String[] data) {
        if (data.length == 1) {
            String[] beanNames = beanFactory.getBeanNamesForType(VersionCheckExecutor.class);
            String names = String.join(", ", beanNames);

            return "At the moment I can get you release info for these: " + names;
        }

        VersionCheckExecutor versionCheckExecutor = null;

        try {
            versionCheckExecutor = versionCheckExecutorFactory.getVersionCheckExecutor(data[1]);
        } catch (NoSuchBeanDefinitionException e) {
            // unknown command, not interested in this
        }

        VersionInfo versionInfo = null;

        if (versionCheckExecutor != null) {
            versionInfo = versionCheckExecutor.getLatestVersion();
        }

        return getFormattedMessage(versionInfo);
    }

    @Scheduled(initialDelay = 5_000, fixedDelay = 10_000)
    private void reportNewVersions() {
        List<VersionInfo> versions = versionReporter.getAndFlagUnreportedVersions();

        for (VersionInfo version : versions) {
            jmsMessagingTemplate.convertAndSend(queue, getFormattedMessage(version));
        }
    }

    @Nonnull
    private String getFormattedMessage(VersionInfo versionInfo) {
        if (versionInfo == null) {
            return "Unable to get latest release info.";
        }

        return String.format("*%s* (%s)\nRelease notes: %s\nDownload link: %s",
                versionInfo.getName(), versionInfo.getVersion(), versionInfo.getReleaseNotesUrl(), versionInfo.getDownloadUrl());
    }
}
