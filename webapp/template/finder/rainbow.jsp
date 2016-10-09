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
<title>${path}</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/finder.css"/>
<link type="text/css" rel="stylesheet" href="${contextPath}/resource/rainbow/github.css"/>
<script type="text/javascript" src="${contextPath}/resource/rainbow/rainbow-custom.min.js"></script>

<script type="text/javascript">SyntaxHighlighter.all();</script>
<script type="text/javascript" src="${contextPath}/resource/finder/ajax.js"></script>
</head>
<body>
<div class="finder">
    <div class="menu-bar">
        <table class="finder">
            <tr>
                <td style="padding-left: 4px; width: 20px;">
                    <c:if test="${util.isEmpty(parent)}"><span class="backdisabled"></span></c:if>
                    <c:if test="${util.notEmpty(parent)}"><a class="back" href="/finder/display.html?path=${URLUtil.encode(parent)}"><span class="back"></span></a></c:if>
                </td>
                <td style="width: 34px;">地址: </td>
                <td>
                    <input type="text" class="address" value="${(path != '' ? path : '/')}" onkeydown="(function(event){var e = (event || window.event); var src=(e.srcElement||e.target);var keyCode = (e.keyCode || e.which); if(keyCode==13){window.location.href='/finder/display.html?path='+encodeURIComponent(src.value);}})(event);"/>
                </td>
                <td style="width: 34px;">encoding: </td>
                <td>
                    <select onchange="window.location.href='/finder/display.html?path=${URLUtil.encode(path)}&encoding=' + encodeURIComponent(this.value);">
                        <option value="" <c:if test="${util.isEmpty(encoding)}">selected="true"</c:if>>default</option>
                        <option value="UTF-8" <c:if test="${encoding == 'UTF-8'}">selected="true"</c:if>>UTF-8</option>
                        <option value="GBK" <c:if test="${encoding == 'GBK'}">selected="true"</c:if>>GBK</option>
                        <option value="GB2312" <c:if test="${encoding == 'GB2312'}">selected="true"</c:if>>GB2312</option>
                        <option value="ISO-8859-1" <c:if test="${encoding == 'ISO-8859-1'}">selected="true"</c:if>>ISO-8859-1</option>
                    </select>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <pre><code data-language="Haskell">${content}</code></pre>
    </div>
</div>
</body>
</html>