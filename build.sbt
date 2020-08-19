val Http4sVersion        = "0.21.5"
val CirceVersion         = "0.13.0"
val CirceRefinedVersion  = "0.9.3"
val ScalaCheckVersion    = "1.14.3"
val ScalaTestVersion     = "3.2.1"
val ScalaTestPlusVersion = "3.2.1.0"
val LogbackVersion       = "1.2.3"
val NewtypeVersion       = "0.4.4"
val RefinedVersion       = "0.9.15"
val Http4sJwtVersion     = "0.0.5"
val CirisVersion         = "1.1.2"

lazy val root = (project in file("."))
  .settings(
    organization := "dev.odealva",
    name := "chat-http4s",
    version := "0.3.2-SNAPSHOT",
    scalaVersion := "2.13.2",
    libraryDependencies ++= Seq(
      "org.http4s"        %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"        %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"        %% "http4s-circe"        % Http4sVersion,
      "org.http4s"        %% "http4s-dsl"          % Http4sVersion,
      "dev.profunktor"    %% "http4s-jwt-auth"     % Http4sJwtVersion,
      "io.circe"          %% "circe-core"          % CirceVersion,
      "io.circe"          %% "circe-generic"       % CirceVersion,
      "io.circe"          %% "circe-parser"        % CirceVersion,
      "io.circe"          %% "circe-refined"       % CirceVersion,
      "is.cir"            %% "ciris"               % CirisVersion,
      "is.cir"            %% "ciris-enumeratum"    % CirisVersion,
      "is.cir"            %% "ciris-refined"       % CirisVersion,
      "eu.timepit"        %% "refined"             % RefinedVersion,
      "eu.timepit"        %% "refined-cats"        % RefinedVersion,
      "io.estatico"       %% "newtype"             % NewtypeVersion,
      "org.scalacheck"    %% "scalacheck"          % ScalaCheckVersion    % Test,
      "org.scalatest"     %% "scalatest"           % ScalaTestVersion     % Test,
      "org.scalatestplus" %% "scalacheck-1-14"     % ScalaTestPlusVersion % Test,
      "ch.qos.logback"     % "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)
