package uk.gov.justice.digital.hmpps.gqlapi.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.gqlapi.data.Offender
import java.time.LocalDate
import kotlin.random.Random

@Service
class OffenderService {
  companion object {
    val OFFENDERS = listOf(
      Offender(
        id = "1",
        firstName = "John",
        lastName = "Smith",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "2",
        firstName = "Jane",
        lastName = "Doe",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "3",
        firstName = "Joe",
        lastName = "Bloggs",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "4",
        firstName = "John",
        lastName = "Smith",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "5",
        firstName = "Jane",
        lastName = "Doe",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "6",
        firstName = "Joe",
        lastName = "Bloggs",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "7",
        firstName = "John",
        lastName = "Smith",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "8",
        firstName = "Jane",
        lastName = "Doe",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
      Offender(
        id = "9",
        firstName = "Alice",
        lastName = "Bloggs",
        dateOfBirth = LocalDate.now().minusYears(Random.nextLong(18, 65)),
      ),
    )
  }

  fun findAll(): Flux<Offender> {
    return Flux.fromIterable(OFFENDERS)
  }

  fun findByLastName(lastName: String): Flux<Offender> =
    Flux.fromIterable(OFFENDERS).filter { it.lastName.lowercase() == lastName.lowercase() }

  fun findById(id: String): Mono<Offender> =
    Mono.justOrEmpty(OFFENDERS.find { it.id == id })
}
