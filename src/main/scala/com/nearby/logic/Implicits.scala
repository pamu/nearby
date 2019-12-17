package com.nearby.logic

import com.nearby.domain.{QuickestTravelTimes, Station, TravelTime, VisitHistory}

object Implicits {

  implicit class TravelTimeOps(times: QuickestTravelTimes) {

    def quickestTravelTime(dest: Station): TravelTime =
      times.travelTimes.getOrElse(dest, TravelTime.Inf)

  }

  implicit class PathOps(history: VisitHistory) {

    def path(to: Station): List[Station] = {
      val visitedFrom = history.visitedFrom
      var path: List[Station] = to :: Nil
      while (visitedFrom.contains(path.head) &&
             visitedFrom(path.head) != path.head) {
        path = visitedFrom(path.head) :: path
      }
      path
    }

  }

}
