// SparkSQL can work with a Spark built with Scala 2.11 too.

import scalapb.compiler.Version.{grpcJavaVersion, scalapbVersion, protobufVersion}
lazy val consolelog = taskKey[Unit]("Prints 'Hello World'")

consolelog := println(grpcJavaVersion, scalapbVersion, protobufVersion)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.1" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.4.1" % "provided",
  "io.grpc" % "grpc-netty" % grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion
)


assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}
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

// assemblyMergeStrategy in assembly := {
//   case "META-INF/io.netty.versions.properties" =>
//     MergeStrategy.first
//   case x if x.contains ("META-INF/maven/") =>
//     MergeStrategy.first
//   // case "ahc-default.properties" =>
//   //   ahcMerge
//   case x =>
//     val oldStrategy = (assemblyMergeStrategy in assembly).value
//     oldStrategy(x)
// }
// assemblyMergeStrategy in assembly := {
//   case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
//   case x =>
//     val oldStrategy = (assemblyMergeStrategy in assembly).value
//     oldStrategy(x)
// }
