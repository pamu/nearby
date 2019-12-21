package com.transportrouter.logic

import com.transportrouter.domain.{Station, VisitedFrom}

object Implicits {

  implicit class VisitFromOps(visitHistory: VisitedFrom) {

    /**
      * Create path from visited from map.
      *
      * @param to Destination station to which path has to be created.
      * @return Path (route)
      *         Warning: to (destination station) must be reachable station from source station.
      */
    def path(to: Station): List[Station] = {

      @scala.annotation.tailrec
      def goUntilStart(path: List[Station]): List[Station] = {
        val head = path.head
        visitHistory.visitedFrom(head) match {
          case None => Nil
          case Some(value) if value == head => path
          case Some(value) => goUntilStart(value :: path)
        }
      }

      goUntilStart(to :: Nil)
    }
  }

}
