import httpRequests._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

package object Scenario {

  val feeder = csv("src/user_data.csv").random

  val getUsersScenario: ScenarioBuilder = scenario("Get Users Scenario")
    .exec(getUserInfo)

  val chainedScenario: ScenarioBuilder = scenario("Chained scenario")
    .exec(getUsersScenario)
}
