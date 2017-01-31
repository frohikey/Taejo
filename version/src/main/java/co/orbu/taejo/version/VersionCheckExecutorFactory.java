package co.orbu.taejo.version;

public interface VersionCheckExecutorFactory {

    VersionCheckExecutor getVersionCheckExecutor(String versionCheck);

}
