FROM openjdk:8 AS sbt-builder

ENV SBT_VERSION 1.3.2
ENV SBT_HOME /usr/local/sbt
ENV PATH ${PATH}:${SBT_HOME}/bin

RUN curl -sL "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | tar xz -C /usr/local

RUN sbt "show sbtVersion"

COPY *.sbt /work_dir/
COPY .scalafmt.conf /work_dir/
COPY scalastyle-config.xml /work_dir/

COPY project/*.properties /work_dir/project/

# source code
COPY src /work_dir/src

WORKDIR /work_dir/

RUN sbt compile