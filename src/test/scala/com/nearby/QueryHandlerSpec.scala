package com.nearby

import com.nearby.domain.{Connection, Station, TravelTime}
import com.nearby.logic.QueryHandler

class QueryHandlerSpec extends CommonSpec {

  //    8
  //    A -> B: 240
  //    A -> C: 70
  //    A -> D: 120
  //    C -> B: 60
  //    D -> E: 480
  //    C -> E: 240
  //    B -> E: 210
  //    E -> A: 300
  //    route A -> B
  //    nearby A, 130

  private implicit val handler: QueryHandler = QueryHandler(
    List(
      Connection(Station("A"), Station("B"), TravelTime(240)),
      Connection(Station("A"), Station("C"), TravelTime(70)),
      Connection(Station("A"), Station("D"), TravelTime(120)),
      Connection(Station("C"), Station("B"), TravelTime(60)),
      Connection(Station("D"), Station("E"), TravelTime(480)),
      Connection(Station("C"), Station("E"), TravelTime(240)),
      Connection(Station("B"), Station("E"), TravelTime(210)),
      Connection(Station("E"), Station("A"), TravelTime(300)),
      Connection(Station("X"), Station("Y"), TravelTime(100)), // Unreachable stations
    ))

  "No stations within zero travel time" in {
    Main.handleQueryLine("nearby A, 0").toString mustBe ""
  }

  "No route to unknown station" in {
    // Note Z station is not present in input
    Main.handleQueryLine("route A -> Z").toString mustBe "Error: No route from A to Z"
  }

  "No route to unreachable station" in {
    Main.handleQueryLine("route A -> X").toString mustBe "Error: No route from A to X"
    Main.handleQueryLine("route A -> Y").toString mustBe "Error: No route from A to Y"
  }

  "Return correct result for route query" in {
    Main.handleQueryLine("route A -> B").toString mustBe "A -> C -> B: 130"
    Main.handleQueryLine("route A -> E").toString mustBe "A -> C -> E: 310"
  }

  "Return correct result for nearby query" in {
    Main.handleQueryLine("nearby A, 130").toString mustBe "C: 70, D: 120, B: 130"
  }

}
