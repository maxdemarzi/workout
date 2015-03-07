import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration._

class GetFriends extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:7474")
    .acceptHeader("application/json")
    /* Uncomment to see the response of each request.
    .extraInfoExtractor(extraInfo => {
      println(extraInfo.response.body.string)
      Nil
    }).disableResponseChunksDiscarding
    */

  // Use a data file for our requests and repeat values if we get to the end.
  val feeder = csv("usernames.csv").circular

  val query = """MATCH (me:User {username:{username}})-[:FRIENDS]-(people) RETURN people.username"""
  val cypherQuery = """{"statements" : [{"statement" : "%s", "parameters" : { "username": "${username}" }}]}""".format(query)


  val scn = scenario("Get Friends")
    .repeat(1000) {
    feed(feeder)
    .exec(
      http("get friends")
        .post("/db/data/transaction/commit")
        .basicAuth("neo4j", "swordfish")
        .body(StringBody(cypherQuery))
        .asJSON
        .check(status.is(200))
    )
  }

  setUp(
    scn.inject(rampUsers(10) over(10 seconds)).protocols(httpConf)
  )
}