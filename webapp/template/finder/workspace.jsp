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
<title>Select Workspace</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/finder.css"/>
<script type="text/javascript">
window.onload = function(){
    var table = document.getElementById("workspaceList");

    if(table.rows.length == 2) {
        var workspace = table.rows[1].getAttribute("workspace");
        window.location.href = "${contextPath}/finder/index.html?workspace=" + encodeURIComponent(workspace);
    }
};
</script>
</head>
<body>
<div class="finder">
    <div class="menu-bar"><h4>&nbsp;Select Workspace</h4></div>
    <div class="h20"></div>
    <div>
        <table id="workspaceList" class="finder">
            <tr style="background-color: #efefef;">
                <td>Workspace</td>
            </tr>
            <c:forEach items="${workspaces}" var="workspace" varStatus="status">
            <tr workspace="${workspace}">
                <td><img src="${contextPath}/resource/finder/images/folder.gif"/>&nbsp;<a class="file" href="${contextPath}/finder/index.html?workspace=${URLUtil.encode(workspace)}" title="${workspace}">${workspace}</a></td>
            </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
