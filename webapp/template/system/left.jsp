<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>管理后台</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/htree/css/htree.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/htree/htree.js"></script>
</head>
<body>
<div class="left-nav">
    <!-- div class="menu-head"><h4>系统管理</h4></div -->
    <div class="menu-body" style="overflow: auto;">
        <div id="treePanel" class="htree" style="margin-top: 10px; white-space: nowrap;">
            <ul class="menu">
                <li><a href="${contextPath}/system/schedule/list.html" target="mainFrame">任务管理</a></li>
                <li><a href="${contextPath}/system/config.html" target="mainFrame">系统变量管理</a></li>
                <li><a href="${contextPath}/system/accesslog/index.html" target="mainFrame">访问日志</a></li>
                <li><a href="${contextPath}/system/index.html" target="mainFrame">系统信息</a></li>
                <li><a href="${contextPath}/system/upgrade/index.html" target="mainFrame">系统更新</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</noframes>
</html>