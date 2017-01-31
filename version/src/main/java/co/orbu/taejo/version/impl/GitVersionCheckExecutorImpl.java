package co.orbu.taejo.version.impl;

import co.orbu.taejo.version.VersionCheckExecutor;
import co.orbu.taejo.version.model.VersionInfo;
import co.orbu.taejo.version.model.github.Asset;
import co.orbu.taejo.version.model.github.Release;
import co.orbu.taejo.version.service.GitHubService;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Component("git")
public class GitVersionCheckExecutorImpl implements VersionCheckExecutor {

    private final GitHubService gitHubService;

    @Inject
    public GitVersionCheckExecutorImpl(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Nullable
    @Override
    public VersionInfo getLatestVersion() {
        Release release = gitHubService.getVersion("git-for-windows", "git");
        if (release == null) {
            return null;
        }

        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setName(release.getName());
        versionInfo.setVersion(release.getTagName());
        versionInfo.setReleaseNotesUrl(release.getReleaseInfoUrl());

        for (Asset asset : release.getAssets()) {
            if (asset.getDownloadUrl().endsWith("64-bit.exe")) {
                versionInfo.setDownloadUrl(asset.getDownloadUrl());
                break;
            }
        }

        return versionInfo;
    }

}
