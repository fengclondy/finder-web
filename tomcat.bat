@ECHO OFF
@ECHO delete log files.
@IF exist "D:\MyApp\setenv.bat" call D:\MyApp\setenv.bat

del "d:\opt\resin\log\*.log"
del "webapp\WEB-INF\classes\version.xml"
del "webapp\WEB-INF\upgrade\*.zip"
del "%TOMCAT_HOME%\logs\*.log"
del "%TOMCAT_HOME%\logs\*.txt"
rd /s /q "webapp\WEB-INF\ayada"

@ECHO   JAVA_HOME: %JAVA_HOME%
@ECHO TOMCAT_HOME: %TOMCAT_HOME%

copy "conf\server8.xml" "%TOMCAT_HOME%\conf\server.xml"

cd /d "%TOMCAT_HOME%\bin"
startup.bat
