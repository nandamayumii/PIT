@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.4.101-hotspot"
set "TOMCAT=C:\dev\ferramentas\apache-tomcat-10.1.60"

cd /d C:\dev\projetos\catalogo-midia
del "%TOMCAT%\webapps\catalogo.war" 2>nul
rmdir /s /q "%TOMCAT%\webapps\catalogo" 2>nul

call mvn clean package
if errorlevel 1 (
  echo.
  echo *** ERRO NO MAVEN - veja as mensagens acima ***
  pause
  exit /b 1
)

copy target\catalogo.war "%TOMCAT%\webapps\"
cd /d "%TOMCAT%\bin"
call catalina.bat run