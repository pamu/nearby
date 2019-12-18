package com.nearby.logic

import com.nearby.domain.{Station, VisitedFrom}

object Implicits {

  implicit class VisitFromOps(visitedFrom: VisitedFrom) {

    def path(to: Station): List[Station] = {

      @scala.annotation.tailrec
      def goUntilStart(path: List[Station]): List[Station] =
        if (visitedFrom.value.getOrElse(path.head, path.head) != path.head)
          goUntilStart(visitedFrom.value(path.head) :: path)
        else path

      goUntilStart(to :: Nil)
    }
  }

}
