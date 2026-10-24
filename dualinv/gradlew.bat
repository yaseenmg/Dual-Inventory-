@echo off
setlocal
set "APP_HOME=%~dp0"
set "CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar"
where java >NUL 2>&1
if errorlevel 1 (
  echo ERROR: Java was not found. Install Java 17+ and ensure java is on PATH.
  exit /b 1
)
if not exist "%CLASSPATH%" (
  echo ERROR: gradle\wrapper\gradle-wrapper.jar is missing.
  echo The wrapper JAR must be present before gradlew.bat can run.
  exit /b 1
)
java -Xmx64m -Xms64m -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
exit /b %ERRORLEVEL%
