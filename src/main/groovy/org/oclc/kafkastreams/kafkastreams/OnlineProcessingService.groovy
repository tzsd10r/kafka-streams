package org.oclc.kafkastreams.kafkastreams

import groovy.util.logging.Slf4j
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.util.MimeTypeUtils

@EnableBinding(IChannelBinder.class)
@Component
@Slf4j
class OnlineProcessingService implements IProcessingService {
    MessageChannel messageChannel

    /**
     * This constructor is being autowired... the "outputIntoKafka" bean is being injected as constructor argument.
     *
     * @param outputIntoKafka
     */
    OnlineProcessingService(IChannelBinder outputIntoKafka) {
        messageChannel = outputIntoKafka.sendMessageToKafka()
    }

    @Override
    Boolean send(EventMessage event) {
        Boolean ack = false

        try {
            Message message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.id.getBytes())
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .build()

            messageChannel.send(message)

            log.info("Message Sent... {}", message)
            ack = true
        } catch (all) {
            log.error("Error... ", all)
        }

        ack
    }
}