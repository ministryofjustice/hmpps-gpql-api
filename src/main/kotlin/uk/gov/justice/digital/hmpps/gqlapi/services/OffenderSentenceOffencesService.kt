package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence

@Service
class OffenderSentenceOffencesService {

  fun findBySentence(sentence: Sentence): Flux<Offence> {
    return Flux.fromIterable(sentence.offences)
  }
}
