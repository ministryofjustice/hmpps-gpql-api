package uk.gov.justice.digital.hmpps.gqlapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get

class PrisonApiMockServer : WireMockServer(WIREMOCK_PORT) {
  companion object {
    private const val WIREMOCK_PORT = 8093
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

  fun stubSingleSentence(prisonerNumber: String) {
    stubFor(
      get("/api/offenders/$prisonerNumber/booking/latest/sentence-summary").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(
            """
            {
                "prisonerNumber": "$prisonerNumber",
                "latestPrisonTerm": {
                    "bookNumber": "38843A",
                    "bookingId": 1201725,
                    "courtSentences": [
                        {
                            "id": 1563734,
                            "caseSeq": 1,
                            "beginDate": "2021-12-13",
                            "court": {
                                "agencyId": "ABDRCT",
                                "description": "Aberdare County Court",
                                "longDescription": "Test desc",
                                "agencyType": "CRT",
                                "active": true,
                                "courtType": "CO"
                            },
                            "caseType": "Adult",
                            "caseStatus": "ACTIVE",
                            "sentences": [
                                {
                                    "sentenceSequence": 1,
                                    "sentenceStatus": "A",
                                    "sentenceCategory": "2020",
                                    "sentenceCalculationType": "ADIMP_ORA",
                                    "sentenceTypeDescription": "ORA Sentencing Code Standard Determinate Sentence",
                                    "sentenceStartDate": "2021-12-13",
                                    "sentenceEndDate": "2023-12-12",
                                    "lineSeq": 1,
                                    "offences": [
                                        {
                                            "offenderChargeId": 3932605,
                                            "offenceCode": "RL05016",
                                            "offenceDescription": "Access / exit by unofficial route - railway bye-law"
                                        }
                                    ],
                                    "terms": [
                                        {
                                            "termSequence": 1,
                                            "sentenceTermCode": "IMP",
                                            "startDate": "2021-12-13",
                                            "years": 2,
                                            "lifeSentence": false
                                        }
                                    ]
                                }
                            ],
                            "issuingCourt": {
                                "agencyId": "ABDRCT",
                                "description": "Aberdare County Court",
                                "longDescription": "Test desc",
                                "agencyType": "CRT",
                                "active": true,
                                "courtType": "CO"
                            },
                            "issuingCourtDate": "2021-12-13"
                        }
                    ],
                    "licenceSentences": [],
                    "keyDates": {
                        "sentenceStartDate": "2021-12-13",
                        "effectiveSentenceEndDate": "2026-02-12",
                        "nonDtoReleaseDate": "2026-02-12",
                        "nonDtoReleaseDateType": "CRD",
                        "confirmedReleaseDate": "2026-02-12",
                        "releaseDate": "2026-02-12",
                        "sentenceExpiryDate": "2026-02-12",
                        "conditionalReleaseDate": "2026-02-12",
                        "licenceExpiryDate": "2026-02-12",
                        "paroleEligibilityDate": "2024-09-23"
                    },
                    "sentenceAdjustments": {}
                },
                "previousPrisonTerms": []
            }
            """.trimIndent()
          )
      )
    )
  }
}
