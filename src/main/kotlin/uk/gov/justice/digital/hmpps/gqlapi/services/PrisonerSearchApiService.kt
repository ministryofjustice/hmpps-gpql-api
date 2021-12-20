package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class PrisonerSearchApiService(private val prisonerSearchWebClient: WebClient) {

  fun getOffenderById(id: String): Mono<Prisoner> {
    return prisonerSearchWebClient.get()
      .uri("/prisoner/$id")
      .retrieve()
      .bodyToMono(Prisoner::class.java)
  }

  fun findOffendersByLastName(lastName: String): Mono<SearchResult> {
    return prisonerSearchWebClient.post()
      .uri("/global-search")
      .bodyValue(PrisonerOffenderSearch(lastName = lastName))
      .retrieve()
      .bodyToMono(SearchResult::class.java)
  }
}

data class PrisonerOffenderSearch(val lastName: String)

data class SearchResult(
  val content: List<Prisoner>
)

data class Prisoner(
  val prisonerNumber: String,
  val firstName: String,
  val lastName: String,
  val dateOfBirth: LocalDate,
)
