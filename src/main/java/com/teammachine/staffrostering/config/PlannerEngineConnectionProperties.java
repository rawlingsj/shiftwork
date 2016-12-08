package com.teammachine.staffrostering.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plannerengine", ignoreUnknownFields = true)
public class PlannerEngineConnectionProperties {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
