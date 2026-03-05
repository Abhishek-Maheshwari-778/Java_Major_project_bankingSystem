@echo off
setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
pushd "%SCRIPT_DIR%" >nul

set "DEV_MODE=0"
if exist "%SCRIPT_DIR%..\ModernBankingSystem\bin" (
  set "DEV_MODE=1"
)

if "%DEV_MODE%"=="1" (
  cd /d "%SCRIPT_DIR%.." >nul
  set "CLASSPATH=ModernBankingSystem\bin;ModernBankingSystem\lib\*"
  if not exist "ModernBankingSystem\data" mkdir "ModernBankingSystem\data"
) else (
  set "CLASSPATH=bin;lib\*"
  if not exist "ModernBankingSystem\data" mkdir "ModernBankingSystem\data"
  if not exist "data" mkdir "data"
)

set "JAVA_CMD=javaw"
where javaw >nul 2>&1
if errorlevel 1 set "JAVA_CMD=java"
where %JAVA_CMD% >nul 2>&1
if errorlevel 1 (
  set "JAVA_CMD="
  if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\javaw.exe" set "JAVA_CMD=%JAVA_HOME%\bin\javaw.exe"
    if not defined JAVA_CMD if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
  )
  if not defined JAVA_CMD (
    for /d %%D in ("C:\Program Files\Java\jre*") do (
      if exist "%%D\bin\javaw.exe" set "JAVA_CMD=%%D\bin\javaw.exe"
      if not defined JAVA_CMD if exist "%%D\bin\java.exe" set "JAVA_CMD=%%D\bin\java.exe"
    )
  )
  if not defined JAVA_CMD (
    for /d %%D in ("C:\Program Files\Java\jdk*") do (
      if exist "%%D\bin\javaw.exe" set "JAVA_CMD=%%D\bin\javaw.exe"
      if not defined JAVA_CMD if exist "%%D\bin\java.exe" set "JAVA_CMD=%%D\bin\java.exe"
    )
  )
  if not defined JAVA_CMD (
    for /d %%D in ("C:\Program Files (x86)\Java\jre*") do (
      if exist "%%D\bin\javaw.exe" set "JAVA_CMD=%%D\bin\javaw.exe"
      if not defined JAVA_CMD if exist "%%D\bin\java.exe" set "JAVA_CMD=%%D\bin\java.exe"
    )
  )
  if not defined JAVA_CMD (
    echo.
    echo ERROR: Java runtime not found. Install Java 8+ or set JAVA_HOME.
    echo Example path: C:\Program Files\Java\jre1.8.0_xxx\bin
    pause
    exit /b 1
  )
)

if "%DEV_MODE%"=="1" (
  "%JAVA_CMD%" -cp "%CLASSPATH%" com.banking.Main
) else (
  start "" "%JAVA_CMD%" -cp "%CLASSPATH%" com.banking.Main
)

endlocal
exit /b 0
