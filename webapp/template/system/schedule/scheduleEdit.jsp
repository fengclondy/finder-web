<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/include/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>编辑定时任务</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/pagebar.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/base.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/cron.js"></script>
<script type="text/javascript">
//<![CDATA[
jQuery(function(){
    jQuery("a.ensure").click(function(){
        var scheduleId = jQuery("input[name=scheduleId]").val();
        var scheduleName = jQuery("input[name=scheduleName]").val();
        var description = jQuery("input[name=description]").val();

        if(jQuery.trim(scheduleId).length < 1 || jQuery.trim(scheduleName).length < 1) {
            alert("名称不能为空 !");
            return false;
        }

        if(jQuery.trim(description).length < 1) {
            alert("描述不能为空 !");
            return false;
        }

        var action = jQuery("input[name=action]").val();
        var expression = jQuery("input[name=expression]").val();
        var properties = jQuery("textarea[name=properties]").val();
        var params = {};
        params.scheduleId = scheduleId;
        params.scheduleName = scheduleName;
        params.description = description;
        params.expression = expression;
        params.action = action;
        params.properties = properties;

        jQuery.ajax({
            "type": "post",
            "url": "${contextPath}/system/schedule/save.html",
            "data": jQuery.param(params, true),
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(data) {
                Response.success(data, function(result) {
                    alert("修改成功！");
                    window.location.href = "${contextPath}/system/schedule/list.html";
                });
            }
        });
    });

    jQuery("a.cancel").click(function(){
        window.location.href = "${contextPath}/system/schedule/list.html";
    });
})
//]]>
</script>
</head>
<body>
<div class="menu-panel"><h4>编辑定时任务</h4></div>
<div class="menu-bar">
    <a class="button" href="javascript:void(0)" onclick="window.history.back();"><span class="back">&nbsp;</span>返回</a>
    <a class="button" href="javascript:void(0)" onclick="window.location.reload();"><span class="refresh">&nbsp;</span>刷新</a>
    <div class="button-wrap">
        <a class="button ensure" href="javascript:void(0)"><span class="add">&nbsp;</span>保 存</a>
        <a class="button cancel"  href="javascript:void(0)"><span class="delete">&nbsp;</span>取 消</a>
    </div>
</div>

<div>
    <table>
        <tr>
            <td class="w80 bb">定时器名称：</td>
            <td>
                <input name="scheduleId" type="hidden" value="${schedule.scheduleId}"/>
                <input name="scheduleName" type="text" class="w300 text" value="${schedule.scheduleName}"/>
            </td>
        </tr>
        <tr>
            <td class="w80 bb">定时器描述：</td>
            <td><input name="description" type="text" class="w300 text" value="${schedule.description}"/></td>
        </tr>
        <tr>
            <td class="w80 bb">定时器动作：</td>
            <td>
                <input name="action" type="text" class="w300 text" value="${schedule.action}"/>
                <span class="red bb">[注]：</span><span> 定时任务执行的操作。</span>
            </td>
        </tr>
        <tr>
            <td class="w80 bb" style="vertical-align: top;">定时器参数：</td>
            <td>
                <div><textarea name="properties" class="w300 text">${schedule.properties}</textarea></div>
                <div><span class="red bb">[注]：</span><span> 定时器参数，可以是任意格式，定时器执行类能够自己识别的格式即可。</span></div>
            </td>
        </tr>
        <tr>
            <td class="w80 bb" style="vertical-align: top;">定时表达式：</td>
            <td>
                <div><input name="expression" type="text" class="w300 text" value="${schedule.expression}"/></div>
                
                <div style="line-height: 24px;">
                    <p><span class="red bb">格式: [秒] [分] [小时] [日] [月] [周] [年]</span> 其中周和秒不用选，采用默认值。</p>
                    <p><span class="red bb">*</span> 表示所有值。例如:在分的字段上设置 "*"，表示每一分钟都会触发。</p>
                    <p><span class="red bb">-</span> 表示区间。例如 在小时上设置 "10-12"，表示 10,11,12点都会触发。</p>
                    <p><span class="red bb">/</span> 表示递增触发。例如 在分钟上设置 "5/15" 表示从第5分钟开始，每增15分钟触发一次(5,20,35,50,55)。</p>
                    <p><span class="red bb">示例:</span></p>
                    <p><span class="red bb">0 0 6 * 10 ? 2012</span> 表示2012年的10月每天早上6点触发。</p>
                </div>
            </td>
        </tr>
    </table>
</div>
<%@include file="/include/common/footer.jsp"%>
</body>
</html>
