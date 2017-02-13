package co.orbu.taejo.version.impl;

import co.orbu.taejo.version.VersionReporter;
import co.orbu.taejo.version.model.VersionInfo;
import co.orbu.taejo.version.model.repository.Version;
import co.orbu.taejo.version.repository.VersionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Component
public class VersionReporterImpl implements VersionReporter {

    private final VersionRepository versionRepository;

    @Inject
    public VersionReporterImpl(VersionRepository versionRepository) {
        Assert.notNull(versionRepository);
        this.versionRepository = versionRepository;
    }

    public List<VersionInfo> getAndFlagUnreportedVersions() {
        List<Version> versions = versionRepository.findByWasReported(false);

        ArrayList<VersionInfo> versionInfos = new ArrayList<>();

        for (Version version : versions) {
            VersionInfo versionInfo = new VersionInfo();
            versionInfo.setName(version.getName());
            versionInfo.setVersion(version.getVersion());
            versionInfo.setDownloadUrl(version.getDownloadUrl());
            versionInfo.setReleaseNotesUrl(version.getReleaseNotesUrl());

            versionInfos.add(versionInfo);
            version.setWasReported(true);
        }

        versionRepository.save(versions);
        versionRepository.flush();

        return versionInfos;
    }
}
