<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/include/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head><!-- 添加定时任务 -->
<title>定时任务</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="stylesheet" type="text/css" href="${domainConfig.resource}/resource/finder/css/style.css"/>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/cron.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/widget.js"></script>
<script type="text/javascript">
//<![CDATA[
jQuery(function(){
    jQuery("input[name=save]").click(function(){
        var scheduleId = jQuery("div.schedule input[name=scheduleId]").val();
        var scheduleName = jQuery("div.schedule input[name=scheduleName]").val();

        if(jQuery.trim(scheduleId).length < 1 || jQuery.trim(scheduleName).length < 1)
        {
            alert("名称不能为空 !");
            return false;
        }

        var action = jQuery("div.schedule input[name=action]").val();
        var expression = jQuery("div.schedule input[name=expression]").val();
        var form = document.scheduleForm.elements;

        form["scheduleId"].value = scheduleId;
        form["scheduleName"].value = scheduleName;
        form["expression"].value = expression;
        form["action"].value = action;
        document.scheduleForm.submit();
    });

    jQuery("input[name=cancel]").click(function(){
        window.location.href = "${contextPath}/schedule/list.html";
    });
})
//]]>
</script>
</head>
<body>
<div style="margin: 4px 4px 4px 4px;">
    <div class="panel">
        <div class="panel-title"><h4>定时任务</h4></div>
        <div class="panel-content">
            <div class="formpanel" style="padding-top: 0px; padding-left: 0px; overflow-x: hide;">
                <div class="schedule" style="padding: 10px;">
                    <input type="hidden" name="scheduleId" value="${schedule.scheduleId}"/>
                    <div class="h30"><span class="bb">定时器名称: </span><input name="scheduleName" type="text" value="${schedule.scheduleName}"/></div>
                    <div class="h30">
                        <span class="bb">定时器动作: </span>
                        <input name="action" type="text" class="w300" value="${schedule.action}"/>
                        <span class="red bb">[注]:</span><span> 定时任务执行的操作。</span>
                    </div>
                    <div class="setting">
                        <span class="bb">定时器动作: </span>
                        <input name="expression" type="text" class="w300" value="${schedule.expression}"/>
                    </div>
                    <div style="line-height: 24px;">
                        <p><span class="red bb">格式: [秒] [分] [小时] [日] [月] [周] [年]</span> 其中周和秒不用选，采用默认值。</p>
                        <p><span class="red bb">*</span> 表示所有值。例如:在分的字段上设置 "*"，表示每一分钟都会触发。</p>
                        <p><span class="red bb">-</span> 表示区间。例如 在小时上设置 "10-12"，表示 10,11,12点都会触发。</p>
                        <p><span class="red bb">/</span> 表示递增触发。例如 在分钟上设置 "5/15" 表示从第5分钟开始，每增15分钟触发一次(5,20,35,50,55)。</p>
                        <p><span class="red bb">示例:</span></p>
                        <p><span class="red bb">0 0 6 * 10 ? 2012</span> 表示2012年的10月每天早上6点触发。</p>
                    </div>
                    <div class="h20"></div>
                    <div>
                        <input name="save" type="button" class="button w60" value="保 存"/>
                        <input name="cancel" type="button" class="button w60" value="取 消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
<div>

<div class="hide">
    <form name="scheduleForm" method="post" action="${contextPath}/schedule/save.html">
        <input name="scheduleId" type="hidden"/>
        <input name="scheduleName" type="hidden"/>
        <input name="expression" type="hidden"/>
        <input name="action" type="hidden"/>
    </form>
</div>
<t:include file="/include/common/footer.jsp"/>
</body>
</html>
