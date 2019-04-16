// SparkSQL can work with a Spark built with Scala 2.11 too.
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.4.0" % "provided",
  "com.thesamet.scalapb" %% "sparksql-scalapb" % "0.8.0",
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)
assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}
// Hadoop contains an old protobuf runtime that is not binary compatible
// with 3.0.0.  We shared ours to prevent runtime issues.
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value,
  scalapb.UdtGenerator -> (sourceManaged in Compile).value
)

