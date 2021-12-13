package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence

@Service
class OffenderSentenceService(private val prisonApiService: PrisonApiService) {
  fun findByOffender(offender: Offender): Flux<Sentence> =
    prisonApiService.findSentencesByPrisonNumber(offender.id).flatMapIterable {
      it.prisonTerms.flatMap { prisonTerm ->
        prisonTerm.courtSentences.flatMap { courtSentence ->
          courtSentence.sentences.map { sentence ->
            Sentence(
              id = "${courtSentence.id}-${sentence.sentenceSequence}",
              description = sentence.sentenceTypeDescription,
              length = sentence.length,
              startDate = sentence.sentenceStartDate,
              offences = sentence.offences.map { offence ->
                Offence(
                  id = offence.offenceCode,
                  description = offence.offenceDescription
                )
              }
            )
          }
        }
      }
    }
}
