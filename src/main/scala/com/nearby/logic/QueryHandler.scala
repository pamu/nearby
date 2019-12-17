package com.nearby.logic

import com.nearby.domain.{Connection, Query}

trait QueryHandler {
  def handleQuery(query: Query): String
}

class QueryHandlerImpl(connections: List[Connection]) extends QueryHandler {
  override def handleQuery(query: Query): String = {
    ???
  }
}

object QueryHandler {
  def apply(connections: List[Connection]): QueryHandler = new QueryHandlerImpl(connections)
}
