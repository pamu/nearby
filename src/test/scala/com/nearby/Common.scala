package com.nearby

import com.nearby.domain.{Connection, Station, TravelTime}

object Common {

  // Example taken from https://algs4.cs.princeton.edu/lectures/44DemoDijkstra.pdf
  val connections = List(
    Connection(Station("0"), Station("1"), TravelTime(5)),
    Connection(Station("0"), Station("7"), TravelTime(8)),
    Connection(Station("0"), Station("4"), TravelTime(9)),
    Connection(Station("1"), Station("3"), TravelTime(15)),
    Connection(Station("1"), Station("2"), TravelTime(12)),
    Connection(Station("1"), Station("7"), TravelTime(4)),
    Connection(Station("4"), Station("7"), TravelTime(5)),
    Connection(Station("4"), Station("5"), TravelTime(4)),
    Connection(Station("4"), Station("6"), TravelTime(20)),
    Connection(Station("7"), Station("2"), TravelTime(7)),
    Connection(Station("7"), Station("5"), TravelTime(6)),
    Connection(Station("2"), Station("3"), TravelTime(3)),
    Connection(Station("2"), Station("6"), TravelTime(11)),
    Connection(Station("5"), Station("2"), TravelTime(1)),
    Connection(Station("5"), Station("6"), TravelTime(13))
  )

}
