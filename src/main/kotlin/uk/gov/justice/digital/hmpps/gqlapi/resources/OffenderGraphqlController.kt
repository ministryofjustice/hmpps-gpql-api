package uk.gov.justice.digital.hmpps.gqlapi.resources

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderSentenceOffencesService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderSentenceService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderService

@Controller
class OffenderGraphqlController(
  private val offenderService: OffenderService,
  private val offenderSentenceService: OffenderSentenceService,
  private val offenderSentenceOffencesService: OffenderSentenceOffencesService
) {

  @QueryMapping
  fun offenders(): Flux<Offender> = offenderService.findAll()

  @QueryMapping
  fun offendersByLastName(@Argument lastName: String): Flux<Offender> = offenderService.findByLastName(lastName)

  @QueryMapping
  fun offenderById(@Argument id: String): Mono<Offender> = offenderService.findById(id)

  @SchemaMapping
  fun sentences(offender: Offender): Flux<Sentence> = offenderSentenceService.findByOffender(offender)

  @SchemaMapping
  fun offences(sentence: Sentence): Flux<Offence> = offenderSentenceOffencesService.findBySentence(sentence)
}
