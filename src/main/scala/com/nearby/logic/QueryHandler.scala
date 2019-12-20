package com.nearby.logic

import Implicits._
import com.nearby.domain.Result.RouteFound
import com.nearby.domain.{Connection, Query, QuickestTravelTimesToAllStations, Result, Station, TravelTime, VisitedFrom}

import scala.collection.mutable.{Map => MutableMap}

trait QueryHandler {
  def handle(query: Query): Result
}

/**
  *
  * @param connections
  */
class QueryHandlerImpl(connections: List[Connection]) extends QueryHandler {

  // Other implementations of QuickestTravelTimeFinder can be injected.
  private val finder: QuickestTravelTimeFinder = new QuickestTravelTimeFinderImpl(connections)
  private val cache: MutableMap[Station, QuickestTravelTimesToAllStations] = MutableMap.empty

  override def handle(query: Query): Result = query match {
    case Query.Route(source, destination) =>
      val travelTimes = cacheResult(source)

      if (travelTimes.quickestTravelTimes.contains(destination))
        RouteFound(travelTimes.visitedFrom.path(destination), travelTimes.quickestTravelTimes(destination))
      else Result.RouteNotFound(source, destination)

    case Query.Nearby(source, maximumTravelTime) =>
      val travelTimes = cacheResult(source)

      val stationsSortedByTravelTime = travelTimes.quickestTravelTimes.view
        .filterNot(_._1 == source)
        .collect {
          case (station, travelTime) if implicitly[Ordering[TravelTime]].lteq(travelTime, maximumTravelTime) =>
            (station, travelTime)
        }
        .toList
        .sortBy(_._2)

      Result.NearbyStations(stationsSortedByTravelTime)
  }

  private def cacheResult(source: Station): QuickestTravelTimesToAllStations =
    cache.getOrElseUpdate(source, finder.quickestTravelTimesToAllStations(source))
}

object QueryHandler {
  def apply(connections: List[Connection]): QueryHandler = new QueryHandlerImpl(connections)
}
