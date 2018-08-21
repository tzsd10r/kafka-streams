package org.oclc.kafkastreams.kafkastreams

import groovy.util.logging.Slf4j
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Component

/**
 * Although it appears that this class isn't being used... in reality, the annotations provide the behind the scenes
 * implementation for which these methods are listening for
 */
@Component
@Slf4j
class KafkaStreamsListener {
    @StreamListener
    @SendTo(IChannelBinder.OUTPUT_COUNT_INTO_KAFKA)
    KStream<String, Long> process(@Input(IChannelBinder.INPUT_FROM_KAFKA_KSTREAM) KStream<String, EventMessage> events) {
//        events.foreach({
//            key, value -> println "${value.message}"
//        })
//        KTable kTable = events.filter({ key, value -> value.duration > 10 })
        events.filter({ key, value -> value.duration > 10 })
                .map({ key, value -> new KeyValue<>(value.message, "0") })
                .groupByKey()
                .count(Materialized.as(IChannelBinder.MATERIALIZED_COUNT))
                .toStream()
    }

    @StreamListener
    void process1(@Input(IChannelBinder.INPUT_COUNT_FROM_KAFKA) KTable<String, Long> counts) {
        counts.toStream().foreach({
            key, value -> println "${key}=${value}"
        })
    }
}
