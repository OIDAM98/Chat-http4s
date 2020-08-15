val Http4sVersion        = "0.21.5"
val CirceVersion         = "0.13.0"
val ScalaCheckVersion    = "1.14.3"
val ScalaTestVersion     = "3.2.1"
val ScalaTestPlusVersion = "3.2.1.0"
val LogbackVersion       = "1.2.3"

lazy val sharedSettings = Seq(
  organization := "dev.odealva",
  scalaVersion := "2.13.2",
  version := "0.3.1",
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core"    % CirceVersion,
    "io.circe" %% "circe-generic" % CirceVersion,
    "io.circe" %% "circe-parser"  % CirceVersion
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-language:higherKinds",
    "-language:postfixOps",
    "-feature",
    "-Xfatal-warnings"
  )
)

lazy val backend = (project in file("backend"))
  .settings(sharedSettings)
  .settings(
    name := "chat-http4s",
    libraryDependencies ++= Seq(
      "org.http4s"        %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"        %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"        %% "http4s-circe"        % Http4sVersion,
      "org.http4s"        %% "http4s-dsl"          % Http4sVersion,
      "org.scalacheck"    %% "scalacheck"          % ScalaCheckVersion    % Test,
      "org.scalatest"     %% "scalatest"           % ScalaTestVersion     % Test,
      "org.scalatestplus" %% "scalacheck-1-14"     % ScalaTestPlusVersion % Test,
      "ch.qos.logback"     % "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    mainClass in reStart := Some("chat.Main"),
    mainClass in (Compile, run) := Some("chat.Main")
  )
