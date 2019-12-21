package com.transportrouter.logic

import com.transportrouter.domain.{
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
  * Dijkstra's algorithm to finder quickest travel time from source station to
  * all other stations along with the visiting order.
  *
  * @param connections  List of directed edges.
  *                     (Edge represents travel time between two stations)
  */
class QuickestTravelTimeFinderImpl(connections: List[Connection]) extends QuickestTravelTimeFinder {

  private val stationConnections: Map[Station, List[Connection]] = connections.groupBy(_.from)

  private val travelTimes: MutableMap[Station, TravelTime] = {
    val times = MutableMap.empty[Station, TravelTime]
    connections.foreach { times += _.from -> TravelTime.Inf }
    times
  }

  private val visitedFrom: MutableMap[Station, Station] = MutableMap.empty
  private val relaxedStations = mutable.TreeMap.empty[StationTravelTime, Unit]

  /**
    * Finds quickest travel times from source to all other stations.
    * Unreachable stations are represented using TravelTime.Inf.
    *
    * @param source Source station from which travel times to all other stations
    *               is computed.
    * @return       QuickestTravelTimesToAllStations
    */
  override def quickestTravelTimesToAllStations(
      source: Station
  ): QuickestTravelTimesToAllStations = {

    if (stationConnections.contains(source)) {
      relaxedStations += StationTravelTime(source, TravelTime.Zero) -> ()
      travelTimes += source -> TravelTime.Zero
      visitedFrom += source -> source
    }

    while (relaxedStations.nonEmpty) {
      val kv = relaxedStations.min
      relaxedStations.remove(kv._1)
      stationConnections.getOrElse(kv._1.station, Nil).foreach(relax)
    }

    QuickestTravelTimesToAllStations(source, travelTimes.toMap, VisitedFrom(visitedFrom.toMap))
  }

  // Update the travel time from source if smaller travel time is found for
  // a particular station. Also update the TreeMap with new travel time.
  private def relax(edge: Connection): Unit = {
    val travelTimeFromSource = travelTimes(edge.from).plus(edge.travelTime)

    if (implicitly[Ordering[TravelTime]].gt(travelTimes.getOrElse(edge.to, TravelTime.Inf), travelTimeFromSource)) {
      relaxedStations.remove(StationTravelTime(edge.to, travelTimes.getOrElse(edge.to, TravelTime.Inf)))
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
