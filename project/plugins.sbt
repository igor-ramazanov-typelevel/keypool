val sbtTypelevelVersion = "0.7.7"

addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "1.18.2")
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.5.7")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")
addSbtPlugin("com.github.cb372"   % "sbt-explicit-dependencies"     % "0.3.1")
addSbtPlugin("org.typelevel"      % "sbt-typelevel"                 % sbtTypelevelVersion)
addSbtPlugin("org.typelevel"      % "sbt-typelevel-site"            % sbtTypelevelVersion)

addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.5.3")
