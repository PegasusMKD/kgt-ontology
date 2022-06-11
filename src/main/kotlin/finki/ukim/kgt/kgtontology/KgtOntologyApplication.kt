package finki.ukim.kgt.kgtontology

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class KgtOntologyApplication

fun main(args: Array<String>) {
    runApplication<KgtOntologyApplication>(*args)
}
