import sbt._
import Keys._

object MacroBuild extends Build {
  val Name = "utforsca"
  val Organization = "com.mdedetrich"
  val Version = "1.0.1"
  val StartYear = Some(2014)
  val ScalaVersion = "2.11.0"
  val CrossScalaVersions = Seq("2.11.0","2.10.4")

  lazy val main = Project(Name, file("."), settings = Defaults.defaultSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      crossScalaVersions := CrossScalaVersions,
      libraryDependencies ++= Seq(
        "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test"
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
    version := Version
  )
}