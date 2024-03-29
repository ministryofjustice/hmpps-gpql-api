package uk.gov.justice.digital.hmpps.gqlapi.config

import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context

class AuthTokenFilterFunction : ExchangeFilterFunction {

  override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
    return AuthTokenResponseMono(request, next)
  }
}

class AuthTokenResponseMono(
  private val request: ClientRequest,
  private val next: ExchangeFunction
) : Mono<ClientResponse>() {

  override fun subscribe(subscriber: CoreSubscriber<in ClientResponse>) {
    val context = subscriber.currentContext()
    val requestBuilder = ClientRequest.from(request)
    requestBuilder.header(HttpHeaders.AUTHORIZATION, context.get(HttpHeaders.AUTHORIZATION))
    val mutatedRequest = requestBuilder.build()
    next.exchange(mutatedRequest).subscribe(subscriber)
  }
}

@Configuration
class AuthTokenExtractor : WebGraphQlInterceptor {

  override fun intercept(webInput: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
    webInput.configureExecutionInput { _, builder ->
      builder.graphQLContext {
        it.put(
          HttpHeaders.AUTHORIZATION,
          webInput.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull() ?: "anonymous"
        )
      }.build()
    }
    return chain.next(webInput)
  }
}

fun <T> Mono<T>.withToken(env: DataFetchingEnvironment): Mono<T> =
  this.contextWrite { transferAuthHeader(env, it) }

fun <T> Flux<T>.withToken(env: DataFetchingEnvironment): Flux<T> =
  this.contextWrite { transferAuthHeader(env, it) }

fun <T> Mono<T>.withToken(graphQLContext: GraphQLContext): Mono<T> =
  this.contextWrite { transferAuthHeader(graphQLContext, it) }

private fun transferAuthHeader(env: DataFetchingEnvironment, context: Context) =
  context.put(HttpHeaders.AUTHORIZATION, env.graphQlContext.get<String>(HttpHeaders.AUTHORIZATION))

private fun transferAuthHeader(graphQLContext: GraphQLContext, context: Context) =
  context.put(HttpHeaders.AUTHORIZATION, graphQLContext.get<String>(HttpHeaders.AUTHORIZATION))
