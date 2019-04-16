// SparkSQL can work with a Spark built with Scala 2.11 too.

import scalapb.compiler.Version.{grpcJavaVersion, scalapbVersion, protobufVersion}
scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.2.0" % "provided",
  "io.grpc" % "grpc-netty" % grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion
)

// Hadoop contains an old protobuf runtime that is not binary compatible
// with 3.0.0.  We shared ours to prevent runtime issues.
// might need to look more into this--- [MK]
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value,
  scalapb.UdtGenerator -> (sourceManaged in Compile).value
)

