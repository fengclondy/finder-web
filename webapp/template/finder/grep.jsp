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
<title>WebGrep</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/less.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/config.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/charset.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/grep.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    var container = Grep.getContainer();

    jQuery(window).bind("resize", function(){
        jQuery(container).height(jQuery(window).height() - 38);
    });
    jQuery(window).trigger("resize");
});

jQuery(function() {
    Grep.init();
});
//-->
</script>
</head>
<body contextPath="${contextPath}" workspace="${workspace}" parent="${parent}" path="${path}" charset="${charset}">
<!-- 设置-语言-语言和输入设置-去掉勾选 启用拼写检查 -->
<div id="less-container" class="less-container" contenteditable="false"></div>
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
<div id="find-panel" class="find-panel">
    <div>
        查找内容: <input id="grep-keyword" type="text" class="grep-keyword" value="" placeholder="正则示例: /finder/.*\.html"/>
        <input id="grep-ensure" type="button" class="grep-search" value="查找"/>
    </div>
    <div style="clear: both; padding-top: 12px; height: 20px;">
        <span style="float: left; width: 10px; display: inline-block;"><input id="grep-regexp" type="checkbox" title="正则表达式"/></span>
        <span style="float: left; margin-left: 6px; margin-top: -1px; width: 100px; display: inline-block;">正则表达式</span>
    </div>
    <div style="clear: both; margin-top: 10px;">
        <p><span style="color: #ff0000;">提示：</span>快捷键(Ctrl + B)，再次按下可关闭。</p>
        <p><span style="color: #ff0000;">参考：</span><a href="http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#sum" target="_blank">正则表达式参考</a></p>
    </div>
    <div style="text-align: center;">
        <input id="grep-close" type="button" class="grep-button" value="关闭"/>
    </div>
</div>
</body>
</html>
