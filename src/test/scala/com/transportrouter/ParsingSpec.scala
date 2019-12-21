package com.transportrouter

import com.transportrouter.domain.{Connection, Query, Station, TravelTime}

class ParsingSpec extends WordSpecWithMatchers {

  "Parse connection correctly" in {
    Main.parseConnection("A -> E: 123.33") mustEqual
      Connection(Station("A"), Station("E"), TravelTime(123.33))
  }

  "Parse route query correctly" in {
    Main.parseQuery("route A -> E") mustEqual
      Query.Route(Station("A"), Station("E"))
  }

  "Parse nearby query correctly" in {
    Main.parseQuery("nearby A, 123.22") mustEqual
      Query.Nearby(Station("A"), TravelTime(123.22))
  }

}
