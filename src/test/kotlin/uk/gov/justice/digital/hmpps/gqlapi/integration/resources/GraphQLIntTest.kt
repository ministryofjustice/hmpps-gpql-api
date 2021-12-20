package uk.gov.justice.digital.hmpps.gqlapi.integration.resources

import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import uk.gov.justice.digital.hmpps.gqlapi.integration.IntegrationTestBase

class GraphQLIntTest : IntegrationTestBase() {
  @BeforeEach
  internal fun setUp() {
    communityApiMockServer.resetAll()
    prisonApiMockServer.resetAll()
    prisonerSearchApiMockServer.resetAll()
    prisonerSearchApiMockServer.stubNoOffendersFound()
  }

  @Test
  internal fun `requires a valid token to retrieve data`() {
    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .bodyValue(simplyQuery())
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().jsonPath("$.errors[0].message").isEqualTo("Unauthorized")
  }

  @Test
  internal fun `requires the graphql role to retrieve data`() {
    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_BANANAS")))
      .bodyValue(simplyQuery())
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().jsonPath("$.errors[0].message").isEqualTo("Forbidden")

    prisonerSearchApiMockServer
      .verify(
        0,
        postRequestedFor(urlEqualTo("/global-search"))
      )
  }

  @Test
  internal fun `can introspect the GraphQL schema without a token`() {
    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .bodyValue(
        Query(
          """{
            __schema {
              types {
                name
              }
            }
          }"""
        )
      )
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.errors").doesNotExist()
      .jsonPath("$.data.__schema").exists()
  }

  @Test
  internal fun `can access data with the correct role`() {
    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_GRAPHQL_QUERY")))
      .bodyValue(simplyQuery())
      .exchange()
      .expectStatus()
      .isOk

    prisonerSearchApiMockServer
      .verify(
        postRequestedFor(urlEqualTo("/global-search"))
          .withRequestBody(containing("""{"lastName":"kane"}"""))
      )
  }

  @Test
  internal fun `will call services when query demands it`() {
    prisonerSearchApiMockServer.stubSingleOffender("A5194DY")
    prisonApiMockServer.stubSingleSentence("A5194DY")
    communityApiMockServer.stubConvictions("A5194DY")
    communityApiMockServer.stubOffenderManagers("A5194DY")

    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_GRAPHQL_QUERY")))
      .bodyValue(
        Query(
          """
        {
          offendersByLastName(lastName: "kane") {
            id
            firstName
            lastName
            dateOfBirth
            offenderManagers {
              firstName
              lastName
              responsibleOfficer
              type
            }
            sentences {
              length
              description
              offences {
                description
                id
              }
            }
          }
        }
          """.trimIndent()
        )
      )
      .exchange()
      .expectStatus()
      .isOk

    prisonerSearchApiMockServer.verify(postRequestedFor(urlEqualTo("/global-search")))
    prisonApiMockServer.verify(getRequestedFor(urlEqualTo("/api/offenders/A5194DY/booking/latest/sentence-summary")))
    communityApiMockServer.verify(getRequestedFor(urlEqualTo("/secure/offenders/nomsNumber/A5194DY/convictions")))
    communityApiMockServer.verify(getRequestedFor(urlEqualTo("/secure/offenders/nomsNumber/A5194DY/allOffenderManagers")))
  }
  @Test
  internal fun `will not call services when query does not demand it`() {
    prisonerSearchApiMockServer.stubSingleOffender("A5194DY")
    prisonApiMockServer.stubSingleSentence("A5194DY")
    communityApiMockServer.stubConvictions("A5194DY")
    communityApiMockServer.stubOffenderManagers("A5194DY")

    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_GRAPHQL_QUERY")))
      .bodyValue(
        Query(
          """
        {
          offendersByLastName(lastName: "kane") {
            id
            firstName
            lastName
            dateOfBirth
          }
        }
          """.trimIndent()
        )
      )
      .exchange()
      .expectStatus()
      .isOk

    prisonerSearchApiMockServer.verify(postRequestedFor(urlEqualTo("/global-search")))
    prisonApiMockServer.verify(0, getRequestedFor(urlEqualTo("/api/offenders/A5194DY/booking/latest/sentence-summary")))
    communityApiMockServer.verify(0, getRequestedFor(urlEqualTo("/secure/offenders/nomsNumber/A5194DY/convictions")))
    communityApiMockServer.verify(0, getRequestedFor(urlEqualTo("/secure/offenders/nomsNumber/A5194DY/allOffenderManagers")))
  }

