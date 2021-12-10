package uk.gov.justice.digital.hmpps.gqlapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.gqlapi.utils.UserContext

@Configuration
class WebClientConfiguration(@Value("\${api.base.url.oauth}") val authBaseUri: String,
                             @Value("\${community.endpoint.url}") private val communityRootUri: String,
                             @Value("\${prison.endpoint.url}") private val prisonRootUri: String) {

  @Bean
  fun authWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(authBaseUri)
      .filter(addAuthHeaderFilterFunction())
      .build()
  }

  @Bean
  fun prisonWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(prisonRootUri)
      .filter(addAuthHeaderFilterFunction())
      .build()
  }

  @Bean
  fun probationWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(communityRootUri)
      .filter(addAuthHeaderFilterFunction())
      .build()
  }

  private fun addAuthHeaderFilterFunction(): ExchangeFilterFunction {
    return ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
      val filtered = ClientRequest.from(request)
        .header(HttpHeaders.AUTHORIZATION, UserContext.getAuthToken())
        .build()
      next.exchange(filtered)
    }
  }


}
