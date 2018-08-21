package org.oclc.kafkastreams.kafkastreams

import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel
import org.springframework.stereotype.Component

@Component
interface IChannelBinder {
    // Source... read events from Kafka
    // "inputFromKafkaKstream" is the name of the bean
    String INPUT_FROM_KAFKA_KSTREAM = "inputFromKafkaKstream"

    // Source... read events from Kafka
    // "inputFromKafka" is the name of the bean
    String INPUT_FROM_KAFKA = "inputFromKafka"

    // Sink... write events into Kafka
    // "outputIntoKafka" is the name of the bean
    String OUTPUT_INTO_KAFKA = "outputIntoKafka"

    // Sink... write events count into Kafka
    // "outputCountIntoKafka" is the name of the bean
    String OUTPUT_COUNT_INTO_KAFKA = "outputCountIntoKafka"

    // Source... read events count from Kafka
    // "inputCountFromKafka" is the name of the bean
    String INPUT_COUNT_FROM_KAFKA = "inputCountFromKafka"

    // Sink... write the count onto Kafka
    // "materializedCount" is the name of the bean
    String MATERIALIZED_COUNT = "materializedCount"






    // Define a binding that represents a channel sending events to kafka
    // This is an annotated method that creates the "outputIntoKafka" bean
    @Output(IChannelBinder.OUTPUT_INTO_KAFKA)
    MessageChannel sendMessageToKafka()

    // Define a binding that represents a channel reading events from kafka
    // This is an annotated method that creates the "inputFromKafka" bean
    @Input(IChannelBinder.INPUT_FROM_KAFKA)
    SubscribableChannel readMessageFromKafka();

    // Define a method that represents a channel reading events from kafka... using Kafka Streams
    // This is an annotated method that creates the "inputFromKafkaKstream" bean
    @Input(IChannelBinder.INPUT_FROM_KAFKA_KSTREAM)
    KStream<String, EventMessage> readMessageFromKafkaUsingKStream();

    // Define a method that represents a channel sending counts to kafka... using Kafka Streams
    // This is an annotated method that creates the "outputCountIntoKafka" bean
    @Output(IChannelBinder.OUTPUT_COUNT_INTO_KAFKA)
    KStream<String, Long> sendCountToKafka();

    // Define a method that represents a channel reading counts from kafka... using Kafka Streams
    // This is an annotated method that creates the "inputCountFromKafka" bean
    @Input(IChannelBinder.INPUT_COUNT_FROM_KAFKA)
    KTable<String, EventMessage> readCountFromKafka();

}