package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.gqlapi.data.OMType
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import uk.gov.justice.digital.hmpps.gqlapi.data.OffenderManager

@Service
class OffenderManagerService(private val communityApiService: CommunityApiService) {
  fun findByOffender(offender: Offender): Flux<OffenderManager> =
    communityApiService.findOffenderManagersByNOMSNumber(offender.id).map {
      OffenderManager(
        id = "${it.staffId}",
        firstName = it.staff.forenames,
        lastName = it.staff.surname,
        type = if (it.isPrisonOffenderManager) {
          OMType.PRISON
        } else {
          OMType.COMMUNITY
        },
        responsibleOffice = it.isResponsibleOfficer
      )
    }
}
