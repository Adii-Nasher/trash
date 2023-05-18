import httpRequests._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

package object Scenario {

  val feeder = csv("src/user_data.csv").random

  val createUsersScenario: ScenarioBuilder = scenario("Create Users Scenario")
    .feed(feeder)
    .exec(createUserInfo)

  val getUsersScenario: ScenarioBuilder = scenario("Get Users Scenario")
    .exec(getUserInfo)

  val updateUsersScenario: ScenarioBuilder = scenario("Update Users Scenario")
    .feed(feeder)
    .exec(updateUserInfo)

  val chainedScenario: ScenarioBuilder = scenario("Chained scenario")
    .exec(createUsersScenario, getUsersScenario, updateUsersScenario)
}
