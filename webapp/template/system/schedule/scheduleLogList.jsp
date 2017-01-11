<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/include/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>任务日志</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/pagebar.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/scrollpage.js"></script>
</head>
<body>
<div class="menu-panel"><h4>任务日志</h4></div>
<div class="menu-bar">
    <a class="button" href="javascript:void(0)" onclick="window.history.back();"><span class="back">&nbsp;</span>返回</a>
    <a class="button" href="javascript:void(0)" onclick="window.location.reload();"><span class="refresh">&nbsp;</span>刷新</a>
</div>
<table id="friendSiteList" class="list">
    <tr class="head">
        <td class="w60">序号</td>
        <td class="w150">任务名称</td>
        <td class="w100">调用者</td>
        <td class="w200">执行时间</td>
        <td class="w200">下次执行</td>
        <td class="w100">执行状态</td>
        <td>执行结果</td>
    </tr>
    <c:forEach items="${scheduleLogList}" var="scheduleLog" varStatus="status">
    <tr>
        <td>${status.index + 1}</td>
        <td>${scheduleLog.scheduleName}</td>
        <td>${scheduleLog.invocation}</td>
        <td>${DateUtil.format(scheduleLog.fireTime, "yyyy-MM-dd HH:mm:ss")}</td>
        <td>${DateUtil.format(scheduleLog.nextFireTime, "yyyy-MM-dd HH:mm:ss")}</td>
        <td class="left">${scheduleLog.executeStatus == 200 ? "成功": "失败"}</td>
        <td class="left">${scheduleLog.executeResult}</td>
    </tr>
    </c:forEach>
</table>
<div class="pagebar">
    <div name="scrollpage" class="scrollpage" theme="2" pageNum="${pageNum}" pageSize="${pageSize}" count="${logCount}"></div>
</div>
<%@include file="/include/common/footer.jsp"%>
</body>
</html>
