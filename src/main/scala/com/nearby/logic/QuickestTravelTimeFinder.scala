package com.nearby.logic

import com.nearby.domain.{Connection, QuickestTravelTimes, Station, StationTravelTime, TravelTime, VisitHistory}

import scala.collection.mutable
import scala.collection.mutable.{Map => MutableMap}

class QuickestTravelTimeFinder(connections: Map[Station, List[Connection]]) {

  private val travelTimes: MutableMap[Station, TravelTime] =
    MutableMap
      .newBuilder(connections.map(_ -> TravelTime.Inf))
      .result()

  private val visitedFrom: MutableMap[Station, Station] = MutableMap.empty
  private val quickestTimes = mutable.TreeMap.empty[StationTravelTime, Unit]

  def quickestTravelTimeToAllStations(start: Station): (QuickestTravelTimes, VisitHistory) = {

    quickestTimes += StationTravelTime(start, TravelTime.Zero) -> ()

    travelTimes += start -> TravelTime.Zero
    visitedFrom += start -> start

    while (quickestTimes.nonEmpty) {
      val station = quickestTimes.firstKey
      connections(station.station).foreach(relax)
    }

    QuickestTravelTimes(start, travelTimes.toMap) -> VisitHistory(start, visitedFrom.toMap)
  }

  private def relax(edge: Connection): Unit = {
    val travelTimeFromSource = travelTimes(edge.from).plus(edge.travelTime)

    if (implicitly[Ordering[TravelTime]].gt(travelTimes(edge.to), travelTimeFromSource)) {
      quickestTimes.remove(StationTravelTime(edge.to, travelTimes(edge.to)))
      quickestTimes += StationTravelTime(edge.to, travelTimeFromSource) -> Unit
      travelTimes(edge.to) = travelTimeFromSource
      visitedFrom(edge.to) = edge.from
    }
  }

}
