# Java Major Project: Banking System

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
