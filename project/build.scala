import sbt._
import Keys._

object MacroBuild extends Build {
  val Name = "utforsca"
  val Organization = "com.mdedetrich"
  val Version = "1.1.0"
  val StartYear = Some(2015)
  val ScalaVersion = "2.10.5"
  val CrossScalaVersions = Seq("2.10.5")

  lazy val main = Project(Name, file("."), settings = Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      crossScalaVersions := CrossScalaVersions,
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.1.5" % "test",
        "org.spire-math" %% "spire" % "0.8.2" % "test"
      )
    )
  ) dependsOn (macroSub % "compile-internal;test") settings(
    // include the macro classes and resources in the main jar

    mappings in (Compile, packageBin) ++= mappings.in(macroSub, Compile, packageBin).value,

    // include the macro sources in the main source jar
    mappings in (Compile, packageSrc) ++= mappings.in(macroSub, Compile, packageSrc).value

  )

  lazy val macroSub = Project(Name + "-macros", file("macro")) settings(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    publish := {},
    publishLocal := {},
    scalaVersion := ScalaVersion,
    crossScalaVersions := CrossScalaVersions,
    version := Version,
    scalacOptions:= Seq(
      "-deprecation"
    )
  )
}