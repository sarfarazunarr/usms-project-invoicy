@echo off
cd /d %~dp0
cmd /k "mvn exec:java"
