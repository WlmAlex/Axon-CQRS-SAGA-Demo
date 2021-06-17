package com.appsdeveloperblog.estore.ProductService.cmd.RestController;

import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/management")
public class EventsReplayController {

    @Autowired
    private EventProcessingConfiguration eventProcessingConfiguration;

    @PostMapping(path = "/eventProcessor/{processorName}/reset")
    public ResponseEntity<String> replayEvents(@PathVariable("processorName") String processorName) {
        Optional<TrackingEventProcessor> trackingEventProcessor = eventProcessingConfiguration.eventProcessor(processorName, TrackingEventProcessor.class);

        if (trackingEventProcessor.isPresent()) {
            TrackingEventProcessor eventProcessor = trackingEventProcessor.get();
            eventProcessor.shutDown();
            eventProcessor.resetTokens();
            eventProcessor.start();
            return ResponseEntity.ok().body(String.format("The event processor with a name [%s] has been reset", processorName));
        } else {
            return ResponseEntity.badRequest().body(String.format("The event processor with a name [%s] is not a tracking processor, " +
                    " only tracking processor is supported", processorName));
        }
    }
}
