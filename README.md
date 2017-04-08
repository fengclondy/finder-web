概述 Overview
==================

Finder是一个web方式的文件管理器。Finder最主要的功能是超大日志文件的实时查看。

* 类似window的资源管理器方式对文件进行操作。
* 文件拖拽上传。
* 文本文件在线浏览。
* 图片文件在线浏览。
* 音频文件和视频文件直接播放。
* web方式的log文件查看，支持任意大小的文本文件，支持类似于linux系统的tail和less方式查看，并支持任意定位文件位置，无论文件大小，查看体验都近似于本地文件。
* 你也可以将它部署在你的NAS上通过浏览器管理你服务器上文件。

Finder2即将推出, 敬请关注
Finder2的新特性:
1. 在保持原有功能的同时，将更加容易集成, 完全无第三方依赖. 集成包只有一个jar文件, 并且集成包可以部署在服务器的任意目录, 包括但不限于应用的目录, 你甚至可以放在服务器的/tmp目录.
   采用动态加载技术, 部署完成无需重启服务器即可访问。
2. 提供更多开发类的小工具.
3. 增加系统监控, 允许你通过finder的web界面远程调用服务器上的任意服务, 包括但不限于被spring管理的类.
4. API工具: 自动化测试支持: sql工具, 允许你在自动化测试中调用外部sql文件解释执行; 命令行工具, 允许使用命令行交互的方式反复调用不同的服务, 不必每次都重启junit;
   数据导入导出工具, 支持任意大小的sql, csv文件多线程导入到数据库, 并且允许你在导入过程中做数据转换, 适合非常规需求的临时数据处理.
   该功能需要应用方引入finder提供的jar文件.


运行截图
=================
 **grep示例**
