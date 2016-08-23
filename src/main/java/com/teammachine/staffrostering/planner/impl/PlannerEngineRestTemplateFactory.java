package com.teammachine.staffrostering.planner.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PlannerEngineRestTemplateFactory {

    public RestTemplate getRestTemplate() {
        return  new RestTemplate();
    }

}
