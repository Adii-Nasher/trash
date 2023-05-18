package Simulation

import Scenario.chainedScenario
import io.gatling.core.Predef._
import scala.concurrent.duration._

class GetUsers extends Simulation {

    setUp(
      chainedScenario.inject(
        nothingFor(5 seconds),
        atOnceUsers(50),
        constantUsersPerSec(20) during (15 seconds),
        rampUsersPerSec(1) to 100 during (30 seconds))
    ).assertions(
      global.responseTime.max.lt(4000),
      global.successfulRequests.percent.gt(98)
    ).maxDuration(1 minute)
}
