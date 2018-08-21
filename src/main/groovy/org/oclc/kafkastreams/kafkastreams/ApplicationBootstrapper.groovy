package org.oclc.kafkastreams.kafkastreams

import groovy.util.logging.Slf4j
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import javax.annotation.Resource
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootApplication
@Slf4j
class ApplicationBootstrapper implements CommandLineRunner {
    @Resource
    ApplicationControllerDelegate kafkaStreamsControllerDelegate

    static void main(String[] args) {
        SpringApplication.run ApplicationBootstrapper, args
    }

    @Override
    void run(String... args) throws Exception {
        //String url = "http://localhost:8080/deliver/event"
        def names = ["Carlos", "Paulo", "Pedro", "Tobias", "Marcos", "Ricardo"]
        def pages = ["blog", "sitemap", "oclc", "google", "news", "fox"]
        def size = 6

        Runnable runnable = {
            String page = pages.get(new Random().nextInt(size))
            String name = names.get(new Random().nextInt(size))

            def duration = Math.random() > 0.5 ? 10 : 1000

            EventMessage event = EventMessage.builder().mode("ONLINE").message("${name}:${page}").duration(duration).build()

            kafkaStreamsControllerDelegate.delegateToService(event)
        }

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS)
    }
}
