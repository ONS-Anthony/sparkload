name := "Spark_loader"

version := "1.0"

scalaVersion := "2.11.8"

lazy val Versions = new {
  val scala = "2.11.11"
  val hbase = "1.3.1"
  val spark = "2.1.0"
}

libraryDependencies ++= Seq(
"de.unkrig.jdisasm"            %  "jdisasm"              % "1.0.0",
"org.apache.hbase"             %  "hbase-hadoop-compat"  % "1.2.1",
"org.apache.hbase"             %  "hbase-server"         % Versions.hbase,
"org.apache.hbase"             %  "hbase-common"         % Versions.hbase,
"org.apache.hbase"             %  "hbase-client"         % Versions.hbase,
"org.apache.spark"             %% "spark-core"           % Versions.spark,
"org.apache.spark"             %% "spark-sql"            % Versions.spark,
"io.swagger"                   %% "swagger-play2"        % "1.5.3",
"org.webjars"                  %  "swagger-ui"           % "2.2.10-1")