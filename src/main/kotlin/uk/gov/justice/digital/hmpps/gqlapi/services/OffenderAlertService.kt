package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.data.Alert
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender

@Service
class OffenderAlertService(private val prisonApiService: PrisonApiService) {
  fun findByOffenders(offenders: Set<Offender>): Mono<Map<Offender, List<Alert>>> =
    prisonApiService.findAlertsByPrisonNumbers(offenders.map { it.id }).map {
      Alert(
        offenderId = it.offenderNo,
        id = "${it.alertId}",
        type = it.alertTypeDescription,
        category = it.alertCodeDescription,
        date = it.dateCreated,
      )
    }.collectMultimap { alert ->
      offenders.find { offender -> offender.id == alert.offenderId }!! // must exist
    }.map {
      it.entries.associate { (offender, alerts) ->
        // convert to List since we need a List not a collection
        offender to alerts.toList()
      }
    }.map {
      // some offenders have no alerts so convert to empty list if not present else we will get a null list
      it + addEmptyAlerts(it, offenders)
    }

  private fun addEmptyAlerts(
    alertsMap: Map<Offender, List<Alert>>,
    offenders: Set<Offender>
  ): Map<Offender, List<Alert>> = offenders.subtract(alertsMap.keys).associateWith {
    emptyList()
  }
}
