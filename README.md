<div align="center">

# Modern Banking System

Java Major Project (BCA Final Year) — Secure, modern, and GUI‑driven banking.

![Java](https://img.shields.io/badge/Java-8%2B-blue)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-forestgreen)
![Database](https://img.shields.io/badge/Database-MySQL-informational)
![Installer](https://img.shields.io/badge/Installer-NSIS-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Windows-blueviolet)

<br/>
<!-- Optional demo GIF or screenshot -->
<!-- Add your GIF at assets/demo.gif and uncomment the line below -->
<!-- <img src="assets/demo.gif" alt="Demo" width="720" /> -->

</div>

A comprehensive Java-based banking system with MVC architecture, supporting Administrators, Employees, Customers, and ATM operations.

## Badges
![Java](https://img.shields.io/badge/Java-8%2B-blue)
![Build](https://img.shields.io/badge/build-manual-lightgrey)
![License](https://img.shields.io/badge/license-MIT-green)

## Features
- Role-Based Access Control (RBAC)
- ATM Simulator (Cash Withdrawal, Deposit, Mini Statement)
- Loan Management (Apply and Repay)
- Fund Transfers with security limits
- Modern UI using Java Swing and FlatLaf
- MySQL-backed Persistence (JDBC)

## Plan
1. Core Models & Persistence
2. Business Logic Controllers
3. Modern GUI Implementation
4. Security & Validation
5. Documentation & Finalization

## Quick Start
- Requirements: Java 8+, MySQL Server, MySQL Connector/J (included in `ModernBankingSystem/lib`)
- Compile (Windows):
  `javac -source 8 -target 8 -d ModernBankingSystem\bin -cp "ModernBankingSystem\lib\*;ModernBankingSystem\src" ModernBankingSystem\src\com\banking\Main.java ModernBankingSystem\src\com\banking\db\*.java ModernBankingSystem\src\com\banking\model\*.java ModernBankingSystem\src\com\banking\controller\*.java ModernBankingSystem\src\com\banking\view\*.java ModernBankingSystem\src\com\banking\util\*.java`
- Run (Windows): `ModernBankingSystem\run.bat`

Default logins and detailed instructions are in `howtorun.txt`.

## About
- Java Major Project — BCA Final Year.
- Built with Java Swing and MySQL using an MVC architecture with secure RBAC.
- Team (5): Abhishek (Team Lead), Govind Gupta, Khushi Gupta, Pallal, Team Member 5.
- Live Repository: https://github.com/Abhishek-Maheshwari-778/Java_Major_project_bankingSystem

## Screenshots
- Add screenshots to an assets/ directory and showcase the UI here.
- Example:
  - Login Screen
  - Admin Dashboard
  - ATM Simulator

## Installer
- Windows installer generated with NSIS.
- Shortcuts launch the app without any console windows.
- Java is auto‑detected at runtime; if not installed system‑wide, the app guides users accordingly.
