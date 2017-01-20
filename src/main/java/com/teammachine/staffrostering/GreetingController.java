package com.teammachine.staffrostering;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public JobStatusUpdate greeting(HelloMessage message) throws Exception {
        return new JobStatusUpdate();
    }

}
