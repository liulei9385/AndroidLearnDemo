@echo off
if "%OS%"=="Windows_NT" setlocal
set CURR_DIR=%cd%
set DIRNAME=%~dp0
@rem set ORI_JAVA_HOME=%JAVA_HOME%
@rem echo ORI_JAVA_HOME = %ORI_JAVA_HOME%
set JAVA_HOME=c:\Program Files\Java\jdk1.8.0_231
cd /d d:\studio_projects\ShaoManBApp
gradlew.bat app:assembleDebug
@rem set JAVA_HOME=%ORI_JAVA_HOME%
@rem echo JAVA_HOME = %JAVA_HOME%
cd /d %DIRNAME%
if "%OS%"=="Windows_NT" endlocal
