package org.oclc.kafkastreams.kafkastreams

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.Canonical
import groovy.transform.builder.Builder

@Builder
@Canonical
@JsonIgnoreProperties(ignoreUnknown = true)
class EventMessage {
    String id
    Long timestamp

    String mode
    String message
    Long duration
}
