; NSIS Installer Script for Modern Banking System

!define APPNAME "Modern Banking System"
!define COMPANYNAME "Banking Corp"
!define DESCRIPTION "A modern banking system application"
!define VERSION "1.0.0"

; Main Install settings
Name "${APPNAME}"
InstallDir "$PROGRAMFILES\${APPNAME}"
OutFile "ModernBankingSystemInstaller.exe"

; Request application privileges for Windows Vista and higher
RequestExecutionLevel admin

; Modern UI
!include "MUI2.nsh"
!define MUI_ABORTWARNING

; Pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

; Languages
!insertmacro MUI_LANGUAGE "English"

Section "Install"
    ; Set output path to the installation directory
    SetOutPath "$INSTDIR"

    ; Copy application files
    File "run.bat"
    
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
    CreateShortcut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\run.bat" 0
    CreateShortcut "$SMPROGRAMS\${APPNAME}\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0

    ; Create Desktop shortcut
    CreateShortcut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\run.bat" 0

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
