package uk.gov.justice.digital.hmpps.gqlapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration(
  @Value("\${api.base.url.oauth}") val authBaseUri: String,
  @Value("\${api.base.url.community}") private val communityRootUri: String,
  @Value("\${api.base.url.prison}") private val prisonRootUri: String,
  @Value("\${api.base.url.prisonersearch}") private val prisonerSearchRootUri: String
) {

  @Bean
  fun authWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(authBaseUri)
      .build()
  }

  @Bean
  fun prisonerSearchWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(prisonerSearchRootUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }

  @Bean
  fun prisonWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(prisonRootUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }

  @Bean
  fun prisonerSearchHealthWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(prisonerSearchRootUri)
      .build()
  }

  @Bean
  fun prisonHealthWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(prisonRootUri)
      .build()
  }

  @Bean
  fun communityWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(communityRootUri)
      .filter(AuthTokenFilterFunction())
      .build()
  }

  @Bean
  fun communityHealthWebClient(): WebClient {
    return WebClient.builder()
      .baseUrl(communityRootUri)
      .build()
  }
}
