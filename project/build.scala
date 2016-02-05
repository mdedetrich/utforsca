import sbt._
import Keys._

object MacroBuild extends Build {
  val Name = "utforsca"
  val Organization = "org.mdedetrich"
  val Version = "2.2.0"
  val StartYear = Some(2015)
  val ScalaVersion = "2.11.7"
  val CrossScalaVersions = Seq("2.11.7")

  lazy val main = Project(Name, file("."), settings = Seq(
    organization := Organization,
    name := Name,
    version := Version,
    scalaVersion := ScalaVersion,
    crossScalaVersions := CrossScalaVersions,
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := <url>https://github.com/mdedetrich/utforsca</url>
      <licenses>
        <license>
          <name>BSD-style</name>
          <url>http://www.opensource.org/licenses/bsd-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:mdedetrich/utforsca.git</url>
        <connection>scm:git:git@github.com:mdedetrich/utforsca.git</connection>
      </scm>
      <developers>
        <developer>
          <id>mdedetrich</id>
          <name>Matthew de Detrich</name>
          <email>mdedetrich@gmail.com</email>
        </developer>
      </developers>,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.1.5" % "test",
      "org.spire-math" %% "spire" % "0.8.2" % "test"
    )
  )
  ) dependsOn (macroSub % "compile-internal;test") settings(
    // include the macro classes and resources in the main jar

    mappings in(Compile, packageBin) ++= mappings.in(macroSub, Compile, packageBin).value,

    // include the macro sources in the main source jar
    mappings in(Compile, packageSrc) ++= mappings.in(macroSub, Compile, packageSrc).value

    )

  lazy val macroSub = Project(Name + "-macros", file("macro")) settings(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    publish := {},
    publishLocal := {},
    scalaVersion := ScalaVersion,
    crossScalaVersions := CrossScalaVersions,
    version := Version,
    scalacOptions := Seq(
      "-deprecation"
    )
    )
}