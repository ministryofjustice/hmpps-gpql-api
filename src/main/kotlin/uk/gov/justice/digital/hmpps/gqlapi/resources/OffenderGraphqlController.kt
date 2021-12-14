package uk.gov.justice.digital.hmpps.gqlapi.resources

import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.config.withToken
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.OffenderManager
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderManagerService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderSentenceOffencesService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderSentenceService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderService

@Suppress("unused")
@Controller
@PreAuthorize("hasRole('ROLE_GRAPHQL_QUERY')")
class OffenderGraphqlController(
  private val offenderService: OffenderService,
  private val offenderSentenceService: OffenderSentenceService,
  private val offenderSentenceOffencesService: OffenderSentenceOffencesService,
  private val offenderManagerService: OffenderManagerService,
) {

  @QueryMapping
  fun offendersByLastName(@Argument lastName: String, env: DataFetchingEnvironment): Flux<Offender> =
    offenderService.findByLastName(lastName).withToken(env)

  @QueryMapping
  fun offenderById(@Argument id: String, env: DataFetchingEnvironment): Mono<Offender> =
    offenderService.findById(id).withToken(env)

  @SchemaMapping
  fun sentences(offender: Offender, env: DataFetchingEnvironment): Flux<Sentence> =
    offenderSentenceService.findByOffender(offender).withToken(env)

  @SchemaMapping
  fun offences(sentence: Sentence, env: DataFetchingEnvironment): Flux<Offence> =
    offenderSentenceOffencesService.findBySentence(sentence).withToken(env)

  @SchemaMapping
  fun offenderManagers(offender: Offender, env: DataFetchingEnvironment): Flux<OffenderManager> =
    offenderManagerService.findByOffender(offender).withToken(env)
}
