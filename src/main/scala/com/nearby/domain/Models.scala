package com.nearby.domain

/**
  * Represents station.
  * Wrapper type for type-safety.
  * https://docs.scala-lang.org/overviews/core/value-classes.html
  *
  * @param name Name identifier of the station.
  */
case class Station(name: String) extends AnyVal {
  override def toString: String = name
}

/**
  * Represents travel time.
  * None represents practically infinite time.
  * Why use BigDecimal? (https://stackoverflow.com/questions/3413448/double-vs-bigdecimal)
  *
  * @param value Time to travel.
  */
case class TravelTime(value: Option[BigDecimal]) {
  if (value.isDefined)
    require(value.get >= 0, "Time must be zero or positive")

  def plus(other: TravelTime): TravelTime = (value, other.value) match {
    case (Some(x), Some(y)) => TravelTime(x + y)
    case (_, _)             => TravelTime(None)
  }

  override def toString: String = value match {
    case Some(value) => s"$value"
    case None => "Inf"
  }
}

object TravelTime {

  val Inf: TravelTime = TravelTime(None)
  val Zero: TravelTime = TravelTime(0)

  implicit val travelTimeOrdering: Ordering[TravelTime] = (x: TravelTime, y: TravelTime) =>
    (x, y) match {
      case (TravelTime(None), TravelTime(None))     => 0
      case (TravelTime(Some(_)), TravelTime(None))  => -1
      case (TravelTime(None), TravelTime(Some(_)))  => 1
      case (TravelTime(xValue), TravelTime(yValue)) => xValue compare yValue
  }

  def apply(value: BigDecimal): TravelTime = TravelTime(Some(value))
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
  * Represents visiting order.
  * Map whose key and value are stations.
  * Key station is visited after visiting value value station.
  *
  * @param value  Map representing visiting order.
  */
case class VisitedFrom(value: Map[Station, Station]) extends AnyVal {
  def visitedFrom(station: Station): Option[Station] = value.get(station)
}

/**
  * Represents quickest travel times to all stations.
  * Also has visit history for each station to get the quickest travel time.
  *
  * @param source               Station from which travel time to all other stations is computed.
  * @param quickestTravelTimes  Quickest travel times to all stations from source station.
  * @param visitedFrom          Station from which each station is visited.
  */
case class QuickestTravelTimesToAllStations(source: Station,
                                            quickestTravelTimes: Map[Station, TravelTime],
                                            visitedFrom: VisitedFrom)

sealed trait Query

object Query {

  /**
    * Represents route query.
    *
    * @param source      Source station
    * @param destination Destination station
    */
  case class Route(source: Station, destination: Station) extends Query

  /**
    * Represents nearby query.
    *
    * @param source            Source station
    * @param maximumTravelTime Maximum travel time
    */
  case class Nearby(source: Station, maximumTravelTime: TravelTime) extends Query
}

sealed trait Result

object Result {

  /**
    * Represents route from source to destination with travel time.
    *
    * @param route      List of stations visited in order.
    * @param travelTime Travel time.
    */
  case class RouteFound(route: List[Station], travelTime: TravelTime) extends Result {
    override def toString: String = s"${route.mkString(" -> ")}: $travelTime"
  }

  /**
    * Represents not existent route.
    *
    * @param source      Source station.
    * @param destination Destination station to which route is requested.
    */
  case class RouteNotFound(source: Station, destination: Station) extends Result {
    override def toString: String = s"Error: No route from $source to $destination"
  }

  /**
    * Nearby stations sorted by travel time from source station.
    *
    * @param stations  Stations list sorted by travel time from source station.
    */
  case class NearbyStations(stations: List[(Station, TravelTime)]) extends Result {
    override def toString: String =
      stations.map { case (station, travelTime) => s"$station: $travelTime"}.mkString(", ")
  }
}
