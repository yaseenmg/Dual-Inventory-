# Source Status

The project source, Forge metadata, Gradle build files, wrapper scripts, wrapper JAR, and resources are stored at the repository root.

- Java source: `src/main/java/com/example/dualinventory/`
- Forge resources: `src/main/resources/`
- Gradle wrapper: `gradle/wrapper/`
- Target: Minecraft 1.20.1, Forge 47.4.10, Java 17

This repository update moves the project out of the former `dualinv/` subdirectory and keeps its project files at the repository root. Compilation and in-game behavior should be checked against the current GitHub Actions result and with Minecraft before release.
