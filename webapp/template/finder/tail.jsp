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
<script type="text/javascript" src="${contextPath}/resource/finder/config.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/charset.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/tail.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    var container = Tail.getContainer();

    jQuery(window).bind("resize", function(){
        jQuery(container).height(jQuery(window).height() - 26);
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
<body contextPath="${contextPath}" workspace="${workspace}" work="${work}" parent="${parent}" path="${path}" charset="${charset}">
<!-- 设置-语言-语言和输入设置-去掉勾选 启用拼写检查 -->
<div id="tail-container" class="less-container">
    <div id="tail-editor" target="parent" contenteditable="false"></div>
</div>

<div class="less-status-bar">
    <div style="height: 18px; background-color: #333333;">
        <span class="charset"><select name="charset" selected-value="${charset}"></select></span>
        <span class="ctrl">
            <input id="tail-stop-btn" type="button" class="button" value=" 停 止 "/>
            <input id="tail-select-btn" type="button" class="button" value=" 全 选 "/>
            <input id="tail-reload-btn" type="button" class="button" value=" 刷 新 "/>
        </span>
        <span class="pad4">重载时间：<input id="tail-reload-interval" type="text" class="text w30" value="1"/> 秒</span>
        <span class="pad4"><input id="tail-auto-scroll" type="checkbox" class="checkbox" checked="true"/>自动滚动</span>
    </div>
</div>
</body>
</html>