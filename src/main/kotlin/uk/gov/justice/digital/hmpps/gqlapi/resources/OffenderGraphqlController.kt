package uk.gov.justice.digital.hmpps.gqlapi.resources

import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.graphql.execution.BatchLoaderRegistry
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.config.withToken
import uk.gov.justice.digital.hmpps.gqlapi.data.Alert
import uk.gov.justice.digital.hmpps.gqlapi.data.Offence
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.OffenderManager
import uk.gov.justice.digital.hmpps.gqlapi.data.Sentence
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderAlertService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderManagerService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderSentenceOffencesService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderSentenceService
import uk.gov.justice.digital.hmpps.gqlapi.services.OffenderService
import java.util.concurrent.CompletableFuture

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

@Controller
class AlertController(registry: BatchLoaderRegistry, private val offenderAlertService: OffenderAlertService) {
  init {
    registry.forName<Offender, List<Alert>>("alerts")
      .registerMappedBatchLoader { offenders, env ->
        offenderAlertService.findByOffenders(offenders)
          // each offender is associated with a context
          // but each context is exactly the same, so we just need any to
          // pass through token.
          //
          // Doesn't feel quite right so recommend this is looked at again
          // when using batching since feels like the context should be
          // associated with the query not an item in the query, but I suspect
          // this is because data loaders can also be used for caching
          .withToken(env.keyContexts[offenders.first()] as GraphQLContext)
      }
  }

  @SchemaMapping
  @PreAuthorize("hasRole('ROLE_GRAPHQL_QUERY')")
  fun alerts(
    offender: Offender,
    alerts: DataLoader<Offender, List<Alert>>,
    env: DataFetchingEnvironment
  ): CompletableFuture<List<Alert>> {
    return alerts.load(offender, env.graphQlContext)
  }

  // This is what we would like to do, but it is not clear how we get hold of the
  // GraphQLContext from the BatchLoaderEnvironment.
  // Maybe it is something missing from the new annotation 🤷🏽‍, or maybe this hold batch loading
  // doesn't play will with a query context (see the nastiness above)
  //
  // @BatchMapping
  // fun alerts(offenders: Set<Offender>, env: BatchLoaderEnvironment): Mono<Map<Offender, List<Alert>>> =
  //   offenderAlertService.findByOffenders(offenders).withToken(env)
}
