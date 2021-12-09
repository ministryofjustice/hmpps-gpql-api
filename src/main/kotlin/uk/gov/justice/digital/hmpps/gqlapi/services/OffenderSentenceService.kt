package uk.gov.justice.digital.hmpps.gqlapi.services

import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence
import java.time.LocalDate
import kotlin.random.Random

@Service
class OffenderSentenceService {
  companion object {
    val SENTENCE_DESCRIPTIONS = listOf(
      "ORA CJA03 Standard Determinate Sentence",
      "CJA03 Standard Determinate Sentence",
      "Indeterminate Sentence for the Public Protection"
    )

    val LENGTHS = listOf("Days", "Weeks", "Months", "Years")
    val log: Logger = org.slf4j.LoggerFactory.getLogger(OffenderSentenceService::class.java)
  }

  fun findByOffender(offender: Offender): Flux<Sentence> {
    log.info("Find sentences for offender {}", offender.id)
    return Flux.fromIterable(1..Random.nextInt(1, 4))
      .map {
        Sentence(
          id = "${offender.id}$it",
          description = randomDescription(),
          offenderId = offender.id,
          length = randomLength(),
          startDate = LocalDate.now().minusDays(Random.nextLong(999))
        )
      }
  }

  private fun randomLength(): String = "${LENGTHS.random()} ${Random.nextInt(1, 10)}"

  private fun randomDescription(): String = SENTENCE_DESCRIPTIONS.random()
}
