package com.transportrouter

import com.transportrouter.domain.{Connection, Station, TravelTime}
import com.transportrouter.logic.QuickestTravelTimeFinder

class QuickestTravelTimeFinderSpec extends WordSpecWithMatchers {

  "Empty connections must produce empty results" in {
    val finder = QuickestTravelTimeFinder(Nil)
    val result = finder.quickestTravelTimesToAllStations(Station("unknown"))
    result.quickestTravelTimes mustBe empty
    result.visitedFrom.value mustBe empty
  }

  "Finder must produce correct results for one connection" in {
    val finder = QuickestTravelTimeFinder(List(Connection(Station("A"), Station("B"), TravelTime(200))))
    val result = finder.quickestTravelTimesToAllStations(Station("A"))
    result.quickestTravelTimes mustBe
      Map(Station("A") -> TravelTime.Zero, Station("B") -> TravelTime(200))
  }

  "Finder must produce correct results for known graph" in {
    val finder: QuickestTravelTimeFinder = QuickestTravelTimeFinder(Common.connections)

    val result = finder.quickestTravelTimesToAllStations(Station("0"))

    result.quickestTravelTimes mustBe Map(
      Station("0") -> TravelTime(0),
      Station("1") -> TravelTime(5),
      Station("2") -> TravelTime(14),
      Station("3") -> TravelTime(17),
      Station("4") -> TravelTime(9),
      Station("5") -> TravelTime(13),
      Station("6") -> TravelTime(25),
      Station("7") -> TravelTime(8)
    )

    result.visitedFrom.value mustBe Map(
      Station("0") -> Station("0"),
      Station("1") -> Station("0"),
      Station("2") -> Station("5"),
      Station("3") -> Station("2"),
      Station("4") -> Station("0"),
      Station("5") -> Station("4"),
      Station("6") -> Station("2"),
      Station("7") -> Station("0")
    )

  }

  "Travel time to disconnected station should be Inf" in {
    val finder: QuickestTravelTimeFinder = QuickestTravelTimeFinder(
      List(
        Connection(Station("A"), Station("B"), TravelTime(10)),
        Connection(Station("A"), Station("C"), TravelTime(20)),
        Connection(Station("D"), Station("E"), TravelTime(5)),
      )
    )

    // note that A is connected to D

    finder
      .quickestTravelTimesToAllStations(Station("A"))
      .quickestTravelTimes(Station("D")) mustBe TravelTime.Inf

  }
}
