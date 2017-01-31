package co.orbu.taejo.version.service;

import co.orbu.taejo.version.model.github.Release;

import javax.annotation.Nullable;

public interface GitHubService {

    @Nullable
    Release getVersion(String user, String project);

}
