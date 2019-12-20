package com.nearby

import com.nearby.domain.{Connection, Station, TravelTime}
import com.nearby.logic.QuickestTravelTimeFinder

class QuickestTravelTimeFinderSpec extends CommonSpec {

  /**
    * A -> B: 240
    * A -> C: 70
    * A -> D: 120
    * C -> B: 60
    * D -> E: 480
    * C -> E: 240
    * B -> E: 210
    * E -> A: 300
    */
  private val finder: QuickestTravelTimeFinder = QuickestTravelTimeFinder(
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

  "Empty connections must produce empty results" in {

  }


}
