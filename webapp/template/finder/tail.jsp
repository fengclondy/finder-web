<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>WebTail</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/less.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/charset.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/tail.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    var container = Tail.getContainer();

    jQuery(window).bind("resize", function(){
        jQuery(container).height(jQuery(window).height() - 30);
    });
    jQuery(window).trigger("resize");
});

jQuery(function(){
    jQuery("#tailButton").toggle(function(){
        this.value = " 启 动 ";
        Tail.stop();
    }, function(){
        this.value = " 停 止 ";
        Tail.start();
    });
    Tail.init();
});
//-->
</script>
</head>
<body workspace="${workspace}" work="${work}" parent="${parent}" path="${path}" charset="${charset}">
<!-- 设置-语言-语言和输入设置-去掉勾选 启用拼写检查 -->
<div id="tail-container" class="less-container" style="width: 100%; height: 400px;">
    <div id="tail-editor" target="parent" contenteditable="false"></div>
</div>

<div class="less-status-bar">
    <input id="tailButton" type="button" class="button" value=" 停 止 " onclick="Tail.stop();"/>
    <input type="button" class="button" value=" 全 选 " onclick="Tail.select();"/>
    <input type="button" class="button" value=" 刷 新 " onclick="window.location.reload();"/>

    <select name="charset" selected-value="${charset}"></select>
    <span style="font-size: 12px;">重载时间：</span><input id="reloadInterval" type="text" class="text w30" value="2"/> 秒
    <input type="checkbox" checked="true" style="margin-top: -1px; border: none; background: none; vertical-align: middle;" onclick="Tail.setScroll(this.checked);"/>
    <span style="font-size: 12px;">自动滚动</span>
</div>
</body>
</html>