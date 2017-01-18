package com.teammachine.staffrostering;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by bahadyr on 1/19/17.
 */
@Component
public class ScheduledUpdatesOnTopic{

    @Autowired
    private SimpMessagingTemplate template;

//    @Scheduler(fixedDelay=300)
//    public void publishUpdates(String text){
//        template.convertAndSend("/topic/greetings", text);
//    }
}