输入关键字进行查找，支持正则。
![grep示例](http://git.oschina.net/uploads/images/2017/0107/165229_72067217_615195.jpeg "grep示例")

 **less示例**
绿色的进度条可以随着内容的滚动自动显示当前进度，也可以单击某一个位置直接跳转到文件的指定位置显示。可以指定文件的字符集，也可以在finder的文件夹页面设置全局字符集和less的显示样式，包括字体，字体颜色，背景颜色等。
![less示例](http://git.oschina.net/uploads/images/2017/0107/170241_971f549f_615195.jpeg "less示例")

 **tail示例**
tail支持查找，输入查找内容之后，tail将只显示包含指定内容的数据。支持正则。
![tail示例](http://git.oschina.net/uploads/images/2017/0107/170323_36bcb06d_615195.jpeg "tail示例")

 **音频和视频播放**
finder支持播放视频和音频，需支持h5的浏览器。音频和视频文件必须是h5支持的格式(mp3, mp4, mpeg)。
在播放音频的同时仍然允许切换不同的文件夹。你也可以将音频播放器最小化到左下角。
![音频播放](http://git.oschina.net/uploads/images/2016/1013/001204_7d139006_615195.jpeg "音频播放")

 **文件管理**
支持全键盘操作，几乎所有的操作都有对应的快捷键，并且尽可能与windows资源管理器的快捷键相同。
支持多文件剪切，拷贝。
支持任意大小的文件上传，采用分段上传的方式，不受服务器超时限制。仅限支持h5的浏览器。
支持下载文件的断点续传，支持使用多线程工具下载文件。
![文件管理](http://git.oschina.net/uploads/images/2016/1013/001214_cdc0ae21_615195.jpeg "文件管理")

编译运行
=================
1. 代码导入到eclipse并编译。
2. 修改src/main/resource/META-INF/conf/workspace.xml，指定可管理的目录。
3. 修改logback.xml，如果不修改，windows系统下请确保tomcat所在的磁盘存在 /opt/resin/log 目录。
4. 将webapp目录中的文件拷贝到tomcat的webapps/ROOT目录 或者 直接配置tomcat的server.xml，配置一个context指到webapp目录。
5. 启动应用之后访问： http://localhost/finder/index.html, 默认的用户名: admin, 密码: 1234, 请登录之后及时修改密码。
6. 测试日志功能请找一个较大的日志文件进行测试，太小的日志文件显示不出来效果。
7. 注：workspace.xml中可以配置任意多个workspace，每一个workspace的name可以随便指定一个名字，文件夹也是自定义的，你可以指向任意目录，不一定非得指到logback.xml中配置的目录。
    finder并不限制你要查看的文件，你可以使用日志监控功能监控任意的文件，但请不要查看非文本文件，less和tail都是按行拉取的。
    如果一个文件超大又不存在换行符，你的服务器或者浏览器可能会崩溃，所以请只监控文本文件。
    less和tail功能在当前窗口不会显示过多的内容，以避免浏览器消耗太多内存，当内容超过一定数量的时候会自动将处于窗口之外的内容清除。
    当你再此滚动到那个位置的时候会再次加载，这个对用户来说是感觉不出来的。
    所以理论上less和tail功能可以实时监控任意大小的日志文件，当你有一个超大的日志文件，一般百兆以上，其他文本编辑器打不开时，finder是个很好的工具。
    less可以任意定义文件位置，tail只实时的显示文件的尾部数据。tail也具有grep功能，可以只监控关心的数据。
8. 开两个窗口，一个使用tail打开finder的日志，另外一个访问/log.jsp，不断刷新log.jsp，可以看到tail不断刷新日志的效果。如果你把finder部署在了生产环境，请删除log.jsp文件。

你也可以直接将release/finder.war部署到tomcat, 启动之后tomcat会自动解压finder.war, 然后修改对应目录内的配置文件，修改之后重启tomcat即可。

用户控制
=================
finder默认的用户数据存储在WEB-INF/user目录下，一个用户一个文件，文件名以用户名命名。
这个实现是一个及其简陋的实现，只是为了满足用户控制的功能，未来不打算也不会提供更过的用户控制功能。
finder本身只是打算作为其他系统的一部分，因此也不会支持权限控制，如果你需要更多更强大的用户和权限控制，请参考SessionFilter的方式，将Finder默认的SessionFilter替换为你自己的Filter。
默认的用户控制：SessionFilter, 如果你不需要这个功能，将它从web.xml配置中移除即可。默认的用户是admin, 密码是1234。
安全问题: 由于finder是开源的, 任何人都可能看到源码, 所以你最好修改cookiekey.properties文件中的cookie.md5key，这个是用来签名的，如果泄露就可能自己伪造cookie。
以下链接不会在界面中体现，需要添加用户或者登出系统，请直接在地址栏输入地址：
添加用户: /finder/user/add.html
修改密码: /finder/user/add.html
系统登录: /finder/login.html
系统登出: /finder/logout.html
修改密码只需重新添加用户即可。

常见问题
=================
1. 中文乱码
请在tomcat的server.xml中配置URIEncoding:
``
<Connector port="80" protocol="HTTP/1.1"
    connectionTimeout="20000"
    redirectPort="8443" URIEncoding="UTF-8"/>
``

2. 'javax.tools.JavaCompiler' not found
   finder使用的jsp编译器要求必须加载tools.jar, 所以在环境变量里面配置%JAVA_HOME%\lib\tools.jar即可。

自定义插件
=================
finder允许你自定义自己的插件，finder会根据不同的文件扩展名调用不同的插件打开文件。
插件编写请看考 webapp/resource/finder/plugins.js 和 webapp/resource/finder/plugins/media/media.js

其他系统集成
=================
finder本身不提供任何的权限控制, 所以对于权限敏感的系统，可以通过添加filter的方式，过滤所有/finder/*的请求，并做权限校验。
出于安全考虑，finder对任何文件的访问都需要该文件位于workspace.xml中配置的目录内。

1. 第一种方法，也是推荐的做法，将release目录的finder-res-xxx.jar和finder-web-xxx.jar拷贝到你的应用lib下，参考web.xml将相关过滤器在你的应用中注册即可。
2. 第二种方法，引入finder-web.jar，根据你的应用所使用的框架自己调用FinderServlet和LessServlet的相关方法，具体代码可参考FinderAction和LessAction。

其他说明
=================
日志监控功能基于拉模式实现，拉模式有优点也有缺点，优点是不受服务器超时限制，推模式一般会使用http长连接，一般服务器或者反向代理服务器都有超时设置，推模式容易导致超时，拉模式没有这个限制，因为每次请求时间都很短。
第二，拉模式在低版本的浏览器或者服务器上也可以很好工作。缺点是需要频繁拉取数据。为了尽可能通用，所以采用了拉模式实现。

BUG反馈
=================
我自己发现的bug都会及时修改并提交。使用过程中发现的bug也请反馈给我，我会及时修改。另外发现bug也请及时下载新版本。
也可进QQ群反馈：341624652


ChangeLog
=================
$# ........................................................
$# version: 1.0.0.5
$# download: http://git.oschina.net/xuesong123/finder-web
$## change log
$## 1. 修复一处严重bug, workspace使用contextpath配置时无法找到目录，已修复;
$# ........................................................

