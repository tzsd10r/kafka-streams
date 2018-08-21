package org.oclc.kafkastreams.kafkastreams

import groovy.transform.Canonical
import groovy.transform.builder.Builder

@Canonical
@Builder
class Acknowledgment {
    Long timestamp
    String ackMessage
}
