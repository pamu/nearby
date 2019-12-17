package com.nearby

import com.nearby.domain.{Connection, Query, Station, TravelTime}

import scala.io.StdIn
import scala.collection.mutable

object Main extends App {

  private var line: Option[String] = None
  val lineCount = StdIn.readLine().trim().toInt

  val connections = mutable.ListBuffer.empty[Connection]

  (1 to lineCount).foreach { _ =>
    line = Option(StdIn.readLine())
    line.foreach { edgeLine =>
      connections += parseEdge(edgeLine)
    }
  }

  while ({ line = Option(StdIn.readLine()); line.isDefined }) {

  }

  def parseEdge(line: String): Connection = {
    val Array(fromTo, time) = line.split(":").map(_.trim)
    val Array(from, to) = fromTo.split("->").map(_.trim)
    Connection(Station(from), Station(to), TravelTime(Some(BigDecimal(time))))
  }

  def parseQuery(line: String): Query = {
    val parts = line.split("\\s+").map(_.trim).filterNot(_.isEmpty)
    parts(0) match {
      case "route"  => Query.Route(Station(parts(1)), Station(parts(3)))
      case "nearby" => Query.Nearby(Station(parts(1).split(",")(0)), TravelTime(Some(BigDecimal(parts(2)))))
    }
  }

}
