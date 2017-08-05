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
<title>Finder</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/finder/css/frame.css"/>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/widget.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/fileupload.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/index.js"></script>
</head>
<body>
<div id="menu-bar">
    <ul>
        <li onclick="window.location.href='${requestURI}';"><%=i18n.format("finder.index.home")%></li>
        <li onclick="window.location.href='${requestURI}?action=user.admin';"><%=i18n.format("finder.index.user-admin")%></li>
        <li onclick="window.location.href='${requestURI}?action=user.login';"><%=i18n.format("finder.index.login")%></li>
        <li onclick="window.location.href='${requestURI}?action=user.logout';"><%=i18n.format("finder.index.logout")%></li>
    </ul>
</div>
<div id="viewPanel">
    <div id="leftPanel" class="left-panel"><iframe id="leftFrame" name="leftFrame" class="left-frame"
        src="${requestURI}?action=finder.tree" frameborder="0" scrolling="no" marginwidth="0" marginheight="0"></iframe></div>

    <div id="mainPanel" class="main-panel"><iframe id="mainFrame" name="mainFrame" class="main-frame"
        src="${requestURI}?action=finder.blank" frameborder="0" scrolling="auto" marginwidth="0" marginheight="0"></iframe></div>
</div>
<div id="statusBar" class="status-bar hide">
    <div id="_task_bar" class="widget-task-bar"></div>
    <div style="float: right;">
        <img style="margin-top: -2px; margin-right: 4px;" src="${requestURI}?action=res&path=/finder/images/sound.gif"/>Welcome ！
    </div>
</div>
<!--
<div id="upgrade-tips" style="position: fixed; bottom: 2px; right: 2px; padding: 6px; width: 300px; height: 16px; border: 1px solid #22b14c; background-color: #ffc90e;">
    New Upgrade！<a href="${appDownloadUrl}" target="_blank">click for download</a>
</div>
-->
<div id="pageContext" style="display: none;" contextPath="${contextPath}" nav-bar="<%=ConfigFactory.getBoolean("finder.display.nav-bar", true)%>"></div>
</body>
</html>
