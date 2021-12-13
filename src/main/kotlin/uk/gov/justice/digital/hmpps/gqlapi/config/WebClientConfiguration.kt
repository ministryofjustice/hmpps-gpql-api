package uk.gov.justice.digital.hmpps.gqlapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration(
  @Value("\${api.base.url.oauth}") val authBaseUri: String,
  @Value("\${api.base.url.community}") private val communityRootUri: String,
  @Value("\${api.base.url.prison}") private val prisonRootUri: String
) {

  @Bean
  fun authWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(authBaseUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }

  @Bean
  fun prisonWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(prisonRootUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }

  @Bean
  fun communityWebClient(builder: WebClient.Builder): WebClient {
    return builder
      .baseUrl(communityRootUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }
}
