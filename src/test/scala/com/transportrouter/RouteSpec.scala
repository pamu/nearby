package com.transportrouter

import com.transportrouter.domain.{Station, VisitedFrom}
import com.transportrouter.logic.Implicits._

class RouteSpec extends WordSpecWithMatchers {

  private val visitedFrom = VisitedFrom(
    Map(Station("E") -> Station("D"),
      Station("D") -> Station("C"),
      Station("C") -> Station("B"),
      Station("B") -> Station("A"),
      Station("A") -> Station("A"))
  )

  "Return source station if source path is requested" in {
    visitedFrom.path(Station("A")) must
      contain theSameElementsInOrderAs List(Station("A"))
  }

  "Return correct path when path is requested using terminal station" in {
    visitedFrom.path(Station("E")) must
      contain theSameElementsInOrderAs List(
      Station("A"),
      Station("B"),
      Station("C"),
      Station("D"),
      Station("E")
    )
  }

  "Return correct path when path is requested using intermediate station" in {
    visitedFrom.path(Station("C")) must
      contain theSameElementsInOrderAs List(
      Station("A"),
      Station("B"),
      Station("C")
    )
  }

  "Return empty path when path is request using unknown station" in {
    visitedFrom.path(Station("Unknown")) mustBe empty
  }
}
