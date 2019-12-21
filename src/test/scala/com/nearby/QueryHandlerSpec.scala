package com.nearby

import com.nearby.domain.{Connection, Station, TravelTime}
import com.nearby.logic.QueryHandler

class QueryHandlerSpec extends WordSpecWithMatchers {

  private val unreachableStations = Connection(Station("100"), Station("101"), TravelTime(100))

  private implicit val handler: QueryHandler = QueryHandler(
    Common.connections ++ List(unreachableStations)
  )

  "No stations within zero travel time" in {
    Main.handleQueryLine("nearby 0, 0").toString mustBe ""
  }

  "No route to unknown station" in {
    // Note "1000" station is not present in connections
    Main.handleQueryLine("route 0 -> 1000").toString mustBe "Error: No route from 0 to 1000"
  }

  "No route to unreachable station" in {
    Main.handleQueryLine("route 0 -> 100").toString mustBe "Error: No route from 0 to 100"
    Main.handleQueryLine("route 0 -> 101").toString mustBe "Error: No route from 0 to 101"
  }

  "Return correct result for route query" in {
    Main.handleQueryLine("route 0 -> 1").toString mustBe "0 -> 1: 5"
    Main.handleQueryLine("route 0 -> 7").toString mustBe "0 -> 7: 8"
    Main.handleQueryLine("route 0 -> 4").toString mustBe "0 -> 4: 9"
    Main.handleQueryLine("route 0 -> 5").toString mustBe "0 -> 4 -> 5: 13"
    Main.handleQueryLine("route 0 -> 2").toString mustBe "0 -> 4 -> 5 -> 2: 14"
    Main.handleQueryLine("route 0 -> 3").toString mustBe "0 -> 4 -> 5 -> 2 -> 3: 17"
    Main.handleQueryLine("route 0 -> 6").toString mustBe "0 -> 4 -> 5 -> 2 -> 6: 25"
  }

  "Return correct result for nearby query" in {
    Main.handleQueryLine("nearby 0, 10").toString mustBe "1: 5, 7: 8, 4: 9"
    Main.handleQueryLine("nearby 0, 15").toString mustBe "1: 5, 7: 8, 4: 9, 5: 13, 2: 14"
    Main.handleQueryLine("nearby 0, 20").toString mustBe "1: 5, 7: 8, 4: 9, 5: 13, 2: 14, 3: 17"
    Main.handleQueryLine("nearby 0, 25").toString mustBe "1: 5, 7: 8, 4: 9, 5: 13, 2: 14, 3: 17, 6: 25"
  }

  "Return correct results for given test data" in {
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

    implicit val handler: QueryHandler = QueryHandler(
      List(
        Connection(Station("A"), Station("B"), TravelTime(240)),
        Connection(Station("A"), Station("C"), TravelTime(70)),
        Connection(Station("A"), Station("D"), TravelTime(120)),
        Connection(Station("C"), Station("B"), TravelTime(60)),
        Connection(Station("D"), Station("E"), TravelTime(480)),
        Connection(Station("C"), Station("E"), TravelTime(240)),
        Connection(Station("B"), Station("E"), TravelTime(210)),
        Connection(Station("E"), Station("A"), TravelTime(300))
      ))

    Main.handleQueryLine("route A -> B").toString mustBe "A -> C -> B: 130"
    Main.handleQueryLine("nearby A, 130").toString mustBe "C: 70, D: 120, B: 130"
  }

}
