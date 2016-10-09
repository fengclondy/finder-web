@ECHO OFF
del "d:\opt\resin\log\finder.log"
del "webapp\WEB-INF\classes\version.xml"
del "webapp\WEB-INF\upgrade\*.zip"

rd /s /q "webapp\WEB-INF\ayada"

@IF exist "E:\WorkSpace\finder" copy "conf\server-local.xml" "D:\Tomcat-7.0.37\conf\server.xml"
@IF exist "d:\workspace2\finder" copy "conf\server.xml" "D:\Tomcat-7.0.37\conf\server.xml"

cd /d "D:\Tomcat-7.0.37\bin"
startup.bat