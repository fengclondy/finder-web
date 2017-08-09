该目录内（包含子目录）的所有配置文件均可放置到
${user.home}/skinx/${APP_NAME}/${APP_CONF}目录内

user.home: 操作系统当前用户的根目录，linux下为启动tomcat的用户
APP_NAME:  默认为finder, 你可以在classes/app.properties文件中定义APP_NAME
APP_CONF:  默认为META-INF/conf，你可以在classes/app.properties文件中定义APP_CONF

app.properties
============================================================================
name=finder
finder.conf=META-INF/conf

