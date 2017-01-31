package co.orbu.taejo.version.model;

public class VersionInfo {

    private String name;
    private String version;
    private String downloadUrl;
    private String releaseNotesUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getReleaseNotesUrl() {
        return releaseNotesUrl;
    }

    public void setReleaseNotesUrl(String releaseNotesUrl) {
        this.releaseNotesUrl = releaseNotesUrl;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", releaseNotesUrl='" + releaseNotesUrl + '\'' +
                '}';
    }
}
