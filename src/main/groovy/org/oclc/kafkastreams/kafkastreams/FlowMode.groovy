package org.oclc.kafkastreams.kafkastreams


import groovy.transform.Canonical
import groovy.transform.builder.Builder
import groovy.util.logging.Slf4j

@Canonical
@Builder
@Slf4j
class FlowMode {
    ModeType modeType

    // The constructor is needed for convertion from json into object to occur successfully
    FlowMode(String modeType) {
        this.modeType = ModeType.instance(modeType)
    }

    enum ModeType {
        ONLINE("ONLINE"),
        BATCH("BATCH")

        String name

        ModeType(String name) {
            this.name = name
        }

        static ModeType instance(String name) {
            ModeType modeType

            try {
                modeType = valueOf(name.toUpperCase())
            } catch (any) {
                log.info("Error converting value [{}] to a valid ModeType... setting to 'ONLINE'. Exception Message: {}", name, any.getMessage() )
                modeType = ONLINE
            }

            modeType
        }
    }
}
