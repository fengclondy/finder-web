@ECHO OFF
@ECHO delete "webapp\WEB-INF\ayada"
rd /s /q "webapp\WEB-INF\ayada"

@IF exist "C:\apache-ant-1.8.0" @SET ANT_HOME=C:\apache-ant-1.8.0
@IF exist "D:\apache-ant-1.8.0" @SET ANT_HOME=D:\apache-ant-1.8.0
@IF exist "D:\MyApp\setenv.bat" call D:\MyApp\setenv.bat

@ECHO JAVA_HOME: %JAVA_HOME%
@ECHO  ANT_HOME: %ANT_HOME%
@ECHO.

@SET PATH=%PATH%;%ANT_HOME%\bin
call ant "-buildfile" "build.xml"
pause
