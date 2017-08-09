<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.skin.finder.config.ConfigFactory"%>
<%@ page import="com.skin.finder.i18n.I18N"%>
<%@ page import="com.skin.finder.i18n.LocalizationContext"%>
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
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/lang/<%=i18n.getLang()%>.js"></script>
</head>
<body localIp="${localIp}" contextPath="${contextPath}" host="${host}" workspace="${workspace}" work="${work}" parent="${parent}" path="${path}">
<div class="finder" style="min-width: 960px;">
    <div class="menu-bar" contextmenu="false">
        <div style="float: left; width: 80px;">
            <a id="back" class="button" href="javascript:void(0)" title="<%=i18n.getString("finder.button.back")%>"><span class="back"></span></a>
            <a id="refresh" class="button" href="javascript:void(0)" title="<%=i18n.getString("finder.button.refresh")%>"><span class="refresh"></span></a>
        </div>
        <div style="float: left; height: 28px; position: relative;">
            <div style="float: left;"><input id="address" type="text" class="address" autocomplete="off" spellcheck="false" value="${path}"/></div>
            <div id="finder-suggest" class="suggest"></div>
            <a class="button" href="javascript:void(0)" title="<%=i18n.getString("finder.list.view.thumbnail")%>"><span class="view"></span></a>
            <div id="view-options" class="list view-menu">
                <ul>
                    <li index="0" option-value="outline"><a href="javascript:void(0)"><%=i18n.getString("finder.list.view.thumbnail")%></a></li>
                    <li index="1" option-value="detail" class="selected"><a href="javascript:void(0)"><%=i18n.getString("finder.list.view.detail")%></a></li>
                </ul>
            </div>
        </div>
        <div style="float: right; width: 120px;">
            <a class="button home" href="javascript:void(0)" title="<%=i18n.getString("finder.index.home")%>"><span class="home"></span></a>
            <a class="button setting" href="javascript:void(0)" title="<%=i18n.getString("finder.index.setting")%>"><span class="setting"></span></a>
            <a class="button help" href="javascript:void(0)" title="<%=i18n.getString("finder.index.help")%>" target="_blank"><span class="help"></span></a>
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
        <ul id="file-list" class="file-list" style="-moz-user-select:none; -webkit-user-select:none; user-select:none;" onselectstart="return false;"></ul>
    </div>
</div>
<div id="finder-contextmenu" class="contextmenu" style="top: 50px; left: 100px; display: none;">
    <ul class="menu">
        <li class="item" command="open" unselectable="on">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.open")%>(O)</a>
        </li>
        <li class="item" command="upload" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/upload.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.upload")%>(F)</a>
        </li>
        <li class="item disabled" command="download" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/download.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.download")%>(G)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" command="cut">
            <span class="icon"><img src="?action=res&path=/finder/images/cut.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.cut")%>(X)</a>
        </li>
        <li class="item disabled" command="copy" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/copy.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.copy")%>(C)</a>
        </li>
        <li class="item disabled" command="paste" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/paste.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.paste")%>(V)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" command="remove" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/delete.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.delete")%>(D)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="rename" unselectable="on">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.rename")%>(F2)</a>
        </li>
        <li class="item" command="mkdir" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/folder.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.mkdir")%>(N)</a>
        </li>
        <li class="item" command="refresh" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/refresh.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.refresh")%>(E)</a>
        </li>
        <li class="separator"></li>
        <li class="item" command="help" unselectable="on">
            <span class="icon" style="padding-left: 6px; width: 34px; line-height: 22px;"><img src="?action=res&path=/finder/images/help.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.help")%>(H)</a>
        </li>
        <li class="item" command="info" unselectable="on">
            <span class="icon"><img src="?action=res&path=/finder/images/props.gif"/></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.info")%>(R)</a>
        </li>
        <li class="separator"></li>
        <li class="item disabled" unselectable="on">
            <span class="icon"></span>
            <a class="command" href="javascript:void(0)"><%=i18n.getString("finder.context-menu.browser")%></a>
        </li>
    </ul>
</div>
<div id="finder-properties" class="dialog props-dialog" contextmenu="false">
    <div class="title">
        <h4><%=i18n.getString("finder.dialog.props.title")%></h4>
        <span class="close" dragable="false"></span>
    </div>
    <div class="body">
        <div style="padding: 20px 8px 8px 20px;">
            <div class="cp-file-name">
                <span class="label"><img src="" class="file-icon"/></span>
                <input name="fileName" type="text" class="file-name" spellcheck="false" value=""/>
            </div>
            <div class="separator"></div>
            <div class="cp-file-type">
                <span class="label"><%=i18n.getString("finder.dialog.props.type")%>:</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="cp-file-path">
                <span class="label"><%=i18n.getString("finder.dialog.props.path")%>:</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="cp-file-size">
                <span class="label"><%=i18n.getString("finder.dialog.props.size")%>:</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="cp-file-modified">
                <span class="label"><%=i18n.getString("finder.dialog.props.last-modified")%>:</span>
                <span class="field">&nbsp;</span>
            </div>
            <div class="separator"></div>
        </div>
    </div>
    <div class="button right">
        <button type="button" class="button ensure" href="javascript:void(0)"><%=i18n.getString("finder.dialog.button.ensure")%></button>
        <button type="button" class="button cancel" href="javascript:void(0)"><%=i18n.getString("finder.dialog.button.cancel")%></button>
    </div>
</div>
<div id="finder-imageviewer" class="dialog" style="top: 0px; width: 800px; height: 600px; display: none;" contextmenu="false">
    <div class="title">
        <h4><%=i18n.getString("finder.dialog.image.title")%></h4>
        <span class="close" dragable="false"></span>
    </div>
    <div class="body">
        <div style="margin-top: 20px; height: 440px; border: 0px solid #dddddd; line-height: 420px; text-align: center; overflow: hidden;">
            <img id="finder_imgviewer_img" src="?action=res&path=/finder/images/loading.gif" style="vertical-align: middle;"/>
        </div>
        <div style="margin: 20px 0px 10px 0px; text-align: center; color: #666666;">
            <a id="finder_imgviewer_url" style="font-size: 12px;" href="javascript:void(0)" target="_blank"><%=i18n.getString("finder.dialog.image.view-picture")%></a>
        </div>
        <div style="width: 110px; margin: 0px auto 0px auto; overflow: hidden;">
            <div class="pagebar">
                <a class="page active prev" href="javascript:void(0)" title="<%=i18n.getString("finder.dialog.image.button.prev")%>"><span class="prev"></span></a>
                <span style="float: left; display: inline-block; width: 30px;">&nbsp;</span>
                <a class="next active page" href="javascript:void(0)" title="<%=i18n.getString("finder.dialog.image.button.next")%>"><span class="next"></span></a>
            </div>
        </div>
    </div>
</div>
<div id="loading" class="widget-mask" style="display: block;" contextmenu="false"><div class="loading"><img src="?action=res&path=/finder/images/loading.gif"/></div></div>
<div id="pageContext" style="display: none;" upload-part-size="<%=ConfigFactory.getString("finder.upload.part-size", "5M")%>" display-operate-button="<%=ConfigFactory.getString("finder.display.operate-button")%>"></div>
</body>
</html>
