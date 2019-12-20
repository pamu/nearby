# nearby
Near by



# Docker

## Build

```
 docker build -t nearby .
```

## Run

```
cat data/input.txt | docker run -i nearby:latest sbt run
```

Output:

```
[nearby] cat data/input.txt | docker run -i nearby:latest sbt run
[info] Loading project definition from /work_dir/project
[info] Loading settings for project work_dir from build.sbt ...
[info] Set current project to nearby (in build file:/work_dir/)
[info] Compiling 5 Scala sources to /work_dir/target/scala-2.13/classes ...
[info] Done compiling.
[info] running com.nearby.Main
A -> C -> B : 130
C: 70 D: 120 B: 130
[success] Total time: 11 s, completed Dec 20, 2019 12:39:08 AM
[nearby]
```