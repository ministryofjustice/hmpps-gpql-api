package uk.gov.justice.digital.hmpps.gqlapi.services

import org.slf4j.Logger
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence
import kotlin.random.Random

@Service
class OffenderSentenceOffencesService {
  companion object {
    val OFFENCES = listOf(
      "Attempt murder - victim aged 1 year or over",
      "Possess a firearm with intent to endanger life / enable an other to do so",
      "Section 18 - grievous bodily harm with intent",
      "Assault occasioning actual bodily harm",
      "Cause a computer to perform function to secure / enable unauthorised access to a program / data",
      "Doing act tending and intended to pervert course of public justice",
      "Damage / destroy property with intent to endanger life",
      "Violent disorder",
    )
    val log: Logger = org.slf4j.LoggerFactory.getLogger(OffenderSentenceOffencesService::class.java)
  }

  fun findBySentence(sentence: Sentence): Flux<Offence> {
    log.info("Find offences for sentence {}", sentence.id)
    return Flux.fromIterable(1..Random.nextInt(1, 4))
      .map {
        Offence(
          id = "${sentence.id}$it",
          description = randomDescription(),
          sentenceId = sentence.id,
        )
      }
  }

  private fun randomDescription(): String = OFFENCES.random()
}
