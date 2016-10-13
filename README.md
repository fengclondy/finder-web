概述 Overview
==================

Finder是一个web方式的文件管理器。Finder最主要的功能是超大日志文件的实时查看。

* 类似window的资源管理器方式对文件进行操作。
* 文本文件在线浏览。
* 图片文件在线浏览。
* 音频文件和视频文件直接播放。
* web方式的log文件查看，支持任意大小的文本文件，支持类似于linux系统的tail和less方式查看，并支持任意定位文件位置，无论文件大小，查看体验都近似于本地文件。

运行截图
=================
 **less示例**
绿色的进度条可以随着内容的滚动自动显示当前进度，也可以单击某一个位置直接跳转到文件的指定位置显示。可以指定文件的字符集，也可以在finder的文件夹页面设置全局字符集和less的显示样式，包括字体，字体颜色，背景颜色等。
![less示例](http://git.oschina.net/uploads/images/2016/1013/001137_3200dc48_615195.jpeg "less示例")

 **tail示例**
可以通过设置自动拉取时间调整拉取频率
![tail示例](http://git.oschina.net/uploads/images/2016/1013/001151_f65afc3d_615195.jpeg "tail示例")

 **音频播放**
在播放音频的同时仍然允许切换不同的文件夹。你也可以将音频播放器最小化到左下角。
![音频播放](http://git.oschina.net/uploads/images/2016/1013/001204_7d139006_615195.jpeg "音频播放")

 **文件管理**
支持全键盘操作，几乎所有的操作都有对应的快捷键，并且尽可能与windows资源管理器的快捷键相同。
支持多文件剪切，拷贝，支持超大文件上传下载，支持使用多线程工具下载文件。
![文件管理](http://git.oschina.net/uploads/images/2016/1013/001214_cdc0ae21_615195.jpeg "文件管理")

编译运行
=================

1. 代码导入到eclipse并编译。
2. 修改src/main/resource/META-INF/conf/workspace.xml，指定可管理的目录。
3. 修改logback.xml，如果不修改，windows系统下请确保tomcat所在的磁盘存在 /opt/resin/log 目录。
4. 将webapp目录中的文件拷贝到tomcat的webapps/ROOT目录 或者 直接配置tomcat的server.xml，配置一个context指到webapp目录。
5. 启动应用之后访问： http://localhost/finder/index.html
6. 测试日志功能请找一个较大的日志文件进行测试，太小的日志文件显示不出来效果。

常见问题
=================
1. 中文乱码
请在tomcat的server.xml中配置URIEncoding:
``xml
<Connector port="80" protocol="HTTP/1.1"
    connectionTimeout="20000"
    redirectPort="8443" URIEncoding="UTF-8"/>
``

自定义插件
=================
finder允许你自定义自己的插件，finder会根据不同的文件扩展名调用不同的插件打开文件。
插件编写请看考 webapp/resource/finder/plugins.js 和 webapp/resource/finder/plugins/media/media.js

其他系统集成
=================
finder本身不提供任何的权限控制, 所以对于权限敏感的系统，可以通过添加filter的方式，过滤所有/finder/*的请求，并做权限校验。
出于安全考虑，finder对任何文件的访问都需要该文件位于workspace.xml中配置的目录内。

1. 第一种方法，也是推荐的做法，参考web.xml将相关过滤器在你的应用中注册即可。
2. 第二种方法，引入finder-web.jar，根据你的应用所使用的框架自己调用FinderServlet和LessServlet的相关方法，具体代码可参考FinderAction和LessAction。

其他说明
=================
日志监控功能基于拉模式实现，拉模式有优点也有缺点，优点是不受服务器超时限制，推模式一般会使用http长连接，一般服务器或者反向代理服务器都有超时设置，推模式容易导致超时，拉模式没有这个限制，因为每次请求时间都很短。
第二，拉模式在低版本的浏览器或者服务器上也可以很好工作。缺点是需要频繁拉取数据。为了尽可能通用，所以采用了拉模式实现。

BUG反馈
=================
这个东西是我平时常用的一个小工具，我自己发现的bug都会及时修改并提交。使用过程中发现的bug也请反馈给我，我会及时修改。另外发现bug也请及时下载新版本。


