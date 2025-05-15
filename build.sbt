import com.typesafe.tools.mima.core._
import org.typelevel.sbt.gha.WorkflowStep.Run
import org.typelevel.sbt.gha.WorkflowStep.Sbt

val Scala213 = "2.13.16"
val Scala3 = "3.3.6"

ThisBuild / tlBaseVersion := "0.5"
ThisBuild / crossScalaVersions := Seq(Scala213, Scala3)
ThisBuild / tlVersionIntroduced := Map("3" -> "0.4.3")
ThisBuild / developers += tlGitHubDev("ChristopherDavenport", "Christopher Davenport")
ThisBuild / startYear := Some(2019)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / tlSiteApiUrl := Some(url("https://www.javadoc.io/doc/org.typelevel/keypool_2.12"))

ThisBuild / githubOwner := "igor-ramazanov-typelevel"
ThisBuild / githubRepository := "keypool"

ThisBuild / githubWorkflowPublishPreamble := List.empty
ThisBuild / githubWorkflowUseSbtThinClient := true
ThisBuild / githubWorkflowPublish := List(
  Run(
    commands = List("echo \"$PGP_SECRET\" | gpg --import"),
    id = None,
    name = Some("Import PGP key"),
    env = Map("PGP_SECRET" -> "${{ secrets.PGP_SECRET }}"),
    params = Map(),
    timeoutMinutes = None,
    workingDirectory = None
  ),
  Sbt(
    commands = List("+ publish"),
    id = None,
    name = Some("Publish"),
    cond = None,
    env = Map("GITHUB_TOKEN" -> "${{ secrets.GB_TOKEN }}"),
    params = Map.empty,
    timeoutMinutes = None,
    preamble = true
  )
)
ThisBuild / gpgWarnOnFailure := false

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "keypool",
    mimaPreviousArtifacts ~= { _.filterNot(_.revision == "0.4.4") },
    publishTo := githubPublishTo.value,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
  )
  .jsSettings(
    tlVersionIntroduced := List("2.13", "3").map(_ -> "0.4.6").toMap
  )
  .nativeSettings(
    tlVersionIntroduced := List("2.13", "3").map(_ -> "0.4.8").toMap
  )
  .settings(
    mimaBinaryIssueFilters ++= Seq(
      ProblemFilters.exclude[DirectMissingMethodProblem]("org.typelevel.keypool.KeyPool.destroy"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("org.typelevel.keypool.KeyPool.reap"),
      ProblemFilters.exclude[DirectMissingMethodProblem]("org.typelevel.keypool.KeyPool.put"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("org.typelevel.keypool.KeyPool#KeyPoolConcrete.this"),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "org.typelevel.keypool.KeyPool#KeyPoolConcrete.kpCreate"
      ),
      ProblemFilters.exclude[DirectMissingMethodProblem](
        "org.typelevel.keypool.KeyPool#KeyPoolConcrete.kpDestroy"
      ),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("org.typelevel.keypool.KeyPoolBuilder.this"),
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("org.typelevel.keypool.KeyPool#Builder.this"),
      // Introduced by #561, add durationBetweenEvictionRuns
      ProblemFilters
        .exclude[DirectMissingMethodProblem]("org.typelevel.keypool.Pool#Builder.this")
    )
  )

val catsV = "2.13.0"
val catsEffectV = "3.7-4972921"

val munitV = "1.1.1"
val munitCatsEffectV = "2.2.0-M1"

val kindProjectorV = "0.13.3"
val betterMonadicForV = "0.3.1"

// General Settings
lazy val commonSettings = Seq(
  Test / parallelExecution := false,
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"           % catsV,
    "org.typelevel" %%% "cats-effect-std"     % catsEffectV,
    "org.typelevel" %%% "cats-effect-testkit" % catsEffectV      % Test,
    "org.scalameta" %%% "munit"               % munitV           % Test,
    "org.typelevel" %%% "munit-cats-effect"   % munitCatsEffectV % Test
  )
)
