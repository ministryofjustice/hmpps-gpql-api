package uk.gov.justice.digital.hmpps.gqlapi.integration

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.gqlapi.helper.JwtAuthHelper
import uk.gov.justice.digital.hmpps.manageusersapi.integration.wiremock.CommunityApiMockServer
import uk.gov.justice.digital.hmpps.manageusersapi.integration.wiremock.HmppsAuthMockServer
import uk.gov.justice.digital.hmpps.manageusersapi.integration.wiremock.PrisonApiMockServer

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  companion object {
    @JvmField
    internal val prisonApiMockServer = PrisonApiMockServer()

    @JvmField
    internal val hmppsAuthMockServer = HmppsAuthMockServer()

    @JvmField
    internal val communityApiMockServer = CommunityApiMockServer()

    @BeforeAll
    @JvmStatic
    fun startMocks() {
      hmppsAuthMockServer.start()
      hmppsAuthMockServer.stubGrantToken()

      prisonApiMockServer.start()
      communityApiMockServer.start()
    }

    @AfterAll
    @JvmStatic
    fun stopMocks() {
      communityApiMockServer.stop()
      prisonApiMockServer.stop()
      hmppsAuthMockServer.stop()
    }
  }

  internal fun setAuthorisation(
    user: String = "GQL_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf()
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation(user, roles, scopes)

  fun readFile(file: String): String = this.javaClass.getResource(file).readText()
}
