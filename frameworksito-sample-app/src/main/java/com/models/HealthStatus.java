package com.models;

public class HealthStatus {

    private String framework;
    private String version;
    private String status;

    public HealthStatus() {}

    public HealthStatus(String framework, String version, String status) {
        this.framework = framework;
        this.version = version;
        this.status = status;
    }

    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
