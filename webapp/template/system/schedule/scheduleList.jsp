<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/include/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>定时器任务管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/pagebar.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/base.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/cron.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/scrollpage.js"></script>
<script type="text/javascript">
//<![CDATA[
jQuery(function(){
    jQuery("table td p.cron").each(function() {
        jQuery(this).html(Cron.parse(this.innerHTML));
    });

    jQuery("a.create").click(function() {
        window.location.href = "${contextPath}/system/schedule/add.html";
    });

    jQuery("a.build").click(function() {
        if(!window.confirm("重建任务将会重新启动全部定时任务，您确定要重建吗？")) {
            return;
        }

        jQuery.ajax({
            "type": "post",
            "url": "${contextPath}/system/schedule/build.html",
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(data) {
                Response.success(data, function(result) {
                    alert("重建成功！");
                    window.location.reload();
                });
            }
        });
    });

    jQuery("table a.setStatus").click(function() {
        var scheduleId = jQuery(this).attr("scheduleId");
        var status = jQuery(this).attr("status");

        jQuery.ajax({
            "type": "post",
            "url": "${contextPath}/system/schedule/setStatus.html?scheduleId=" + scheduleId + "&status=" + status,
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(data) {
                Response.success(data, function(result) {
                    alert("操作成功！");
                    window.location.reload();
                });
            }
        });
    });

    jQuery("table a.detail").click(function() {
        var scheduleId = jQuery(this).attr("scheduleId");
        window.location.href = "${contextPath}/system/schedule/log.html?scheduleId=" + scheduleId;
    });

    jQuery("table a.invoke").click(function() {
        if(!window.confirm("您确认要手动执行任务吗？")) {
            return;
        }

        var scheduleId = jQuery(this).attr("scheduleId");
        jQuery.ajax({
            "type": "post",
            "url": "${contextPath}/system/schedule/execute.html?scheduleId=" + scheduleId,
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(data) {
                Response.success(data, function(result) {
                    alert("任务已经启动执行，请稍后刷新页面查看执行结果！");
                });
            }
        });
    });

    jQuery("table a.update").click(function() {
        var scheduleId = jQuery(this).attr("scheduleId");
        window.location.href = "${contextPath}/system/schedule/update.html?scheduleId=" + scheduleId;
    });

    jQuery("table a.delete").click(function() {
        if(!window.confirm("您确认要删除该任务吗？")) {
            return;
        }

        var scheduleId = jQuery(this).attr("scheduleId");
        jQuery.ajax({
            "type": "post",
            "url": "${contextPath}/system/schedule/delete.html?scheduleId=" + scheduleId,
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(data) {
                Response.success(data, function(result) {
                    alert("删除成功！");
                    window.location.reload();
                });
            }
        });
    });
})
//]]>
</script>
</head>
<body>
<div class="menu-panel"><h4>定时任务管理</h4></div>
<div class="menu-bar">
    <a class="button back" href="javascript:void(0)">返回</a>
    <a class="button refresh" href="javascript:void(0)">刷新</a>
    <div class="button-wrap">
        <a class="button create" href="javascript:void(0)">新建定时任务</a>
        <a class="button build"  href="javascript:void(0)">重启全部任务</a>
    </div>
</div>
<table id="friendSiteList" class="list">
    <tr class="head">
        <td class="w60">序号</td>
        <td class="w200">名称</td>
        <td class="w300">定时器</td>
        <td class="w200">执行时间</td>
        <td class="w150">状态</td>
        <td>操作</td>
    </tr>
    <c:forEach items="${scheduleList}" var="schedule" varStatus="status">
    <tr>
        <td>${status.index + 1}</td>
        <td>
            <div>${schedule.scheduleName}</div>
            <div class="w180 ellipsis second" title="${schedule.description}">${schedule.description}</div>
        </td>
        <td>
            <p class="cron">${schedule.expression}</p>
            <p>${schedule.action}</p>
        </td>
        <td>
            <p>上次执行：${DateUtil.format(schedule.lastFireTime, "yyyy-MM-dd HH:mm:ss")}</p>
            <p class="second">下次执行：${DateUtil.format(schedule.nextFireTime, "yyyy-MM-dd HH:mm:ss")}</p>
        </td>
        <td class="left">
            <p title="${schedule.executeResult}">上次执行结果：${schedule.executeStatus == 200 ? "成功": "失败"}</p>
            <p>
            <c:if test="${schedule.status == 1}">
                状态：<span style="color: #ff4400;">正常</span>
                <a class="setStatus" href="javascript:void(0)" scheduleId="${schedule.scheduleId}" status="0">禁 用</a>
            </c:if>
            <c:if test="${schedule.status != 1}">
                状态：<span style="color: #ff0000;">停止</span>
                <a class="setStatus" href="javascript:void(0)" scheduleId="${schedule.scheduleId}" status="1">启 动</a>
            </c:if>
            </p>
        </td>
        <td>
            <p>
                <a class="btn detail" href="javascript:void(0)" scheduleId="${schedule.scheduleId}">日 志</a>
                <a class="btn invoke" href="javascript:void(0)" scheduleId="${schedule.scheduleId}">执 行</a>
            </p>
            <p>
                <a class="btn update" href="javascript:void(0)" scheduleId="${schedule.scheduleId}">修 改</a>
                <a class="btn delete" href="javascript:void(0)" scheduleId="${schedule.scheduleId}">删 除</a>
            </p>
        </td>
    </tr>
    </c:forEach>
</table>
<div class="pagebar">
    <div name="scrollpage" class="scrollpage" theme="2" pageNum="${pageNum}" pageSize="${pageSize}" count="${scheduleCount}"></div>
</div>
<%@include file="/include/common/footer.jsp"%>
</body>
</html>
