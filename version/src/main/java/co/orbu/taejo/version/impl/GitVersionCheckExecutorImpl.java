package co.orbu.taejo.version.impl;

import co.orbu.taejo.version.AbstractVersionCheckExecutor;
import co.orbu.taejo.version.model.github.Asset;
import co.orbu.taejo.version.model.github.Release;
import co.orbu.taejo.version.model.repository.Version;
import co.orbu.taejo.version.repository.VersionRepository;
import co.orbu.taejo.version.service.GitHubService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.Date;

@Component(GitVersionCheckExecutorImpl.COMMAND)
public class GitVersionCheckExecutorImpl extends AbstractVersionCheckExecutor {

    static final String COMMAND = "git";

    private final GitHubService gitHubService;

    @Inject
    public GitVersionCheckExecutorImpl(VersionRepository versionRepository, GitHubService gitHubService) {
        super(versionRepository);

        Assert.notNull(gitHubService);
        this.gitHubService = gitHubService;
    }

    @Override
    protected String getCommandName() {
        return COMMAND;
    }

    @Scheduled(fixedDelay = 3_600_000)
    private void fetchLatestVersion() {
        System.out.println("Fetching " + COMMAND);

        Release release = gitHubService.getVersion("git-for-windows", "git");
        if (release == null) {
            return;
        }

        Version version = versionRepository.findByCommand(COMMAND);
        if (version == null) {
            version = new Version();
            version.setCommand(COMMAND);
        }

        if (release.getName() != null && release.getName().equals(version.getName())) {
            // this version is already in the database
            return;
        }

        System.out.println("Found new version: " + release.getName());

        version.setName(release.getName());
        version.setVersion(release.getTagName());
        version.setReleaseNotesUrl(release.getReleaseInfoUrl());

        for (Asset asset : release.getAssets()) {
            if (asset.getDownloadUrl().endsWith("64-bit.exe")) {
                version.setDownloadUrl(asset.getDownloadUrl());
                break;
            }
        }

        version.setLastUpdate(new Date());

        // do not report if there is no entry in the database at all (this prevents spamming the channel on init)
        version.setWasReported(version.getId() == null);

        versionRepository.saveAndFlush(version);
    }

}
