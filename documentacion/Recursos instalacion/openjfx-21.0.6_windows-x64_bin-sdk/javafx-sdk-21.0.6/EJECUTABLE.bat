@echo off
set JAR=ezjavacode.jar
set JAVAFX_LIB=lib

java --module-path "%~dp0%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -jar "%~dp0%JAR%"
pause