package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender

@Service
class OffenderService(private val prisonerSearchApiService: PrisonerSearchApiService) {
  fun findByLastName(lastName: String): Flux<Offender> =
    prisonerSearchApiService.findOffendersByLastName(lastName).flatMapIterable {
      it.content.map { result ->
        Offender(
          id = result.prisonerNumber,
          firstName = result.firstName,
          lastName = result.lastName,
          dateOfBirth = result.dateOfBirth,
        )
      }
    }

  fun findById(id: String): Mono<Offender> = prisonerSearchApiService.getOffenderById(id).map {
    Offender(
      id = it.prisonerNumber,
      firstName = it.firstName,
      lastName = it.lastName,
      dateOfBirth = it.dateOfBirth,
    )
  }
}
