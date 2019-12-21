# Transport router

Transport router uses [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) to answer
route and nearby queries.

# Summary

- Query ADT is used to represent (Route and Nearby queries).

- Result ADT represents corresponding results for above queries.

- Connection (edges) and queries are read from Stdin and sent to QueryHandler.

- QueryHandler uses QuickestTravelTimeFinder (which internally uses Dijkstra's algorithm)
to convert Query into Result.

- `toString` method on Result gives output in correct format which is
printed to Stdout.

# Testing library

- Scala test

# Code structure

## Main

Entry point to the application. Main also has parsing logic. 

Main passes parsed query to QueryHandler for results.

## Domain models

Station, TravelTime, Query, Result etc are declared here.


## Logic

##### QueryHandler

Handles query and also caches intermediate results for answering future queries.


##### QuickestTravelTimeFinder

Logic for computing quickest travel times.

# Dataflow

Input:
```
[Stdin] ---->Edges + Queries----> [Main] ---->Queries----> [QueryHandler] -----> [QuickestTravelTimeFinder]
```

Output:
```
[Stdout] <----Result String<---- [Main] <----Result<---- [QueryHandler] <----- [QuickestTravelTimeFinder]
```

##### Main

- Main parses input lines into connections and queries.

- Connections are used by query handler to answer queries.

- Queries are also passed to query handler for results.

##### QueryHandler

- QueryHandler handles queries and returns results.

- Results are computed using QuickestTravelTimeFinder instance.

- Intermediate results are then cached for addressing future queries.

##### QuickestTravelTimeFinder

Logic to compute quickest time from one station to all other stations.

# SBT

## Run

```
[transport-router] sbt run < data/input.txt                                                                          master  ✭
[info] Loading project definition from /Users/pnagarjuna/Documents/transport-router/project
[info] Loading settings for project transport-router from build.sbt ...
[info] Set current project to transport-router (in build file:/Users/pnagarjuna/Documents/transport-router/)
[info] Compiling 5 Scala sources to /Users/pnagarjuna/Documents/transport-router/target/scala-2.13/classes ...
[info] Done compiling.
[info] running com.transportrouter.Main
A -> C -> B: 130
C: 70, D: 120, B: 130
[success] Total time: 16 s, completed 21 Dec, 2019 10:20:36 PM
```

## Test

```
[transport-router] sbt test                                                                                          master  ✭
[info] Loading project definition from /Users/pnagarjuna/Documents/transport-router/project
[info] Loading settings for project transport-router from build.sbt ...
[info] Set current project to transport-router (in build file:/Users/pnagarjuna/Documents/transport-router/)
[info] Compiling 6 Scala sources to /Users/pnagarjuna/Documents/transport-router/target/scala-2.13/test-classes ...
[info] RouteSpec:
[info] - Return source station if source path is requested
[info] - Return correct path when path is requested using terminal station
[info] - Return correct path when path is requested using intermediate station
[info] - Return empty path when path is request using unknown station
[info] QueryHandlerSpec:
[info] - No stations within zero travel time
[info] - No route to unknown station
[info] - No route to unreachable station
[info] - Return correct result for route query
[info] - Return correct result for nearby query
[info] - Return correct results for given test data
[info] ParsingSpec:
[info] - Parse connection correctly
[info] - Parse route query correctly
[info] - Parse nearby query correctly
[info] QuickestTravelTimeFinderSpec:
[info] - Empty connections must produce empty results
[info] - Finder must produce correct results for one connection
[info] - Finder must produce correct results for known graph
[info] - Travel time to disconnected station should be Inf
[info] Run completed in 1 second, 210 milliseconds.
[info] Total number of tests run: 17
[info] Suites: completed 4, aborted 0
[info] Tests: succeeded 17, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 18 s, completed 21 Dec, 2019 10:21:33 PM
```


# Docker

## Build

```
docker build -t transport-router .
```

## Run

```
cat data/input.txt | docker run -i transport-router:latest sbt run
```

Output:

```
[transport-router] cat data/input.txt | docker run -i transport-router:latest sbt run                                master  ✭

[info] Loading project definition from /work_dir/project
[info] Loading settings for project work_dir from build.sbt ...
[info] Set current project to transport-router (in build file:/work_dir/)
[info] Compiling 5 Scala sources to /work_dir/target/scala-2.13/classes ...
[info] Done compiling.
[info] running com.transportrouter.Main
A -> C -> B: 130
C: 70, D: 120, B: 130
[success] Total time: 15 s, completed Dec 21, 2019 9:27:33 PM
[transport-router]
```