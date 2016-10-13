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
<title>WebLess</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/less.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/config.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/charset.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/less.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    var container = Less.getContainer();

    jQuery(window).bind("resize", function(){
        jQuery(container).height(jQuery(window).height() - 38);
    });
    jQuery(window).trigger("resize");
});

jQuery(function() {
    Less.init();
});
//-->
</script>
</head>
<body contextPath="${contextPath}" workspace="${workspace}" work="${work}" parent="${parent}" path="${path}" charset="${charset}">
<!-- 设置-语言-语言和输入设置-去掉勾选 启用拼写检查 -->
<div id="less-container" class="less-container">
    <div id="less-editor" contenteditable="false"></div>
</div>

<div id="less-progress-bar" class="less-progress-bar">
    <div class="progress">
        <div class="slider">
            <div class="pace"></div>
            <a class="dot" href="#"></a>
            <div class="mask"></div>
        </div>
    </div>
</div>

<div id="less-status-bar" class="less-status-bar">
    <div style="height: 18px; background-color: #333333;">
        <span class="charset">
            <select name="charset" selected-value="${charset}"></select>
        </span>
        <span class="file"><input id="less-file" type="text" class="text w240" readonly="true" value="${absolutePath}"/></span>
        <span class="info"><input id="less-info" type="text" class="text w160" readonly="true" value="0 B"/></span>
        <span class="status"><input id="less-status" type="text" class="text w160" readonly="true" value="READY"/></span>
    </div>
</div>
<div id="less-tooltip" class="less-tooltip">50%</div>
</body>
</html>
