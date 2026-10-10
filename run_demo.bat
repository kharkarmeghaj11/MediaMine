@echo off
chcp 65001 >nul
cd /d "%~dp0"
javac -encoding UTF-8 -d bin -sourcepath src src\mediamine\Main.java
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -cp bin mediamine.Main < demo_inputs.txt
