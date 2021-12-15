package uk.gov.justice.digital.hmpps.gqlapi.integration.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get

class CommunityApiMockServer : WireMockServer(WIREMOCK_PORT) {
  companion object {
    private const val WIREMOCK_PORT = 8096
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

  fun stubOffenderNotFound(nomsNumber: String) {
    stubFor(
      get("/secure/offenders/nomsNumber/$nomsNumber/convictions").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withStatus(404)
      )
    )
    stubFor(
      get("/secure/offenders/nomsNumber/$nomsNumber/allOffenderManagers").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withStatus(404)
      )
    )
  }

  fun stubConvictions(nomsNumber: String) {
    stubFor(
      get("/secure/offenders/nomsNumber/$nomsNumber/convictions").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(
            """
            [
                {
                    "convictionId": 2500468188,
                    "index": "4",
                    "active": true,
                    "inBreach": false,
                    "failureToComplyCount": 0,
                    "awaitingPsr": true,
                    "referralDate": "2021-05-20",
                    "offences": [
                        {
                            "offenceId": "M2500468188",
                            "mainOffence": true,
                            "detail": {
                                "code": "03700",
                                "description": "Aggravated taking of a vehicle - 03700",
                                "mainCategoryCode": "037",
                                "mainCategoryDescription": "Aggravated taking of a vehicle",
                                "mainCategoryAbbreviation": "Aggravated taking of a vehicle",
                                "ogrsOffenceCategory": "Taking and driving away and related offences",
                                "subCategoryCode": "00",
                                "subCategoryDescription": "Aggravated taking of a vehicle",
                                "form20Code": "47"
                            },
                            "offenceDate": "2021-05-20T00:00:00",
                            "offenceCount": 1,
                            "offenderId": 2500387823,
                            "createdDatetime": "2021-05-20T14:32:14",
                            "lastUpdatedDatetime": "2021-05-20T14:32:14"
                        }
                    ],
                    "latestCourtAppearanceOutcome": {
                        "code": "101",
                        "description": "Adjourned - Pre-Sentence Report"
                    },
                    "courtAppearance": {
                        "courtAppearanceId": 2500490059,
                        "appearanceDate": "2021-05-20T10:00:00",
                        "courtCode": "ABDRM1",
                        "courtName": "Aberdare Magistrates Court",
                        "appearanceType": {
                            "code": "T",
                            "description": "Trial/Adjournment"
                        },
                        "crn": "X349420"
                    }
                },
                {
                    "convictionId": 2500389581,
                    "index": "3",
                    "active": true,
                    "inBreach": true,
                    "failureToComplyCount": 0,
                    "awaitingPsr": false,
                    "convictionDate": "2020-12-02",
                    "referralDate": "2020-12-01",
                    "offences": [
                        {
                            "offenceId": "M2500389581",
                            "mainOffence": true,
                            "detail": {
                                "code": "05600",
                                "description": "Arson - 05600",
                                "mainCategoryCode": "056",
                                "mainCategoryDescription": "Arson",
                                "mainCategoryAbbreviation": "Arson",
                                "ogrsOffenceCategory": "Criminal damage",
                                "subCategoryCode": "00",
                                "subCategoryDescription": "Arson",
                                "form20Code": "58"
                            },
                            "offenceDate": "2020-12-01T00:00:00",
                            "offenceCount": 1,
                            "offenderId": 2500387823,
                            "createdDatetime": "2020-12-01T09:26:19",
                            "lastUpdatedDatetime": "2020-12-01T09:26:19"
                        }
                    ],
                    "sentence": {
                        "sentenceId": 2500447539,
                        "description": "CJA - Community Order",
                        "originalLength": 12,
                        "originalLengthUnits": "Months",
                        "defaultLength": 12,
                        "lengthInDays": 364,
                        "expectedSentenceEndDate": "2021-12-01",
                        "startDate": "2020-12-02",
                        "sentenceType": {
                            "code": "SP",
                            "description": "CJA - Community Order"
                        },
                        "failureToComplyLimit": 2,
                        "cja2003Order": true,
                        "legacyOrder": false
                    },
                    "latestCourtAppearanceOutcome": {
                        "code": "201",
                        "description": "CJA - Community Order"
                    },
                    "responsibleCourt": {
                        "courtId": 1,
                        "code": "ABDRM1",
                        "selectable": true,
                        "courtName": "Aberdare Magistrates Court",
                        "telephoneNumber": "01685 721731",
                        "fax": "01685 723919",
                        "buildingName": "The Court House",
                        "street": "Cwmbach Road",
                        "locality": "Aberdare",
                        "town": "Aberdare",
                        "county": "Glamorgan",
                        "postcode": "CF44 0NW",
                        "country": "Wales",
                        "courtTypeId": 310,
                        "createdDatetime": "2013-02-05T10:08:41",
                        "lastUpdatedDatetime": "2021-09-20T16:33:49",
                        "probationAreaId": 1500001002,
                        "probationArea": {
                            "code": "N03",
                            "description": "NPS Wales"
                        },
                        "courtType": {
                            "code": "MAG",
                            "description": "Magistrates Court"
                        }
                    },
                    "courtAppearance": {
                        "courtAppearanceId": 2500410339,
                        "appearanceDate": "2020-12-02T10:00:00",
                        "courtCode": "ABDRM1",
                        "courtName": "Aberdare Magistrates Court",
                        "appearanceType": {
                            "code": "S",
                            "description": "Sentence"
                        },
                        "crn": "X349420"
                    }
                },
                {
                    "convictionId": 2500365819,
                    "index": "2",
                    "active": true,
                    "inBreach": false,
                    "failureToComplyCount": 0,
                    "awaitingPsr": false,
                    "convictionDate": "2020-10-15",
                    "referralDate": "2020-10-15",
                    "offences": [
                        {
                            "offenceId": "M2500365819",
                            "mainOffence": true,
                            "detail": {
                                "code": "05600",
                                "description": "Arson - 05600",
                                "mainCategoryCode": "056",
                                "mainCategoryDescription": "Arson",
                                "mainCategoryAbbreviation": "Arson",
                                "ogrsOffenceCategory": "Criminal damage",
                                "subCategoryCode": "00",
                                "subCategoryDescription": "Arson",
                                "form20Code": "58"
                            },
                            "offenceDate": "2020-10-11T00:00:00",
                            "offenceCount": 1,
                            "offenderId": 2500387823,
                            "createdDatetime": "2020-10-15T11:17:23",
                            "lastUpdatedDatetime": "2020-10-15T11:17:24"
                        }
                    ],
                    "sentence": {
                        "sentenceId": 2500349233,
                        "description": "ORA Adult Custody (inc PSS)",
                        "originalLength": 2,
                        "originalLengthUnits": "Months",
                        "defaultLength": 2,
                        "lengthInDays": 60,
                        "expectedSentenceEndDate": "2020-12-14",
                        "startDate": "2020-10-15",
                        "sentenceType": {
                            "code": "SC",
                            "description": "ORA Adult Custody (inc PSS)"
                        },
                        "failureToComplyLimit": 3,
                        "cja2003Order": true,
                        "legacyOrder": false
                    },
                    "latestCourtAppearanceOutcome": {
                        "code": "325",
                        "description": "ORA Adult Custody (inc PSS)"
                    },
                    "custody": {
                        "bookingNumber": "38500A",
                        "institution": {
                            "institutionId": 156,
                            "isEstablishment": true,
                            "code": "COMMUN",
                            "description": "In the Community",
                            "institutionName": "In the Community",
                            "establishmentType": {
                                "code": "E",
                                "description": "Prison"
                            }
                        },
                        "keyDates": {
                            "conditionalReleaseDate": "2020-11-14",
                            "licenceExpiryDate": "2021-06-20",
                            "sentenceExpiryDate": "2020-12-14",
                            "postSentenceSupervisionEndDate": "2021-11-14"
                        },
                        "status": {
                            "code": "B",
                            "description": "Released - On Licence"
                        },
                        "sentenceStartDate": "2020-10-15"
                    },
                    "responsibleCourt": {
                        "courtId": 1,
                        "code": "ABDRM1",
                        "selectable": true,
                        "courtName": "Aberdare Magistrates Court",
                        "telephoneNumber": "01685 721731",
                        "fax": "01685 723919",
                        "buildingName": "The Court House",
                        "street": "Cwmbach Road",
                        "locality": "Aberdare",
                        "town": "Aberdare",
                        "county": "Glamorgan",
                        "postcode": "CF44 0NW",
                        "country": "Wales",
                        "courtTypeId": 310,
                        "createdDatetime": "2013-02-05T10:08:41",
                        "lastUpdatedDatetime": "2021-09-20T16:33:49",
                        "probationAreaId": 1500001002,
                        "probationArea": {
                            "code": "N03",
                            "description": "NPS Wales"
                        },
                        "courtType": {
                            "code": "MAG",
                            "description": "Magistrates Court"
                        }
                    },
                    "courtAppearance": {
                        "courtAppearanceId": 2500386991,
                        "appearanceDate": "2020-10-15T00:00:00",
                        "courtCode": "ABDRM1",
                        "courtName": "Aberdare Magistrates Court",
                        "appearanceType": {
                            "code": "S",
                            "description": "Sentence"
                        },
                        "crn": "X349420"
                    }
                },
                {
                    "convictionId": 2500332817,
                    "index": "1",
                    "active": false,
                    "inBreach": false,
                    "failureToComplyCount": 0,
                    "awaitingPsr": false,
                    "convictionDate": "2020-07-10",
                    "referralDate": "2020-07-10",
                    "offences": [
                        {
                            "offenceId": "M2500332817",
                            "mainOffence": true,
                            "detail": {
                                "code": "05601",
                                "description": "Arson endangering life - 05601",
                                "mainCategoryCode": "056",
                                "mainCategoryDescription": "Arson",
                                "mainCategoryAbbreviation": "Arson",
                                "ogrsOffenceCategory": "Criminal damage",
                                "subCategoryCode": "01",
                                "subCategoryDescription": "Arson endangering life",
                                "form20Code": "57"
                            },
                            "offenceDate": "2020-07-05T00:00:00",
                            "offenceCount": 1,
                            "offenderId": 2500387823,
                            "createdDatetime": "2020-07-10T09:07:40",
                            "lastUpdatedDatetime": "2020-07-10T09:07:40"
                        }
                    ],
                    "sentence": {
                        "sentenceId": 2500316731,
                        "description": "ORA Adult Custody (Not PSS)",
                        "originalLength": 24,
                        "originalLengthUnits": "Months",
                        "defaultLength": 24,
                        "lengthInDays": 729,
                        "expectedSentenceEndDate": "2022-07-09",
                        "startDate": "2020-07-10",
                        "terminationDate": "2020-10-15",
                        "terminationReason": "Rvkd (other)",
                        "sentenceType": {
                            "code": "SC",
                            "description": "ORA Adult Custody (Not PSS)"
                        },
                        "failureToComplyLimit": 3,
                        "cja2003Order": true,
                        "legacyOrder": false
                    },
                    "latestCourtAppearanceOutcome": {
                        "code": "456",
                        "description": "ORA Adult Custody (Not PSS)"
                    },
                    "custody": {
                        "bookingNumber": "38457A",
                        "institution": {
                            "institutionId": 97,
                            "isEstablishment": true,
                            "code": "MDIHMP",
                            "description": "Moorland (HMP & YOI)",
                            "institutionName": "Moorland (HMP & YOI)",
                            "establishmentType": {
                                "code": "E",
                                "description": "Prison"
                            },
                            "nomsPrisonInstitutionCode": "MDI"
                        },
                        "keyDates": {
                            "conditionalReleaseDate": "2021-07-09",
                            "licenceExpiryDate": "2022-07-09",
                            "hdcEligibilityDate": "2021-02-25",
                            "sentenceExpiryDate": "2022-07-09",
                            "expectedPrisonOffenderManagerHandoverStartDate": "2021-07-19",
                            "expectedPrisonOffenderManagerHandoverDate": "2021-06-19"
                        },
                        "status": {
                            "code": "T",
                            "description": "Terminated"
                        },
                        "sentenceStartDate": "2020-07-10"
                    },
                    "responsibleCourt": {
                        "courtId": 1,
                        "code": "ABDRM1",
                        "selectable": true,
                        "courtName": "Aberdare Magistrates Court",
                        "telephoneNumber": "01685 721731",
                        "fax": "01685 723919",
                        "buildingName": "The Court House",
                        "street": "Cwmbach Road",
                        "locality": "Aberdare",
                        "town": "Aberdare",
                        "county": "Glamorgan",
                        "postcode": "CF44 0NW",
                        "country": "Wales",
                        "courtTypeId": 310,
                        "createdDatetime": "2013-02-05T10:08:41",
                        "lastUpdatedDatetime": "2021-09-20T16:33:49",
                        "probationAreaId": 1500001002,
                        "probationArea": {
                            "code": "N03",
                            "description": "NPS Wales"
                        },
                        "courtType": {
                            "code": "MAG",
                            "description": "Magistrates Court"
                        }
                    },
                    "courtAppearance": {
                        "courtAppearanceId": 2500353989,
                        "appearanceDate": "2020-07-10T00:00:00",
                        "courtCode": "ABDRM1",
                        "courtName": "Aberdare Magistrates Court",
                        "appearanceType": {
                            "code": "S",
                            "description": "Sentence"
                        },
                        "crn": "X349420"
                    }
                }
            ]              
            """.trimIndent()
          )
      )
    )
  }

  fun stubOffenderManagers(nomsNumber: String) {
    stubFor(
      get("/secure/offenders/nomsNumber/$nomsNumber/allOffenderManagers").willReturn(
        aResponse()
          .withHeader("Content-Type", "application/json")
          .withBody(
            """
            [
                {
                    "staffCode": "N07A516",
                    "staffId": 2500124223,
                    "isResponsibleOfficer": true,
                    "isPrisonOffenderManager": false,
                    "isUnallocated": false,
                    "staff": {
                        "forenames": "Gurnank",
                        "surname": "Cheema",
                        "email": "gurnank.cheema@digital.justice.gov.uk"
                    },
                    "team": {
                        "code": "N07T01",
                        "description": "OMU A",
                        "localDeliveryUnit": {
                            "code": "N07NPSA",
                            "description": "N07 Division"
                        },
                        "teamType": {
                            "code": "N07NPS1",
                            "description": "N07 LDU 1"
                        },
                        "district": {
                            "code": "N07NPSA",
                            "description": "N07 Division"
                        },
                        "borough": {
                            "code": "N07100",
                            "description": "N07 Cluster 1"
                        },
                        "startDate": "2014-08-29"
                    },
                    "probationArea": {
                        "probationAreaId": 1500001006,
                        "code": "N07",
                        "description": "NPS London",
                        "organisation": {
                            "code": "NPS",
                            "description": "National Probation Service"
                        }
                    },
                    "fromDate": "2021-02-15",
                    "grade": {
                        "code": "NPSM",
                        "description": "NPS - PO"
                    }
                },
                {
                    "staffCode": "MDIA054",
                    "staffId": 2500130524,
                    "isResponsibleOfficer": false,
                    "isPrisonOffenderManager": true,
                    "isUnallocated": false,
                    "staff": {
                        "forenames": "Sheila",
                        "surname": "Hancock",
                        "email": "officer@gov.uk",
                        "phoneNumber": "0123411278"
                    },
                    "team": {
                        "code": "MDIPOM",
                        "description": "Prison Offender Managers",
                        "localDeliveryUnit": {
                            "code": "MDIPOM",
                            "description": "Prison Offender Managers"
                        },
                        "teamType": {
                            "code": "MDIPOM",
                            "description": "Prison Offender Managers"
                        },
                        "district": {
                            "code": "MDIPOM",
                            "description": "Prison Offender Managers"
                        },
                        "borough": {
                            "code": "MDIPOM",
                            "description": "Prison Offender Managers"
                        },
                        "startDate": "2019-12-05"
                    },
                    "probationArea": {
                        "probationAreaId": 2500002202,
                        "code": "MDI",
                        "description": "Moorland (HMP & YOI)",
                        "organisation": {
                            "code": "PRI",
                            "description": "Establishment Providers"
                        },
                        "institution": {
                            "institutionId": 97,
                            "isEstablishment": true,
                            "code": "MDIHMP",
                            "description": "Moorland (HMP & YOI)",
                            "institutionName": "Moorland (HMP & YOI)",
                            "establishmentType": {
                                "code": "E",
                                "description": "Prison"
                            },
                            "nomsPrisonInstitutionCode": "MDI"
                        }
                    },
                    "fromDate": "2021-10-18"
                }
            ]              
            """.trimIndent()
          )
      )
    )
  }
}
