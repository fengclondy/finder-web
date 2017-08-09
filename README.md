概述 Overview
==================

Finder是一个web方式的文件管理器。Finder最主要的功能是超大日志文件的实时查看。

* 类似window的资源管理器方式对文件进行操作。
* 文件拖拽上传。
* 支持截图上传(CTRL + V)。
* 文本文件在线浏览。
* 图片文件在线浏览。
* 音频文件和视频文件直接播放。
* web方式的log文件查看，支持任意大小的文本文件，支持类似于linux系统的tail和less方式查看，并支持任意定位文件位置，无论文件大小，查看体验都近似于本地文件。
* 支持集群部署，允许你通过finder对多台机器上的日志文件进行实时监控。
* 你也可以将它部署在你的NAS上通过浏览器管理你服务器上文件。
* Finder支持群组(QQ)：341624652


运行截图
=================
**集群支持**
支持集群部署，允许你部署多台机器，通过其中任意一台机器即可管理集群内的所有机器上的文件，并可监控集群内任意一台机器上的日志文件。
支持国际化，下图是英文版的截图。2.2.0版本新增更多配置项，允许你配置文件列表显示的操作按钮，允许你配置工作空间为只读模式。更多配置项请参见finder.conf中的说明。
![集群支持](https://git.oschina.net/uploads/images/2017/0809/222427_0c1e9504_615195.jpeg "cluster.jpg")


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

集成到其他系统
=================
finder非常容易集成到你现有的系统中，只有一个jar文件, 依赖slf4j-xxx.jar，所以只需将finder提供的jar包放到你的应用里面即可. 
以下集成方式都需要先将workspace.xml拷贝到你应用的WEB-INF/classes/META-INF/conf目录
1. 第一种集成方式, 需要重启  
1.1 将release/finder-web.2.0.0.jar拷贝到你的应用的WEB-INF/lib目录.  
1.2 修改你应用的web.xml, 参考webapp/WEB-INF/web.xml, 将FinderServlet注册到你的web应用中.  
1.3 重启你的应用，访问/finder即可.  

2. 第二种集成方式, 需要重启  
2.1 将release/finder-web.2.0.0.jar拷贝到你的应用的WEB-INF/lib目录.  
2.2 将webapp/finder.jsp拷贝到你的应用目录, 文件名可以改为任意你喜欢的名称.  
2.3 重启你的应用，访问/finder.jsp即可.  

3. 第二种集成方式, 不需要重启  
3.1 将release/finder-web.2.0.0.jar拷贝到你的服务器的任意目录, 例如/tmp/finder-web.2.0.0.jar.  
3.2 将webapp/test.jsp拷贝到你的应用目录, 文件名可以改为任意你喜欢的名称.  
3.3 修改test.jsp, 将里面加载jar包的位置改为/tmp/finder-web.2.0.0.jar  
3.4 访问/test.jsp即可.  

独立部署
=================
1. 修改webapp/WEB-INF/classes/META-INF/conf/workspace.xml，指定可管理的目录。
2. 修改logback.xml，如果不修改，windows系统下请确保tomcat所在的磁盘存在 /opt/resin/log 目录。
3. 将webapp目录中的文件拷贝到tomcat的webapps/ROOT目录 或者 直接配置tomcat的server.xml，配置一个context指到webapp目录。
4. 测试日志功能请找一个较大的日志文件进行测试，太小的日志文件显示不出来效果。
5. 注：workspace.xml中可以配置任意多个workspace，每一个workspace的name可以随便指定一个名字，文件夹也是自定义的，你可以指向任意目录，不一定非得指到logback.xml中配置的目录。
    finder并不限制你要查看的文件，你可以使用日志监控功能监控任意的文件，但请不要查看非文本文件，less和tail都是按行拉取的。
    如果一个文件超大又不存在换行符，你的服务器或者浏览器可能会崩溃，所以请只监控文本文件。
    less和tail功能在当前窗口不会显示过多的内容，以避免浏览器消耗太多内存，当内容超过一定数量的时候会自动将处于窗口之外的内容清除。
    当你再此滚动到那个位置的时候会再次加载，这个对用户来说是感觉不出来的。
    所以理论上less和tail功能可以实时监控任意大小的日志文件，当你有一个超大的日志文件，一般百兆以上，其他文本编辑器打不开时，finder是个很好的工具。
    less可以任意定义文件位置，tail只实时的显示文件的尾部数据。tail也具有grep功能，可以只监控关心的数据。
7. 开两个窗口，一个使用tail打开finder的日志，另外一个访问/log.jsp，不断刷新log.jsp，可以看到tail不断刷新日志的效果。如果你把finder部署在了生产环境，请删除log.jsp文件。
8. 该版本是简单的集成版本，除了日志系统无其他依赖. 

编译事项
=================
1. 为了便于其他应用集成, finder对外提供的只有一个jar文件, 所以展现层对应的jsp文件都会被编译为servlet, 这个是在开发期编译的。
2. 如果你修改了java代码，请使用build.xml重新编译，build之后源码resource中的文件会被拷贝到webapp/WEB-INF/classes目录中.
3. 如果你修改了webapp/template中的jsp文件, 你需要使用JspKit这个类将jsp生成为对应的servlet. 然后再重新编译出jar文件.

用户控制
=================
finder默认的用户数据存储在WEB-INF/user目录下，一个用户一个文件，文件名以用户名命名。
这个实现是一个及其简陋的实现，只是为了满足用户控制的功能，未来不打算也不会提供更过的用户控制功能。
finder本身只是打算作为其他系统的一部分，因此也不会支持权限控制，如果你需要更多更强大的用户和权限控制，请参考SessionFilter的方式，将Finder默认的SessionFilter替换为你自己的Filter。
默认的用户控制：SessionFilter, 如果你不需要这个功能，将它从web.xml配置中移除即可。默认的用户是admin, 密码是1234。
安全问题: 由于finder是开源的, 任何人都可能看到源码, 所以你最好修改cookie.properties文件中的cookie.passport.md5.key，这个是用来签名的，如果泄露就可能自己伪造cookie。
以下链接不会在界面中体现，需要添加用户或者登出系统，请直接在地址栏输入地址：
添加用户: /finder?action=user.add
修改密码: /finder?action=user.add
系统登录: /finder?action=finder.login
系统登出: /finder?action=finder.logout
修改密码只需重新添加用户即可。

常见问题
=================
1. 中文乱码
请在tomcat的server.xml中配置URIEncoding:
``
<Connector port="80" protocol="HTTP/1.1"
    connectionTimeout="20000"
    redirectPort="8443" URIEncoding="UTF-8" maxPostSize="1073741824"/>
``

maxPostSize：tomcat允许的最大post body大小，finder默认大文件分段上传每次上传5M的数据，此处配置必须大于5M(请求头 + 数据5M)。

2. 'javax.tools.JavaCompiler' not found
   finder使用的jsp编译器要求必须加载tools.jar, 所以请确保%JAVA_HOME%\lib\tools.jar这个文件存在。

自定义插件
=================
finder允许你自定义自己的插件，finder会根据不同的文件扩展名调用不同的插件打开文件。
插件编写请看考 webapp/resource/finder/plugins.js 和 webapp/resource/finder/plugins/media/media.js

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
$# version: 2.0.0
$# download: http://git.oschina.net/xuesong123/finder-web
$## change log
$## 1. 修复一处严重bug, workspace使用contextpath配置时无法找到目录，已修复;
$# ........................................................

