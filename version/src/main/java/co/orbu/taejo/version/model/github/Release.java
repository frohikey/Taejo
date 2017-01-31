package co.orbu.taejo.version.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Release {

    private String name;

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("html_url")
    private String releaseInfoUrl;

    private List<Asset> assets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getReleaseInfoUrl() {
        return releaseInfoUrl;
    }

    public void setReleaseInfoUrl(String releaseInfoUrl) {
        this.releaseInfoUrl = releaseInfoUrl;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "Release{" +
                "name='" + name + '\'' +
                ", tagName='" + tagName + '\'' +
                ", releaseInfoUrl='" + releaseInfoUrl + '\'' +
                ", assets=" + assets +
                '}';
    }
}
