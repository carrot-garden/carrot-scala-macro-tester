import sbt._
import Keys._

object BuildSettings {
  /** Must match scala version used by eclipse scala ide. */
  val ScalaVersion = "2.11.5"
  val ParadiseVersion = "2.1.0-SNAPSHOT"
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.carrotgarden.scala",
    version := "0.0.0",
    scalacOptions ++= Seq("-feature", "-deprecation"),
    scalaVersion := ScalaVersion,
    crossScalaVersions := Seq(ScalaVersion),
    resolvers += Resolver.sonatypeRepo("releases"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    addCompilerPlugin("org.scalamacros" % "paradise" % ParadiseVersion cross CrossVersion.full)
  )
}

object MacroBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "carrot-scala-macro-tester", file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in schema
    )
  ) aggregate (macros, schema)

  lazy val macros: Project = Project(
    "carrot-scala-macro-tester-macros", file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)
    )
  )

  lazy val schema: Project = Project(
    "carrot-scala-macro-tester-schema", file("schema"),
    settings = buildSettings
  ) dependsOn (macros)
}
