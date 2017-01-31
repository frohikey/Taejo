package co.orbu.taejo.version;

import co.orbu.taejo.version.model.VersionInfo;

import javax.annotation.Nullable;

public interface VersionCheckExecutor {

    /**
     * Gets info about the latest released version.
     *
     * @return Detail about latest release or {@code null} if not detail could be found.
     */
    @Nullable
    VersionInfo getLatestVersion();

}
