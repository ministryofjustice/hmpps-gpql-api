package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class CommunityApiService(private val communityWebClient: WebClient) {
  fun findOffenderManagersByNOMSNumber(nomsNumber: String): Flux<CommunityOrPrisonOffenderManager> {
    return communityWebClient.get()
      .uri("/secure/offenders/nomsNumber/$nomsNumber/allOffenderManagers")
      .retrieve()
      .bodyToFlux(CommunityOrPrisonOffenderManager::class.java)
      .onErrorResume(WebClientResponseException::class.java) { emptyWhenNotFound(it) }
  }

  fun findConvictionsByNOMSNumber(nomsNumber: String): Flux<Conviction> {
    return communityWebClient.get()
      .uri("/secure/offenders/nomsNumber/$nomsNumber/convictions")
      .retrieve()
      .bodyToFlux(Conviction::class.java)
      .onErrorResume(WebClientResponseException::class.java) { emptyWhenNotFound(it) }
  }
  fun <T> emptyWhenNotFound(exception: WebClientResponseException): Mono<T> = emptyWhen(exception, NOT_FOUND)
  fun <T> emptyWhen(exception: WebClientResponseException, statusCode: HttpStatus): Mono<T> =
    if (exception.rawStatusCode == statusCode.value()) Mono.empty() else Mono.error(exception)
}

data class CommunityOrPrisonOffenderManager(
  val isPrisonOffenderManager: Boolean,
  val isResponsibleOfficer: Boolean,
  val staff: Staff,
  val staffId: Long,
)

data class Staff(
  val forenames: String,
  val surname: String,
)

data class Conviction(
  val convictionId: Long,
  val offences: List<Offence>,
  val sentence: Sentence?,
  val custody: Custody?,
)

data class Sentence(
  val startDate: LocalDate,
  val description: String,
  val originalLength: Int,
  val originalLengthUnits: String,
) {
  val length: String
    get() = "$originalLength $originalLengthUnits"
}

data class Offence(
  val detail: OffenceDetail,
)

data class OffenceDetail(
  val code: String,
  val description: String,
)

data class Custody(val bookingNumber: String)
