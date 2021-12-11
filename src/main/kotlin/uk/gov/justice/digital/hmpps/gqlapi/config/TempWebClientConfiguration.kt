package uk.gov.justice.digital.hmpps.gqlapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

@Configuration
class TempWebClientConfiguration(
  @Value("\${api.base.url.prison-api}") val prisonApiBaseUri: String
) {

  @Bean
  fun prisonApiWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(prisonApiBaseUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }
}

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
