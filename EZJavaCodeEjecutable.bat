@echo off
REM Este .bat debe estar en la carpeta raíz del proyecto
REM Llama al .bat real dentro de la carpeta de JavaFX usando ruta relativa

cd "%~dp0documentacion\Recursos instalacion\openjfx-21.0.6_windows-x64_bin-sdk\javafx-sdk-21.0.6"
call EJECUTABLE.bat