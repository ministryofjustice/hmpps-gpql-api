package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.OMType
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.OffenderManager
import kotlin.random.Random

@Service
class OffenderManagerService {
  fun findByOffender(offender: Offender): Flux<OffenderManager> =
    Flux.fromIterable(1..Random.nextInt(1, 3))
      .map {
        OffenderManager(
          id = "${offender.id}$it",
          firstName = "Gunder",
          lastName = "Jordan",
          responsibleOffice = it == 1,
          type = if (it == 1) {
            OMType.COMMUNITY
          } else {
            OMType.PRISON
          },
        )
      }
}
