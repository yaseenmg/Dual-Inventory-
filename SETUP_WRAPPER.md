# Gradle Wrapper Setup

The Gradle launcher scripts, wrapper configuration, and wrapper JAR are included in this repository.

## Build with the included wrapper

From the repository root, with Java 17 installed, run:

```bash
./gradlew build
```

On Windows, run:

```bat
gradlew.bat build
```

## Regenerate the wrapper if its JAR is missing

1. Install a Java 17 JDK and Gradle 8.8.
2. Open a terminal at the repository root.
3. Run:

   ```bash
   gradle wrapper --gradle-version 8.8 --distribution-type bin
   ```

4. Confirm that `gradle/wrapper/gradle-wrapper.jar` exists, then run the build command above.

Generating the wrapper locally does not publish the generated files to GitHub; commit them separately. A successful wrapper setup does not guarantee that the project compiles or runs correctly.
