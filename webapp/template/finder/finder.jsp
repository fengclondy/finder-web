<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Finder - ${path}</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/finder.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/ajax.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/widget.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/fileupload.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/finder.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/plugins.js"></script>
</head>
<!-- ${request.getServerName()} -->
<!-- http://${loacalIp}/finder/index.html?workspace=${URLUtil.encode(workspace)} -->
<!-- http://${loacalIp}/finder/display.html?workspace=${URLUtil.encode(workspace)}&path=${URLUtil.encode(path)} -->
<body loacalIp="${loacalIp}" contextPath="${contextPath}" workspace="${workspace}" work="${work}" parent="${parent}" path="${(path != '' ? path : '/')}">
<div class="finder" style="min-width: 900px;">
    <div class="menu-bar" contextmenu="false">
        <div style="float: left; width: 80px;">
            <c:if test="${util.isEmpty(parent)}">
            <a class="button disabled" href="javascript:void(0)"><span class="back-disabled"></span></a>
            </c:if>
            <c:if test="${util.notEmpty(parent)}">
            <a class="button" href="javascript:void(0)" title="后退"><span class="back"></span></a>
            </c:if>
            <a class="button" href="javascript:void(0)" title="刷新"><span class="refresh"></span></a>
        </div>
        <div style="float: left; height: 28px; position: relative;">
            <div style="float: left;"><input id="address" type="text" class="address" autocomplete="off" value="${(path != '' ? path : '/')}"/></div>
            <div id="finder-suggest" class="list suggest"></div>
            <a class="button" href="javascript:void(0)" title="缩略图"><span class="view"></span></a>
            <div id="view-options" class="list view-menu">
                <ul>
                    <li index="0" option-value="outline"><a href="javascript:void(0)">缩略图</a></li>
                    <li index="1" option-value="detail" class="selected"><a href="javascript:void(0)">详细信息</a></li>
                </ul>
            </div>
        </div>
        <div style="float: right; width: 120px;">
            <a class="button home" href="javascript:void(0)" title="首页"><span class="home"></span></a>
            <a class="button setting" href="javascript:void(0)" title="首页"><span class="setting"></span></a>
            <a class="button help" href="javascript:void(0)" title="帮助" target="_blank"><span class="help"></span></a>
        </div>
    </div>
    <div id="file-view" class="detail-view">
        <div id="head-view" class="head">
            <span class="icon">&nbsp;</span>
            <span class="fileName orderable" orderBy="file-name" unselectable="on" onselectstart="return false;"><em class="title">名称</em><em class="order asc"></em></span>
            <span class="fileSize orderable" orderBy="file-size" unselectable="on" onselectstart="return false;"><em class="title">大小</em><em class="order"></em></span>
            <span class="fileType orderable" orderBy="file-type" unselectable="on" onselectstart="return false;"><em class="title">类型</em><em class="order"></em></span>
            <span class="lastModified orderable" orderBy="last-modified" unselectable="on" onselectstart="return false;"><em class="title">修改日期</em><em class="order"></em></span>
            <span class="w300"><em class="title">操作</em></span>
        </div>
        <ul id="file-list" class="file-list" style="-moz-user-select:none; -webkit-user-select:none; user-select:none;" onselectstart="return false;">
        <!-- ${path} -->
        <!-- Folder -->
        <c:forEach items="${fileList}" var="file" varStatus="status">
            <c:if test="${!file.isFile}">
            <li class="item" isFile="false" fileName="${file.fileName}" lastModified="${file.lastModified}">
                <span class="icon"><img src="${contextPath}/resource/finder/images/folder.gif"/></span>
                <span class="fileName"><a class="file" href="javascript:void(0)" bind-event="dblclick">${file.fileName}</a></span>
                <span class="fileSize">&nbsp;</span>
                <span class="fileType">文件夹</span>
                <span class="lastModified">${DateUtil.format(file.lastModified, "yyyy-MM-dd HH:mm")}</span>
                <span class="w300">
                    <a action="finder-open" href="javascript:void(0)">open</a>&nbsp;
                    <a action="finder-remove" href="javascript:void(0)">delete</a>
                </span>
            </li>
            </c:if>
        </c:forEach>
        <c:forEach items="${fileList}" var="file" varStatus="status">
            <c:if test="${file.isFile}">
            <li class="item" fileIcon="${file.fileIcon}" fileName="${file.fileName}" fileSize="${file.fileSize}" lastModified="${file.lastModified}">
                <span class="icon"><img src="${contextPath}/resource/finder/type/${file.fileIcon}"/></span>
                <span class="fileName"><a class="file" href="javascript:void(0)" bind-event="dblclick">${file.fileName}</a></span>
                <span class="fileSize">${file.fileSize / 1024}KB</span>
                <span class="fileType">${file.fileType}文件</span>
                <span class="lastModified">${DateUtil.format(file.lastModified, "yyyy-MM-dd HH:mm")}</span>
                <span class="w300">
                    <a action="finder-less" href="javascript:void(0)">less</a>&nbsp;
                    <a action="finder-tail" href="javascript:void(0)">tail</a>&nbsp;
                    <a action="finder-open" href="javascript:void(0)">open</a>&nbsp;
                    <a action="finder-download" href="javascript:void(0)">download</a>&nbsp;
                    <a action="finder-remove" href="javascript:void(0)">delete</a>
                </span>
            </li>
            </c:if>
        </c:forEach>
        </ul>
    </div>
