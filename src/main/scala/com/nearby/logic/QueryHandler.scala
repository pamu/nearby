package com.nearby.logic

import com.nearby.domain.{Connection, EvaluatedStation, Query, Result, Station}

import scala.collection.mutable.{Map => MutableMap}

trait QueryHandler {
  def handleQuery(query: Query): Result
}

class QueryHandlerImpl(connections: List[Connection]) extends QueryHandler {

  private val finder = new QuickestTravelTimeFinder(connections)
  private val evaluatedStations: MutableMap[Station, EvaluatedStation] =
    MutableMap.empty

  override def handleQuery(query: Query): Result = query match {
    case Query.Route(source, destination)        => ???
    case Query.Nearby(source, maximumTravelTime) => ???
  }

}

object QueryHandler {
  def apply(connections: List[Connection]): QueryHandler = new QueryHandlerImpl(connections)
}
