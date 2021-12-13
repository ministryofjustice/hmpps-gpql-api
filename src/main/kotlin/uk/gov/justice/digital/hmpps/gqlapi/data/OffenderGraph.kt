package uk.gov.justice.digital.hmpps.gqlapi.data

import java.time.LocalDate

data class Offender(
  val id: String,
  val firstName: String,
  val lastName: String,
  val dateOfBirth: LocalDate,
)

data class Sentence(
  val id: String,
  val description: String,
  val length: String,
  val startDate: LocalDate,
  val offences: List<Offence>,
)

data class Offence(
  val id: String,
  val description: String,
)

data class OffenderManager(
  val id: String,
  val firstName: String,
  val lastName: String,
  val responsibleOffice: Boolean,
  val type: OMType,
)

enum class OMType {
  COMMUNITY,
  PRISON,
}
