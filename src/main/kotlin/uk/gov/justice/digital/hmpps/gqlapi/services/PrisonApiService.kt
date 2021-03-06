package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class PrisonApiService(private val prisonWebClient: WebClient) {

  fun findSentencesByPrisonNumber(prisonNumber: String): Mono<SentenceSummary> {
    return prisonWebClient.get()
      .uri("/api/offenders/$prisonNumber/booking/latest/sentence-summary")
      .retrieve()
      .bodyToMono(SentenceSummary::class.java)
      .onErrorResume(WebClientResponseException::class.java) { emptyWhenNotFound(it) }
  }

  fun findAlertsByPrisonNumbers(prisonNumbers: List<String>): Flux<OffenderAlert> {
    return prisonWebClient.post()
      .uri("/api/bookings/offenderNo/alerts")
      .bodyValue(prisonNumbers)
      .retrieve()
      .bodyToFlux(OffenderAlert::class.java)
      .onErrorResume(WebClientResponseException::class.java) { emptyWhenNotFound(it) }
  }
}

data class SentenceSummary(
  val latestPrisonTerm: PrisonTerm,
  val previousPrisonTerms: List<PrisonTerm>,
) {
  val prisonTerms: List<PrisonTerm>
    get() = listOf(latestPrisonTerm) + previousPrisonTerms
}

data class PrisonTerm(
  val courtSentences: List<CourtSentences>,
)

data class CourtSentences(
  val id: String,
  val sentences: List<SentencesOffencesTerms>
)

data class SentencesOffencesTerms(
  val sentenceSequence: Int,
  val sentenceStartDate: LocalDate,
  val sentenceTypeDescription: String,
  val terms: List<Terms>,
  val offences: List<OffenderOffence>,
) {
  // hack = only look at first term to get the sentence length
  val length: String
    get() = terms.firstOrNull()?.length?.trim() ?: ""
}

data class Terms(
  val days: Int?,
  val weeks: Int?,
  val months: Int?,
  val years: Int?,
) {
  val length: String
    get() = "${toYears()}${toMonths()}${toWeeks()}${toDays()}"

  private fun toDays(): String {
    return days?.let { "$days Days " } ?: ""
  }

  private fun toWeeks(): String {
    return weeks?.let { "$weeks Weeks " } ?: ""
  }

  private fun toMonths(): String {
    return months?.let { "$months Months " } ?: ""
  }

  private fun toYears(): String {
    return years?.let { "$years Years " } ?: ""
  }
}

data class OffenderOffence(
  val offenceDescription: String,
  val offenceCode: String,
)

data class OffenderAlert(
  val alertId: Long,
  val offenderNo: String,
  val alertTypeDescription: String,
  val alertCodeDescription: String,
  val dateCreated: LocalDate,
)
