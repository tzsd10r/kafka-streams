package org.oclc.kafkastreams.kafkastreams


import groovy.util.logging.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import javax.annotation.Resource

/**
 * Delegates to a factory to determine which service to use.
 */
@Component
@Slf4j
class ApplicationControllerDelegate {
    @Resource
    KafkaStreamsServiceFactory kafkaStreamsServiceFactory

    def delegateToService(EventMessage event) {
        IProcessingService service = kafkaStreamsServiceFactory.whichServiceShouldHandleThisEvent(event)

        event.timestamp = System.currentTimeMillis()
        event.id = UUID.randomUUID().toString()

        Boolean ack = service.send(event)

        Acknowledgment acknowledgment = Acknowledgment.builder()
                .timestamp(System.currentTimeMillis())
                .ackMessage("Success")
                .build()

        if (!ack) {
            acknowledgment.ackMessage = "Failure"
        }

        ResponseEntity.ok(acknowledgment)
    }

    def build(String payload, String modeType) {
        EventMessage.builder()
                .mode(modeType)
                .message(payload)
                .timestamp(System.currentTimeMillis())
                .id(UUID.randomUUID().toString())
                .build()
    }

    @Component
    @Slf4j
    class KafkaStreamsServiceFactory {
        @Resource
        IProcessingService kafkaStreamsOnlineService

        @Resource
        IProcessingService kafkaStreamsBatchService

        IProcessingService whichServiceShouldHandleThisEvent(EventMessage event) {
            if (event.mode == FlowMode.ModeType.ONLINE.name) {
                kafkaStreamsOnlineService
            } else {
                kafkaStreamsBatchService
            }
        }
    }
}
