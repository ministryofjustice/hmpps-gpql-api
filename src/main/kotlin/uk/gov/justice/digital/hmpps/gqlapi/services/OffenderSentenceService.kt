package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence

@Service
class OffenderSentenceService(
  private val prisonApiService: PrisonApiService,
  private val communityApiService: CommunityApiService
) {
  fun findByOffender(offender: Offender): Flux<Sentence> = Flux.concat(
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
    },
    communityApiService.findConvictionsByNOMSNumber(offender.id).filter { it.isCommunitySentence() }
      .map {
        Sentence(
          id = "${it.convictionId}",
          description = it.sentence!!.description,
          length = it.sentence.length,
          startDate = it.sentence.startDate,
          offences = it.offences.map { offence ->
            Offence(
              id = offence.detail.code,
              description = offence.detail.description,
            )
          }
        )
      },
  ).sort(::compareSentences)
}

private fun Conviction.isCommunitySentence(): Boolean {
  return this.custody == null && this.sentence != null
}

fun compareSentences(a: Sentence, b: Sentence): Int {
  return b.startDate.compareTo(a.startDate)
}
