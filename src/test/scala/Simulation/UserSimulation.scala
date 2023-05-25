package Simulation

import UserScenario.chainedScenario
import io.gatling.core.Predef._
import scala.concurrent.duration._

class UserSimulation extends Simulation {

  val onceUser : Int = Integer.getInteger("forAtOnceUsers")

  val constantUser : Double = System.getProperty("forConstantUsers") match {
    case null => 1.0
    case str => java.lang.Double.parseDouble(str)
  }

  val constantTime  = java.lang.Long.getLong("sec",0)

  val rampUser : Int = Integer.getInteger("forRampUpUsers", 1)

  val myRamp  = java.lang.Long.getLong("ramp", 0)

    setUp(
      chainedScenario.inject(
        nothingFor(5.seconds),
        atOnceUsers(onceUser),
        constantUsersPerSec(constantUser).during(constantTime),
        rampUsers(rampUser).during(myRamp))
    ).assertions(
      global.responseTime.max.between(500,15000),
      global.successfulRequests.percent.gt(98)
    ).maxDuration(1.minutes)
}
