package com.nearby.logic

import Implicits._
import com.nearby.domain.Result.RouteFound
import com.nearby.domain.{Connection, Query, QuickestTravelTimesToAllStations, Result, Station, TravelTime, VisitedFrom}

import scala.collection.mutable.{Map => MutableMap}

trait QueryHandler {
  def handle(query: Query): Result
}

/**
  * Query handler handles two types of queries.
  * 1. Route query
  * 2. Nearby query
  * It also caches the computed quickest travel times from particular source
  * station for future queries.
  *
  * @param connections  List of directed edges
  *                     (Edge represents travel time between two stations)
  */
class QueryHandlerImpl(connections: List[Connection]) extends QueryHandler {

  // Other implementations of QuickestTravelTimeFinder can be injected.
  private val finder: QuickestTravelTimeFinder = new QuickestTravelTimeFinderImpl(connections)
  private val cache: MutableMap[Station, QuickestTravelTimesToAllStations] = MutableMap.empty

  /**
    * Handle query and return result.
    *
    * @param query Query ADT to handle
    * @return      Result ADT
    */
  override def handle(query: Query): Result = query match {
    case Query.Route(source, destination) =>
      val travelTimes = cacheResult(source)

      travelTimes.quickestTravelTimes.get(destination) match {
        case Some(value) if value != TravelTime.Inf => RouteFound(travelTimes.visitedFrom.path(destination), value)
        case _                                      => Result.RouteNotFound(source, destination)
      }

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

  // Cache the computed result for future use.
  private def cacheResult(source: Station): QuickestTravelTimesToAllStations =
    cache.getOrElseUpdate(source, finder.quickestTravelTimesToAllStations(source))
}

object QueryHandler {
  def apply(connections: List[Connection]): QueryHandler = new QueryHandlerImpl(connections)
}
