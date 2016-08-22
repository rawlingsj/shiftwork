package com.teammachine.staffrostering.planner.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PlannerServiceRestTemplateFactory {

    public RestTemplate getRestTemplate() {
        return  new RestTemplate();
    }

}
