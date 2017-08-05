<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.skin.finder.FileItem"%>
<%@ page import="com.skin.finder.config.ConfigFactory"%>
<%@ page import="com.skin.finder.i18n.I18N"%>
<%@ page import="com.skin.finder.i18n.LocalizationContext"%>
<%!
    private String getFileSize(long size) {
        if(size < 1024) {
            return size + " B";
        }

        if(size < 1048576) {
            return (size / 1024) + "KB";
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if(size < 1073741824) {
            return decimalFormat.format(size / 1048576.0f) + "MB";
        }
        return decimalFormat.format(size / 1073741824.0f) + "GB";
    }
%>
<%
    LocalizationContext i18n = I18N.getBundle(request);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Finder - ${path}</title>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/finder/css/finder.css"/>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/ajax.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/widget.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/fileupload.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/finder.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/plugins.js"></script>
</head>
<body localIp="${localIp}" contextPath="${contextPath}" workspace="${workspace}" work="${work}" parent="${parent}" path="${path}">
<div class="finder" style="min-width: 960px;">
    <div class="menu-bar" contextmenu="false">
        <div style="float: left; width: 80px;">
            <a id="back" class="button" href="javascript:void(0)" title="后退"><span class="back"></span></a>
            <a id="refresh" class="button" href="javascript:void(0)" title="刷新"><span class="refresh"></span></a>
        </div>
        <div style="float: left; height: 28px; position: relative;">
            <div style="float: left;"><input id="address" type="text" class="address" autocomplete="off" value="${path}"/></div>
            <div id="finder-suggest" class="list suggest"></div>
            <a class="button" href="javascript:void(0)" title="缩略图"><span class="view"></span></a>
            <div id="view-options" class="list view-menu">
                <ul>
                    <li index="0" option-value="outline"><a href="javascript:void(0)"><%=i18n.getString("finder.list.view.thumbnail")%></a></li>
                    <li index="1" option-value="detail" class="selected"><a href="javascript:void(0)"><%=i18n.getString("finder.list.view.detail")%></a></li>
                </ul>
            </div>
        </div>
        <div style="float: right; width: 120px;">
            <a class="button home" href="javascript:void(0)" title="首页"><span class="home"></span></a>
            <a class="button setting" href="javascript:void(0)" title="设置"><span class="setting"></span></a>
            <a class="button help" href="javascript:void(0)" title="帮助" target="_blank"><span class="help"></span></a>
        </div>
    </div>
    <div id="file-view" class="detail-view">
        <div id="head-view" class="head">
            <span class="icon">&nbsp;</span>
            <span class="fileName orderable" orderBy="file-name" unselectable="on" onselectstart="return false;"><em class="title"><%=i18n.getString("finder.list.title.file-name")%></em><em class="order asc"></em></span>
            <span class="fileSize orderable" orderBy="file-size" unselectable="on" onselectstart="return false;"><em class="title"><%=i18n.getString("finder.list.title.file-size")%></em><em class="order"></em></span>
            <span class="fileType orderable" orderBy="file-type" unselectable="on" onselectstart="return false;"><em class="title"><%=i18n.getString("finder.list.title.file-type")%></em><em class="order"></em></span>
            <span class="lastModified orderable" orderBy="last-modified" unselectable="on" onselectstart="return false;"><em class="title"><%=i18n.getString("finder.list.title.last-modified")%></em><em class="order"></em></span>
            <span class="operate"><em class="title"><%=i18n.getString("finder.list.title.operate")%></em></span>
        </div>
        <ul id="file-list" class="file-list" style="-moz-user-select:none; -webkit-user-select:none; user-select:none;" onselectstart="return false;">
        <%
            @SuppressWarnings("unchecked")
            List<FileItem> fileList = (List<FileItem>)(request.getAttribute("fileList"));

            if(fileList != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for(FileItem fileItem : fileList) {
                    if(fileItem.getIsFile()) {
                        continue;
                    }
        %>

            <li class="item" isFile="false" fileName="<%=fileItem.getFileName()%>" lastModified="<%=fileItem.getLastModified()%>">
                <span class="icon"><img src="?action=res&path=/finder/images/folder.gif"/></span>
                <span class="fileName"><a class="file" href="javascript:void(0)" bind-event="dblclick"><%=fileItem.getFileName()%></a></span>
                <span class="fileSize">&nbsp;</span>
                <span class="fileType">文件夹</span>
                <span class="lastModified"><%=dateFormat.format(new java.util.Date(fileItem.getLastModified()))%></span>
                <span class="operate">
                    <a action="finder-open" href="javascript:void(0)">open</a>
                    <a action="finder-remove" href="javascript:void(0)">delete</a>
                </span>
            </li>
        <%
                }

                for(FileItem fileItem : fileList) {
                    if(!fileItem.getIsFile()) {
                        continue;
                    }
        %>
            <li class="item" fileIcon="<%=fileItem.getFileIcon()%>" fileName="<%=fileItem.getFileName()%>" fileSize="<%=fileItem.getFileSize()%>" lastModified="<%=fileItem.getLastModified()%>">
                <span class="icon"><img src="?action=res&path=/finder/type/<%=fileItem.getFileIcon()%>.gif"/></span>
                <span class="fileName"><a class="file" href="javascript:void(0)" bind-event="dblclick"><%=fileItem.getFileName()%></a></span>
                <span class="fileSize"><%=this.getFileSize(fileItem.getFileSize())%></span>
                <span class="fileType"><%=fileItem.getFileType()%>文件</span>
                <span class="lastModified"><%=dateFormat.format(new java.util.Date(fileItem.getLastModified()))%></span>
                <span class="operate">
                    <a action="finder-tail" href="javascript:void(0)">tail</a>
                    <a action="finder-less" href="javascript:void(0)">less</a>
                    <a action="finder-grep" href="javascript:void(0)">grep</a>
                    <a action="finder-open" href="javascript:void(0)">open</a>
                    <a action="finder-download" href="javascript:void(0)">download</a>
                    <a action="finder-remove" href="javascript:void(0)">delete</a>
                </span>
            </li>
        <%
                }
            }
        %>
        </ul>
    </div>
</div>
<div id="finder-contextmenu" class="contextmenu" style="top: 50px; left: 100px; display: none;">
    <ul class="menu">
        <li class="item" command="open" unselectable="on">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">打 开(O)</a>
        </li>
        <li class="item" command="upload" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/upload.gif"/></span>
            <a class="command" href="javascript:void(0)">上 传(F)</a>
        </li>
        <li class="item disabled" command="download" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/download.gif"/></span>
            <a class="command" href="javascript:void(0)">下 载(G)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" command="cut">
            <span class="icon"><img src="?action=res&path=/finder/images/cut.gif"/></span>
            <a class="command" href="javascript:void(0)">剪 切(X)</a>
        </li>
        <li class="item disabled" command="copy" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/copy.gif"/></span>
            <a class="command" href="javascript:void(0)">复 制(C)</a>
        </li>
        <li class="item disabled" command="paste" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/paste.gif"/></span>
            <a class="command" href="javascript:void(0)">粘 贴(V)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" command="remove" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/delete.gif"/></span>
            <a class="command" href="javascript:void(0)">删 除(D)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="rename" unselectable="on">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">重命名(F2)</a>
        </li>
        <li class="item" command="mkdir" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/folder.gif"/></span>
            <a class="command" href="javascript:void(0)">新建文件夹(N)</a>
        </li>
        <li class="item" command="refresh" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/refresh.gif"/></span>
            <a class="command" href="javascript:void(0)">刷 新(E)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="help" unselectable="on">
            <span class="icon" style="padding-left: 6px; width: 34px; line-height: 22px;"><img src="?action=res&path=/finder/images/help.gif"/></span>
            <a class="command" href="javascript:void(0)">帮 助(H)</a>
        </li>
        <li class="item" command="info" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/props.gif"/></span>
            <a class="command" href="javascript:void(0)">属 性(R)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" unselectable="on">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)">浏览器菜单: ctrl+右键</a>
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
            <img id="finder_imgviewer_img" src="?action=res&path=/finder/images/hua.jpg" style="vertical-align: middle;"/>
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
<div id="pageContext" style="display: none;" upload-part-size="<%=ConfigFactory.getString("finder.upload.part-size", "5M")%>"></div>
</body>
</html>
