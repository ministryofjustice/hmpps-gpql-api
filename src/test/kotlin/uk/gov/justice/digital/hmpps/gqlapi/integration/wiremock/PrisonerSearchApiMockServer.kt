package uk.gov.justice.digital.hmpps.gqlapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.post

class PrisonerSearchApiMockServer : WireMockServer(WIREMOCK_PORT) {
  companion object {
    private const val WIREMOCK_PORT = 8094
  }

  fun stubHealthPing(status: Int) {
    stubFor(
      get("/health/ping").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(if (status == 200) "pong" else "some error")
          .withStatus(status)
      )
    )
  }

  fun stubNoOffendersFound() {
    stubFor(
      post("/global-search").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(
            """
              {
                "totalElements": 0,
                "totalPages": 0,
                "size": 0,
                "numberOfElements": 0,
                "content": []
              }
            """.trimIndent()
          )
          .withStatus(200)
      )
    )
  }

  fun stubSingleOffender(prisonerNumber: String) {
    stubFor(
      post("/global-search").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(
            """
              {                
                "totalElements": 1,
                "totalPages": 1,
                "size": 1,
                "numberOfElements": 1,
                "content": [
                    {
                      "prisonerNumber": "$prisonerNumber",
                      "firstName": "John",
                      "lastName": "Smith",
                      "dateOfBirth": "1980-01-01"
                    }
               ]
           }
            """.trimIndent()
          )
      )
    )
  }
}
