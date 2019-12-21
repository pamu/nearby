package com.transportrouter

import com.transportrouter.domain.{Connection, Query, Result, Station, TravelTime}
import com.transportrouter.logic.QueryHandler

import scala.io.StdIn
import scala.collection.mutable

object Main {

  def main(args: Array[String]): Unit = {
    implicit val handler: QueryHandler = QueryHandler(allConnections(StdIn.readLine().trim.toInt))
    withLines(line => println(handleQueryLine(line)))
  }

  def handleQueryLine(line: String)(implicit handler: QueryHandler): Result =
    handler.handle(parseQuery(line.trim))

  def parseConnection(line: String): Connection = {
    val Array(fromTo, time) = line.split(":").map(_.trim)
    val Array(from, to) = fromTo.split("->").map(_.trim)
    Connection(Station(from), Station(to), TravelTime(BigDecimal(time)))
  }

  def parseQuery(line: String): Query = {
    val parts = line.split("\\s+").map(_.trim).filterNot(_.isEmpty)
    parts(0) match {
      case "route" => Query.Route(Station(parts(1)), Station(parts(3)))
      case "nearby" => Query.Nearby(Station(parts(1).split(",")(0)), TravelTime(Some(BigDecimal(parts(2)))))
    }
  }

  def allConnections(count: Int, source: () => String = () => StdIn.readLine()): List[Connection] = {
    val connections = mutable.ListBuffer.empty[Connection]
    for (_ <- 0 until count) connections += parseConnection(source().trim)
    connections.toList
  }

  // Default source for lines is StdIn but, but any other source can be injected.
  def withLines(lineFn: String => Unit, source: () => String = () => StdIn.readLine()): Unit = {
    var line: Option[String] = None
    while ( {
      line = Option(source()).filterNot(_.trim.isEmpty);
      line.isDefined
    }) lineFn(line.get)
  }
}
