
name := "yang-backend-template"

scalaVersion := "2.12.8"

scalaSource in Compile := baseDirectory.value / "src"
resourceDirectory in Compile := baseDirectory.value / "conf"
scalaSource in Test := baseDirectory.value / "test"
scalaSource in IntegrationTest := baseDirectory.value / "it"

parallelExecution in Test := false
parallelExecution in IntegrationTest := false

fork in Test := true
fork in IntegrationTest := true

val akkaVersion     = "2.5.19"
val akkaHttpVersion = "10.1.5"

libraryDependencies ++= {
  Seq(
    "com.google.inject" % "guice" % "4.2.2",
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.google.inject" % "guice" % "4.2.2",
    "org.typelevel" %% "cats-core" % "1.5.0",
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test, it",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test,
    "com.github.tomakehurst" % "wiremock" % "2.17.0" % IntegrationTest
  )
}


dependencyUpdatesFailBuild := true

scalastyleFailOnError := true

scalacOptions ++= Seq("-unchecked",
  "-deprecation",
  "-feature",
  "-encoding","UTF-8",
  "-explaintypes",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-Ywarn-infer-any",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates"
)

coverageEnabled in (Test, compile) := true
coverageEnabled in (IntegrationTest, compile) := true
coverageEnabled in (Compile, compile) := false
coverageMinimum := 1
coverageFailOnMinimum := true
coverageHighlighting := true

assemblyJarName in assembly := "yang-backend-template.jar"

lazy val root = (project in file(".")).configs(IntegrationTest).settings(Defaults.itSettings: _*)
