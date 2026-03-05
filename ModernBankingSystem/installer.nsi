; NSIS Installer Script for Modern Banking System

!define APPNAME "Modern Banking System"
!define COMPANYNAME "Banking Corp"
!define DESCRIPTION "A modern banking system application"
!define VERSION "1.0.0"

; Main Install settings
Name "${APPNAME}"
InstallDir "$PROGRAMFILES\${APPNAME}"
OutFile "ModernBankingSystemInstaller.exe"
SetCompressor /SOLID lzma

; Request application privileges for Windows Vista and higher
RequestExecutionLevel admin

; Modern UI
!include "MUI2.nsh"
!include "LogicLib.nsh"
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

Var APPICON
Var JAVA_HOME_PATH
Var JAVA_REG_HOME

; Pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

; Languages
!insertmacro MUI_LANGUAGE "English"

Function .onInit
  ; Prefer an app icon if shipped next to the script as "app.ico"
  StrCpy $APPICON ""
  IfFileExists "$EXEDIR\app.ico" 0 +2
    StrCpy $APPICON "$EXEDIR\app.ico"

  ; Basic Java check (JRE/JDK) via registry and JAVA_HOME; runtime also checks in run.bat
  ; Try 64-bit registry view first, then 32-bit for compatibility
  StrCpy $JAVA_REG_HOME ""
  SetRegView 64
  ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ${If} $0 != ""
    ReadRegStr $JAVA_REG_HOME HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$0" "JavaHome"
  ${EndIf}
  ${If} $JAVA_REG_HOME == ""
    ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\JDK" "CurrentVersion"
    ${If} $0 != ""
      ReadRegStr $JAVA_REG_HOME HKLM "SOFTWARE\JavaSoft\JDK\$0" "JavaHome"
    ${EndIf}
  ${EndIf}
  ${If} $JAVA_REG_HOME == ""
    SetRegView 32
    ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ${If} $0 != ""
      ReadRegStr $JAVA_REG_HOME HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$0" "JavaHome"
    ${EndIf}
  ${EndIf}
  ${If} $JAVA_REG_HOME == ""
    ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\JDK" "CurrentVersion"
    ${If} $0 != ""
      ReadRegStr $JAVA_REG_HOME HKLM "SOFTWARE\JavaSoft\JDK\$0" "JavaHome"
    ${EndIf}
  ${EndIf}
  ReadEnvStr $JAVA_HOME_PATH "JAVA_HOME"
  ${If} $JAVA_REG_HOME == "" 
  ${AndIf} $JAVA_HOME_PATH == ""
    MessageBox MB_ICONEXCLAMATION|MB_OK "Java 8+ not detected in registry or JAVA_HOME.$\r$\nThe app also detects Java at runtime, but installation is smoother if Java is installed.$\r$\nYou can install a free OpenJDK from Adoptium."
  ${EndIf}
FunctionEnd

!define MUI_WELCOMEPAGE_TITLE "Modern Banking System"
!define MUI_WELCOMEPAGE_TEXT "Java Major Project (BCA Final Year).$\r$\nSecure RBAC, ATM simulator, transfers, loans.$\r$\nTeam (5): Abhishek (Team Lead), Govind Gupta, Khushi Gupta, Pallal, Team Member 5.$\r$\nLive Repo: https://github.com/Govind-gupta243/modern-banking-system"

Section "Install"
    ; Set output path to the installation directory
    SetOutPath "$INSTDIR"

    ; Copy application files
    File "run.bat"
    ; Package optional app icon if present next to this script at build time
    ; (nonfatal ensures build continues even if app.ico is missing)
    File /nonfatal "app.ico"
    
    ; Copy bin directory
    CreateDirectory "$INSTDIR\bin"
    SetOutPath "$INSTDIR\bin"
    File /r "bin\*.*"

    ; Copy lib directory
    CreateDirectory "$INSTDIR\lib"
    SetOutPath "$INSTDIR\lib"
    File /r "lib\*.*"

    ; Copy data directory for logs
    CreateDirectory "$INSTDIR\data"
    SetOutPath "$INSTDIR\data"
    File /r "data\*.*"

    ; Set output path back to main installation directory
    SetOutPath "$INSTDIR"

    ; Create uninstaller
    WriteUninstaller "$INSTDIR\uninstall.exe"

    ; Create Start Menu shortcut
    CreateDirectory "$SMPROGRAMS\${APPNAME}"
    ${If} ${FileExists} "$INSTDIR\app.ico"
      CreateShortcut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\app.ico" 0 "" "" SW_HIDE
    ${Else}
      CreateShortcut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\run.bat" 0 "" "" SW_HIDE
    ${EndIf}
    CreateShortcut "$SMPROGRAMS\${APPNAME}\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0

    ; Create Desktop shortcut
    ${If} ${FileExists} "$INSTDIR\app.ico"
      CreateShortcut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\app.ico" 0 "" "" SW_HIDE
    ${Else}
      CreateShortcut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\run.bat" 0 "" "" SW_HIDE
    ${EndIf}

    ; Add to Add/Remove Programs
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayName" "${APPNAME}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayVersion" "${VERSION}"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "Publisher" "${COMPANYNAME}"
SectionEnd

Section "Uninstall"
    ; Remove shortcuts
    Delete "$DESKTOP\${APPNAME}.lnk"
    Delete "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk"
    Delete "$SMPROGRAMS\${APPNAME}\Uninstall.lnk"
    RMDir "$SMPROGRAMS\${APPNAME}"

    ; Remove files and directories
    RMDir /r "$INSTDIR\bin"
    RMDir /r "$INSTDIR\lib"
    RMDir /r "$INSTDIR\data"
    Delete "$INSTDIR\run.bat"
    Delete "$INSTDIR\uninstall.exe"

    ; Remove from Add/Remove Programs
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}"

    ; Remove installation directory
    RMDir "$INSTDIR"
SectionEnd
