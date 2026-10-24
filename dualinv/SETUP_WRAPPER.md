# Restoring the Gradle Wrapper

The launcher scripts are checked in, but the binary wrapper JAR is not. To regenerate it:

1. Install a Java 17 JDK.
2. Install Gradle 8.8 (or run the repository's GitHub Actions build workflow, which installs Gradle for you).
3. Open a terminal in the `dualinv` directory.
4. Run:

   ```bash
   gradle wrapper --gradle-version 8.8
   ```

5. Confirm `gradle/wrapper/gradle-wrapper.jar` now exists, then run `./gradlew build` (Windows: `gradlew.bat build`).

This generates the wrapper JAR locally; it does not automatically upload it to GitHub. The first clean build may still reveal source or dependency errors, which must be fixed separately.
