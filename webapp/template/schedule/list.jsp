<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/include/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>定时器任务管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/style.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/cron.js"></script>
<script type="text/javascript">
//<![CDATA[
jQuery(function(){
    jQuery("table td.cron").each(function(){
        jQuery(this).html(Cron.parse(this.innerHTML));
    });

    jQuery("input.create").click(function(){
        window.location.href = "${contextPath}/schedule/add.html";
    });

    jQuery("input.build").click(function(){
        window.location.href = "${contextPath}/schedule/build.html";
    });

    jQuery("table").find("a.update").click(function(){
        window.location.href = "${contextPath}/schedule/update.html?scheduleId=" + jQuery(this).attr("scheduleId");
    });

    jQuery("table").find("a.update").click(function(){
        var scheduleId = jQuery(this).attr("scheduleId");
        window.location.href = "${contextPath}/schedule/update.html?scheduleId=" + scheduleId;
    });

    jQuery("table").find("a.delete").click(function(){
        var scheduleId = jQuery(this).attr("scheduleId");

        if(window.confirm("您确认要删除吗？"))
        {
            window.location.href = "${contextPath}/schedule/delete.html?scheduleId=" + scheduleId;
        }
    });
})
//]]>
</script>
</head>
<body>
<div style="margin: 4px 4px 4px 4px;">
    <div style="margin: 4px 4px 0px 4px; height: 30px; line-height: 30px;">
        <input class="button create" type="button" value=" 新建定时任务 "/>
        <input class="button build" type="button" value=" 重建 "/>
    </div>

    <div class="panel">
        <div class="panel-title"><h4>定时任务</h4></div>
        <div class="panel-content">
            <div class="formpanel">
                <table class="gridview" style="margin-top: -1px; margin-left: -1px;">
                    <tr class="head">
                        <td class="c1">序号</td>
                        <td class="w100">名称</td>
                        <td class="w300 center">定时器</td>
                        <td class="w200 center">动作</td>
                        <td class="w100 center">状态</td>
                        <td class="w100 center">操作</td>
                    </tr>
                    <c:forEach items="${scheduleList}" var="schedule" varStatus="status">
                    <tr>
                        <td class="c2 bb">${status.index + 1}</td>
                        <td class="bb">${schedule.scheduleName}</td>
                        <td class="cron">${schedule.expression}</td>
                        <td class="left">${schedule.action}</td>
                        <td class="center">${(schedule.status == 1 ? "有效" : "已失效")}</td>
                        <td class="center bb">
                            <a class="update" href="javascript:void(0)" scheduleId="${schedule.scheduleId}">修 改</a>
                            <a class="delete" href="javascript:void(0)" scheduleId="${schedule.scheduleId}">删 除</a>
                        </td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
<div>
<t:include file="/include/common/footer.jsp"/>
</body>
</html>
