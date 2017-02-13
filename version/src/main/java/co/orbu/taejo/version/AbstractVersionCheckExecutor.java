package co.orbu.taejo.version;

import co.orbu.taejo.version.model.VersionInfo;
import co.orbu.taejo.version.model.repository.Version;
import co.orbu.taejo.version.repository.VersionRepository;
import org.springframework.util.Assert;

import javax.annotation.Nullable;

public abstract class AbstractVersionCheckExecutor implements VersionCheckExecutor {

    protected final VersionRepository versionRepository;

    protected AbstractVersionCheckExecutor(VersionRepository versionRepository) {
        Assert.notNull(versionRepository);
        this.versionRepository = versionRepository;
    }

    protected abstract String getCommandName();

    @Nullable
    @Override
    public VersionInfo getLatestVersion() {
        Version version = versionRepository.findByCommand(getCommandName());
        if (version == null) {
            return null;
        }

        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setName(version.getName());
        versionInfo.setVersion(version.getVersion());
        versionInfo.setDownloadUrl(version.getDownloadUrl());
        versionInfo.setReleaseNotesUrl(version.getReleaseNotesUrl());

        return versionInfo;
    }
}
