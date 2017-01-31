package co.orbu.taejo.azure.config;

public class AzureConfigDetail {

    private String entityPath;
    private String namespace;
    private String sasKeyName;
    private String sasKey;
    private String serviceBusRootUri;

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSasKeyName() {
        return sasKeyName;
    }

    public void setSasKeyName(String sasKeyName) {
        this.sasKeyName = sasKeyName;
    }

    public String getSasKey() {
        return sasKey;
    }

    public void setSasKey(String sasKey) {
        this.sasKey = sasKey;
    }

    public String getServiceBusRootUri() {
        return serviceBusRootUri;
    }

    public void setServiceBusRootUri(String serviceBusRootUri) {
        this.serviceBusRootUri = serviceBusRootUri;
    }

    @Override
    public String toString() {
        return "AzureConfigDetail{" +
                "entityPath='" + entityPath + '\'' +
                ", namespace='" + namespace + '\'' +
                ", sasKeyName='" + sasKeyName + '\'' +
                ", sasKey='" + sasKey + '\'' +
                ", serviceBusRootUri='" + serviceBusRootUri + '\'' +
                '}';
    }
}
