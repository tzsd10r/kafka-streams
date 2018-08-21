package org.oclc.kafkastreams.kafkastreams

import groovy.util.logging.Slf4j
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

/**
 * Listens for new messages and simply logs them.
 */
@Component
@Slf4j
class MessageListener {

    @StreamListener(IChannelBinder.INPUT_FROM_KAFKA)
    void process(@Payload EventMessage event) {
        log.info("Event Message Processed: {}", event.toString())
    }
}
