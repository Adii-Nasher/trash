import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
package object httpRequests {
  val baseUrl = "https://reqres.in/api/users"

  val createUserInfo: HttpRequestBuilder = http("Create user info")
    .post(baseUrl)
    .body(
      StringBody(
        """{ "name": "${name}", "job": "${job}", "id": "${id}" }"""
      )
    )
    .asJson
    .header("content-type", "application/json")
    .check(
      status.is (201),
      status.not(404),
      status.not(500),
      status.not(400),
      jsonPath("$.createdAt").ofType[String].exists
  )

  val getUserInfo: HttpRequestBuilder = http("Get all users")
    .get(baseUrl)
    .queryParam("page", Seq("1", "2"))
    .check(
      status.is (200),
      status.not(404),
      status.not(500),
      status.not(400),
      jsonPath("$.data[0].id").ofType[Int].exists,
      jsonPath("$.data[0].email").ofType[String].exists,
      jsonPath("$.data[0].first_name").ofType[String].exists,
      jsonPath("$.data[0].last_name").ofType[String].exists
    )

  val updateUserInfo: HttpRequestBuilder = http("Update user info")
    .put(baseUrl + "/2")
    .body(
      StringBody(
        """{ "name": "${name}", "job": "${job}", "id": "${id}" }"""
      )
    )
    .asJson
    .header("content-type", "application/json")
    .check(
      status.is(200),
      status.not(404),
      status.not(500),
      status.not(400),
      jsonPath("$.updatedAt").ofType[String].exists
    )
}
