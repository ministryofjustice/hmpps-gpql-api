package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender

@Service
class OffenderService(private val prisonApiService: PrisonApiService) {
  fun findByLastName(lastName: String): Flux<Offender> =
    prisonApiService.findOffendersByLastName(lastName).map {
      Offender(
        id = it.offenderNo,
        firstName = it.firstName,
        lastName = it.lastName,
        dateOfBirth = it.dateOfBirth,
      )
    }

  fun findById(id: String): Mono<Offender> = prisonApiService.getOffenderById(id).map {
    Offender(
      id = it.offenderNo,
      firstName = it.firstName,
      lastName = it.lastName,
      dateOfBirth = it.dateOfBirth,
    )
  }
}
