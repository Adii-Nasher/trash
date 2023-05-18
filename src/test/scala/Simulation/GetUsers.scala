package Simulation

import Scenario.chainedScenario
import io.gatling.core.Predef._
import scala.concurrent.duration._

class GetUsers extends Simulation {

    setUp(
      chainedScenario.inject(
        nothingFor(5 seconds),
        atOnceUsers(50))
    ).assertions(
      global.responseTime.max.lt(4000),
      global.successfulRequests.percent.gt(98)
    ).maxDuration(1 minute)
}
