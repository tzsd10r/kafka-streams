package org.oclc.kafkastreams.kafkastreams


import org.springframework.stereotype.Component

@Component
interface IProcessingService {
    Boolean send(EventMessage event)
}