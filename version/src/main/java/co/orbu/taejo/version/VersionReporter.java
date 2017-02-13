package co.orbu.taejo.version;

import co.orbu.taejo.version.model.VersionInfo;

import java.util.List;

public interface VersionReporter {

    List<VersionInfo> getAndFlagUnreportedVersions();

}
