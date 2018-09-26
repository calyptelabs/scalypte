@echo off
rem -------------------------------------------------------------------------
rem Calypte Bootstrap App Script for Windows
rem -------------------------------------------------------------------------

rem $Id$

set "APP_JAR=setup.jar"

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT" setlocal

if "%OS%" == "Windows_NT" (
  set "DIRNAME=%~dp0%"
) else (
  set DIRNAME=.\
)

if "x%APP_HOME%" == "x" (
  set "APP_HOME=%DIRNAME%"
)

set DIRNAME=
set "PROGNAME=%~nx0%"

rem Setup App specific properties
set JAVA_OPTS=-Dprogram.name=%PROGNAME% %JAVA_OPTS%

if "x%JAVA_HOME%" == "x" (
  set JAVA=java
  rem check java installed
  where java 2>&1 | findstr /I /C:"java" > nul

  if errorlevel == 1 (
	echo The JRE not installed!
	choice /C YN /N /T 10 /D Y /M "Do you want to install it? (Y/N)"

	IF errorlevel 2 goto END
	IF errorlevel 1 goto END_URL
  )

) else (
  set "JAVA=%JAVA_HOME%\bin\java"
)

if exist "%APP_HOME%\%APP_JAR%" (
    set "RUNJAR=%APP_HOME%\%APP_JAR%"
) else (
  echo Could not locate "%APP_HOME%\%APP_JAR%".
  echo Please check that you are in the bin directory when running this script.
  goto END
)

if "x%APP_BASE_DIR%" == "x" (
  set  "APP_BASE_DIR=%APP_HOME%"
)

if "x%APP_LOG_DIR%" == "x" (
  set  "APP_LOG_DIR=%APP_BASE_DIR%"
)

if "x%APP_CONFIG_DIR%" == "x" (
  set  "APP_CONFIG_DIR=%APP_BASE_DIR%"
)

start "" "%JAVA%w" %JAVA_OPTS% "-Dapp.log.file=%APP_LOG_DIR%\info.log" ^
  "-Dlogging.configuration=file:%APP_CONFIG_DIR%/logging.properties" ^
  -jar "%APP_HOME%\%APP_JAR%" -Dapp.home.dir="%APP_HOME%" %*

goto END

:END_URL

start http://www.java.com/en/download/manual.jsp#win
goto END

:END