package co.orbu.taejo.version;

import co.orbu.taejo.integration.module.CommandExecutor;
import co.orbu.taejo.version.model.VersionInfo;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

@Component
public class Version implements CommandExecutor {

    private final VersionCheckExecutorFactory versionCheckExecutorFactory;
    private final ListableBeanFactory beanFactory;

    @Inject
    public Version(VersionCheckExecutorFactory versionCheckExecutorFactory, ListableBeanFactory beanFactory) {
        this.versionCheckExecutorFactory = versionCheckExecutorFactory;
        this.beanFactory = beanFactory;
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

    @Nonnull
    private String getFormattedMessage(VersionInfo versionInfo) {
        if (versionInfo == null) {
            return "Unable to get latest release info.";
        }

        return String.format("*%s* (%s)\nRelease notes: %s\nDownload link: %s",
                versionInfo.getName(), versionInfo.getVersion(), versionInfo.getReleaseNotesUrl(), versionInfo.getDownloadUrl());
    }
}