  @Test
  internal fun `will return JSON data combing all queries`() {
    prisonerSearchApiMockServer.stubSingleOffender("A5194DY")
    prisonApiMockServer.stubSingleSentence("A5194DY")
    communityApiMockServer.stubConvictions("A5194DY")
    communityApiMockServer.stubOffenderManagers("A5194DY")

    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_GRAPHQL_QUERY")))
      .bodyValue(
        Query(
          """
        {
          offendersByLastName(lastName: "kane") {
            id
            firstName
            lastName
            dateOfBirth
            offenderManagers {
              firstName
              lastName
              responsibleOfficer
              type
            }
            sentences {
              length
              description
              offences {
                description
                id
              }
            }
          }
        }
          """.trimIndent()
        )
      )
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().json(
        """
{
  "data": {
    "offendersByLastName": [
      {
        "id": "A5194DY",
        "firstName": "John",
        "lastName": "Smith",
        "dateOfBirth": "1980-01-01",
        "offenderManagers": [
          {
            "firstName": "Gurnank",
            "lastName": "Cheema",
            "responsibleOfficer": true,
            "type": "COMMUNITY"
          },
          {
            "firstName": "Sheila",
            "lastName": "Hancock",
            "responsibleOfficer": false,
            "type": "PRISON"
          }
        ],
        "sentences": [
          {
            "length": "2 Years",
            "description": "ORA Sentencing Code Standard Determinate Sentence",
            "offences": [
              {
                "description": "Access / exit by unofficial route - railway bye-law",
                "id": "RL05016"
              }
            ]
          },
          {
            "length": "12 Months",
            "description": "CJA - Community Order",
            "offences": [
              {
                "description": "Arson - 05600",
                "id": "05600"
              }
            ]
          }
        ]
      }
    ]
  }
}
        """.trimIndent()
      )
  }
  @Test
  internal fun `will return JSON data combing ony required queries`() {
    prisonerSearchApiMockServer.stubSingleOffender("A5194DY")

    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_GRAPHQL_QUERY")))
      .bodyValue(
        Query(
          """
        {
          offendersByLastName(lastName: "kane") {
            id
            firstName
            lastName
            dateOfBirth
          }
        }
          """.trimIndent()
        )
      )
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().json(
        """
{
  "data": {
    "offendersByLastName": [
      {
        "id": "A5194DY",
        "firstName": "John",
        "lastName": "Smith",
        "dateOfBirth": "1980-01-01"
      }
    ]
  }
}
        """.trimIndent()
      )
  }

  @Test
  internal fun `will not error when some down stream services find no data`() {
    prisonerSearchApiMockServer.stubSingleOffender("A5194DY")
    prisonApiMockServer.stubSingleSentence("A5194DY")
    communityApiMockServer.stubOffenderNotFound("A5194DY")

    webTestClient.post()
      .uri("/graphql")
      .contentType(APPLICATION_JSON)
      .headers(setAuthorisation(roles = listOf("ROLE_GRAPHQL_QUERY")))
      .bodyValue(
        Query(
          """
        {
          offendersByLastName(lastName: "kane") {
            id
            firstName
            lastName
            dateOfBirth
            offenderManagers {
              firstName
              lastName
              responsibleOfficer
              type
            }
            sentences {
              length
              description
              offences {
                description
                id
              }
            }
          }
        }
          """.trimIndent()
        )
      )
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().json(
        """
{
  "data": {
    "offendersByLastName": [
      {
        "id": "A5194DY",
        "firstName": "John",
        "lastName": "Smith",
        "dateOfBirth": "1980-01-01",
        "offenderManagers": [],
        "sentences": [
          {
            "length": "2 Years",
            "description": "ORA Sentencing Code Standard Determinate Sentence",
            "offences": [
              {
                "description": "Access / exit by unofficial route - railway bye-law",
                "id": "RL05016"
              }
            ]
          }
        ]
      }
    ]
  }
}
        """.trimIndent()
      )
  }

  private fun simplyQuery() = Query(
    query = """
          {
            offendersByLastName(lastName: "kane") {
              id,
              firstName,
              lastName,
            }
          }
    """.trimIndent()
  )
}

data class Query(val query: String)
