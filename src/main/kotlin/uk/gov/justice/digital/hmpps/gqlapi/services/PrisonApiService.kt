package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class PrisonApiService(private val prisonWebClient: WebClient) {

  fun getOffenderById(id: String): Mono<InmateDetail> {
    return prisonWebClient.get()
      .uri("/api/offenders/$id")
      .retrieve()
      .bodyToMono(InmateDetail::class.java)
  }

  fun findOffendersByLastName(lastName: String): Flux<PrisonerDetail> {
    return prisonWebClient.post()
      .uri("/api/prisoners")
      .bodyValue(PrisonOffenderSearch(lastName = lastName))
      .retrieve()
      .bodyToFlux(PrisonerDetail::class.java)
  }
}

data class PrisonOffenderSearch(val lastName: String)

data class InmateDetail(
  val offenderNo: String,
  val firstName: String,
  val lastName: String,
  val dateOfBirth: LocalDate,
)

data class PrisonerDetail(
  val offenderNo: String,
  val firstName: String,
  val lastName: String,
  val dateOfBirth: LocalDate,
)
