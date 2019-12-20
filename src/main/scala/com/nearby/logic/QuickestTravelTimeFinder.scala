package com.nearby.logic

import com.nearby.domain.{
  Connection,
  QuickestTravelTimesToAllStations,
  Station,
  StationTravelTime,
  TravelTime,
  VisitedFrom
}

import scala.collection.mutable
import scala.collection.mutable.{Map => MutableMap}

trait QuickestTravelTimeFinder {
  def quickestTravelTimesToAllStations(start: Station): QuickestTravelTimesToAllStations
}

/**
  *
  * @param connections
  */
class QuickestTravelTimeFinderImpl(connections: List[Connection]) extends QuickestTravelTimeFinder {

  private val stationConnections: Map[Station, List[Connection]] =
    connections.groupBy(_.from)

  private val travelTimes: MutableMap[Station, TravelTime] = {
    val times = MutableMap.empty[Station, TravelTime]
    connections.foreach { times += _.from -> TravelTime.Inf }
    times
  }

  private val visitedFrom: MutableMap[Station, Station] = MutableMap.empty
  private val relaxedStations = mutable.TreeMap.empty[StationTravelTime, Unit]

  /**
    *
    * @param start
    * @return
    */
  override def quickestTravelTimesToAllStations(
      start: Station
  ): QuickestTravelTimesToAllStations = {

    relaxedStations += StationTravelTime(start, TravelTime.Zero) -> ()

    travelTimes += start -> TravelTime.Zero
    visitedFrom += start -> start

    while (relaxedStations.nonEmpty) {
      val kv = relaxedStations.min
      relaxedStations.remove(kv._1)
      stationConnections(kv._1.station).foreach(relax)
    }

    QuickestTravelTimesToAllStations(start, travelTimes.toMap, VisitedFrom(visitedFrom.toMap))
  }

  private def relax(edge: Connection): Unit = {
    val travelTimeFromSource = travelTimes(edge.from).plus(edge.travelTime)

    if (implicitly[Ordering[TravelTime]].gt(travelTimes(edge.to), travelTimeFromSource)) {
      relaxedStations.remove(StationTravelTime(edge.to, travelTimes(edge.to)))
      relaxedStations += StationTravelTime(edge.to, travelTimeFromSource) -> ()
      travelTimes(edge.to) = travelTimeFromSource
      visitedFrom(edge.to) = edge.from
    }
  }

}

object QuickestTravelTimeFinder {

  def apply(connections: List[Connection]): QuickestTravelTimeFinder =
    new QuickestTravelTimeFinderImpl(connections)
}
