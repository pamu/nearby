package com.nearby.domain

/**
  * Represents station.
  * Wrapper type for type-safety.
  * https://docs.scala-lang.org/overviews/core/value-classes.html
  *
  * @param name Name identifier of the station.
  */
case class Station(name: String) extends AnyVal

/**
  * Represents travel time.
  * None represents practically infinite time.
  *
  * @param value Time to travel.
  */
case class TravelTime(value: Option[BigDecimal]) {
  if (value.isDefined)
    require(value.get > 0, "Time must be zero or positive")

  def plus(other: TravelTime): TravelTime = (value, other.value) match {
    case (Some(x), Some(y)) => TravelTime(Some(x + y))
    case (_, _)             => TravelTime(None)
  }
}

object TravelTime {

  val Inf: TravelTime = TravelTime(None)
  val Zero: TravelTime = TravelTime(Some(0))

  implicit val travelTimeOrdering: Ordering[TravelTime] = (x: TravelTime, y: TravelTime) =>
    (x, y) match {
      case (TravelTime(None), TravelTime(None))     => 0
      case (TravelTime(Some(_)), TravelTime(None))  => -1
      case (TravelTime(None), TravelTime(Some(_)))  => 1
      case (TravelTime(xValue), TravelTime(yValue)) => xValue compare yValue
  }
}

/**
  * Directed connection between two stations.
  *
  * @param from       Starting station.
  * @param to         Destination station.
  * @param travelTime Travel time from starting to destination.
  */
case class Connection(from: Station, to: Station, travelTime: TravelTime)

/**
  * Represents travel time to station.
  *
  * @param station  Destination station from start (source).
  * @param time     Time taken to reach station.
  */
case class StationTravelTime(station: Station, time: TravelTime)

object StationTravelTime {
  implicit val ordering: Ordering[StationTravelTime] =
    Ordering.by[StationTravelTime, TravelTime](_.time)
}

/**
 *
 * @param value
 */
case class VisitedFrom(value: Map[Station, Station]) extends AnyVal

/**
  * @param station
  * @param quickestTravelTimes
  * @param visitedFrom
  */
case class EvaluatedStation(station: Station,
                            quickestTravelTimes: Map[Station, TravelTime],
                            visitedFrom: Map[Station, Station])

sealed trait Query

object Query {

  /**
    * Represents route query
    * @param source      Source station
    * @param destination Destination station
    */
  case class Route(source: Station, destination: Station) extends Query

  /**
    * Represents nearby query
    * @param source            Source station
    * @param maximumTravelTime Maximum travel time
    */
  case class Nearby(source: Station, maximumTravelTime: TravelTime) extends Query
}

sealed trait Result

object Result {

  /**
    *
    * @param quickestTravelTimes
    */
  case class RouteFound(quickestTravelTimes: Map[Station, TravelTime]) extends Result
  case object NotRouteFound extends Result

  /**
    *
    * @param stations
    */
  case class NearbyStations(stations: List[(Station, TravelTime)]) extends Result
}
