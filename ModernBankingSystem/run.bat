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
  echo.
  echo ERROR: Java runtime not found in PATH. Please install Java 8+ and try again.
  echo Visit https://adoptium.net/ for installers.
  pause
  exit /b 1
)

if "%DEV_MODE%"=="1" (
  "%JAVA_CMD%" -cp "%CLASSPATH%" com.banking.Main
) else (
  start "" "%JAVA_CMD%" -cp "%CLASSPATH%" com.banking.Main
)

endlocal
exit /b 0