</div>
<div id="finder-contextmenu" class="contextmenu" style="top: 50px; left: 100px; display: none;">
    <ul class="menu">
        <li class="item" command="open">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">打 开(O)</a>
        </li>
        <li class="item" command="upload">
            <span class="icon"><img src="${contextPath}/resource/finder/images/upload.gif"/></span>
            <a class="command" href="javascript:void(0)">上 传(F)</a>
        </li>
        <li class="item disabled" command="download">
            <span class="icon"><img src="${contextPath}/resource/finder/images/download.gif"/></span>
            <a class="command" href="javascript:void(0)">下 载(G)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" command="cut">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">剪 切(X)</a>
        </li>
        <li class="item disabled" command="copy">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">复 制(C)</a>
        </li>
        <li class="item disabled" command="paste">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">粘 贴(V)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" command="remove">
            <span class="icon"><img src="${contextPath}/resource/finder/images/delete.gif"/></span>
            <a class="command" href="javascript:void(0)">删 除(D)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="rename">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">重命名(F2)</a>
        </li>
        <li class="item" command="mkdir">
            <span class="icon"><img src="${contextPath}/resource/finder/images/folder.gif"/></span>
            <a class="command" href="javascript:void(0)">新建文件夹(N)</a>
        </li>
        <li class="item" command="refresh">
            <span class="icon"><img src="${contextPath}/resource/finder/images/refresh.gif"/></span>
            <a class="command" href="javascript:void(0)">刷 新(E)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="viewsource">
            <span class="icon"><img src="${contextPath}/resource/finder/images/source.gif"/></span>
            <a class="command" href="javascript:void(0)">查看网页源代码(U)</a>
        </li>
        <li class="item" command="help">
            <span class="icon" style="padding-left: 6px; width: 34px; line-height: 22px;"><img src="${contextPath}/resource/finder/images/help.gif"/></span>
            <a class="command" href="javascript:void(0)">帮 助(H)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="info">
            <span class="icon"><img src="${contextPath}/resource/finder/images/props.gif"/></span>
            <a class="command" href="javascript:void(0)">属 性(R)</a>
        </li>
    </ul>
</div>

<div id="finder-properties" class="dialog props-dialog" contextmenu="false">
    <div class="title">
        <h4>属性对话框</h4>
        <span class="close" dragable="false"></span>
    </div>
    <div class="body">
        <div style="padding: 20px 8px 8px 20px;">
            <div class="cp-file-name">
                <span class="label"><img src="" class="file-icon"/></span>
                <input name="fileName" type="text" class="file-name" value=""/>
            </div>
            <div class="separator"></div>
            <div class="cp-file-type">
                <span class="label">类型：</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="cp-file-path">
                <span class="label">位置：</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="cp-file-size">
                <span class="label">大小：</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="cp-file-modified">
                <span class="label">修改时间：</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="separator"></div>
        </div>
    </div>
    <div class="button right">
        <button type="button" class="button ensure" href="javascript:void(0)">确 定</button>
        <button type="button" class="button cancel" href="javascript:void(0)">取 消</button>
    </div>
</div>

<div id="finder-imageviewer" class="dialog" style="top: 0px; width: 800px; height: 600px; display: none;" contextmenu="false">
    <div class="title">
        <h4>Finder Image Viewer</h4>
        <span class="close" dragable="false"></span>
    </div>
    <div class="body">
        <div style="margin-top: 20px; height: 440px; border: 0px solid #dddddd; line-height: 420px; text-align: center; overflow: hidden;">
            <img id="finder_imgviewer_img" src="${contextPath}/resource/finder/images/hua.jpg" style="vertical-align: middle;"/>
        </div>
        <div style="margin: 20px 0px 10px 0px; text-align: center; color: #666666;">
            <a id="finder_imgviewer_url" style="font-size: 12px;" href="javascript:void(0)" target="_blank">查看原图</a>
        </div>
        <div style="width: 110px; margin: 0px auto 0px auto; overflow: hidden;">
            <div class="pagebar">
                <a class="page active prev" href="javascript:void(0)" title="上一张"><span class="prev"></span></a>
                <span style="float: left; display: inline-block; width: 30px;">&nbsp;</span>
                <a class="next active page" href="javascript:void(0)" title="下一张"><span class="next"></span></a>
            </div>
        </div>
    </div>
</div>
<!-- ${template.home}/${template.path} -->
</body>
</html>