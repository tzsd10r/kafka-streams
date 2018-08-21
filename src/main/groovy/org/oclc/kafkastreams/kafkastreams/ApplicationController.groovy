package org.oclc.kafkastreams.kafkastreams

import org.apache.kafka.streams.state.KeyValueIterator
import org.apache.kafka.streams.state.QueryableStoreType
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.annotation.Resource
import javax.validation.Valid

@RestController
class ApplicationController {
    @Resource
    ApplicationControllerDelegate kafkaStreamsControllerDelegate

    @Resource
    QueryableStoreRegistry registry

    /**
     * http://localhost:8080/deliver/event
     * {
     *     "mode" : "ONLINE",
     *     "message" : "Hello World!",
     *     "duration" : "1000"
     * }
     * @param message
     */
    @PostMapping("/deliver/event")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<Acknowledgment> event(@Valid @RequestBody EventMessage event) {
        kafkaStreamsControllerDelegate.delegateToService(event)
    }

    /**
     * http://localhost:8080/deliver/request
     * {
     *     "message" : "Hello World!"
     * }
     * @param message
     */
    @PostMapping("/deliver/request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<Acknowledgment> request(@Valid @RequestBody final String payload) {
        kafkaStreamsControllerDelegate.delegateToService(kafkaStreamsControllerDelegate.build(payload, "ONLINE"))
    }

    /**
     * Queries for the counts.
     */
    @GetMapping("/count")
    Map<String, Long> counts() {
        def count = [:]

        registry.getQueryableStoreType(IChannelBinder.MATERIALIZED_COUNT, QueryableStoreTypes.keyValueStore()).all().each { keyValue ->
            count."${keyValue.key}" = keyValue.value
        }

        count
    }
}
