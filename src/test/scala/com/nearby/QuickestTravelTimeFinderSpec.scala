package com.nearby

import com.nearby.domain.{Connection, Station, TravelTime}
import com.nearby.logic.QuickestTravelTimeFinder

class QuickestTravelTimeFinderSpec extends CommonSpec {

  "Empty connections must produce empty results" in {
    val finder = QuickestTravelTimeFinder(Nil)
    val result = finder.quickestTravelTimesToAllStations(Station("unknown"))
    result.quickestTravelTimes mustBe empty
    result.visitedFrom.value mustBe empty
  }

  "Finder must produce correct results for 1 edge" in {
    val finder = QuickestTravelTimeFinder(List(Connection(Station("A"), Station("B"), TravelTime(200))))
    val result = finder.quickestTravelTimesToAllStations(Station("A"))
    result.quickestTravelTimes mustBe Map(Station("A") -> TravelTime.Zero, Station("B") -> TravelTime(200))
  }

  "Finder must produce correct results for known graph" in {

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

    val finder: QuickestTravelTimeFinder = QuickestTravelTimeFinder(
      List(
        Connection(Station("A"), Station("B"), TravelTime(240)),
        Connection(Station("A"), Station("C"), TravelTime(70)),
        Connection(Station("A"), Station("D"), TravelTime(120)),
        Connection(Station("C"), Station("B"), TravelTime(60)),
        Connection(Station("D"), Station("E"), TravelTime(480)),
        Connection(Station("C"), Station("E"), TravelTime(240)),
        Connection(Station("B"), Station("E"), TravelTime(210)),
        Connection(Station("E"), Station("A"), TravelTime(300)),
      )
    )

    val result = finder.quickestTravelTimesToAllStations(Station("A"))

    result.quickestTravelTimes mustBe Map(Station("E") -> TravelTime(310),
                                          Station("A") -> TravelTime(0),
                                          Station("B") -> TravelTime(130),
                                          Station("C") -> TravelTime(70),
                                          Station("D") -> TravelTime(120))

    result.visitedFrom.value mustBe Map(Station("E") -> Station("C"),
                                        Station("A") -> Station("A"),
                                        Station("B") -> Station("C"),
                                        Station("C") -> Station("A"),
                                        Station("D") -> Station("A"))

  }
}
