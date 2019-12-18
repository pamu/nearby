package com.nearby.logic

import com.nearby.domain.{Connection, EvaluatedStation, Station, StationTravelTime, TravelTime,}

import scala.collection.mutable
import scala.collection.mutable.{Map => MutableMap}

class QuickestTravelTimeFinder(connections: List[Connection]) {
  private val stationConnections: Map[Station, List[Connection]] = connections.groupBy(_.from)
  private val travelTimes: MutableMap[Station, TravelTime] =
    MutableMap
      .newBuilder(connections.map(_ -> TravelTime.Inf))
      .result()

  private val visitedFrom: MutableMap[Station, Station] = MutableMap.empty
  private val relaxedStations = mutable.TreeMap.empty[StationTravelTime, Unit]

  def quickestTravelTimeToAllStations(start: Station): EvaluatedStation = {

    relaxedStations += StationTravelTime(start, TravelTime.Zero) -> ()

    travelTimes += start -> TravelTime.Zero
    visitedFrom += start -> start

    while (relaxedStations.nonEmpty) {
      val station = relaxedStations.firstKey
      stationConnections(station.station).foreach(relax)
    }

    EvaluatedStation(start, travelTimes.toMap, visitedFrom.toMap)
  }

  private def relax(edge: Connection): Unit = {
    val travelTimeFromSource = travelTimes(edge.from).plus(edge.travelTime)

    if (implicitly[Ordering[TravelTime]].gt(travelTimes(edge.to), travelTimeFromSource)) {
      relaxedStations.remove(StationTravelTime(edge.to, travelTimes(edge.to)))
      relaxedStations += StationTravelTime(edge.to, travelTimeFromSource) -> Unit
      travelTimes(edge.to) = travelTimeFromSource
      visitedFrom(edge.to) = edge.from
    }
  }

}

object QuickestTravelTimeFinder {

  def apply(connections: List[Connection]): QuickestTravelTimeFinder =
    new QuickestTravelTimeFinder(connections)
}
