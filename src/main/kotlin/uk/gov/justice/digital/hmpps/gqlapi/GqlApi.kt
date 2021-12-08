package uk.gov.justice.digital.hmpps.gqlapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class GqlApi

fun main(args: Array<String>) {
  runApplication<GqlApi>(*args)
}
