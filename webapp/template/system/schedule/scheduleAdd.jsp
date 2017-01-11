<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/include/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>定时任务</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/pagebar.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/base.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/cron.js"></script>
<script type="text/javascript">
//<![CDATA[
jQuery(function(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth();

    var getOptions = function(i, j){
        var options = [];

        for(var k = i; k <= j; k++) {
            options[options.length] = {"text": k, "value": k};
        }
        return options;
    };

    var setOptions = function(element, options, value) {
        if(element != null) {
            var nodeName = element.nodeName.toLowerCase();

            if(nodeName == "select") {
                for(var i = element.length - 1; i > -1; i--) {
                    element.options.remove(i);
                }

                for(var i = 0; i < options.length; i++) {
                    element.options.add(new Option(options[i].text, options[i].value));
                }

                if(value != null) {
                    element.value = value;
                }
            }
        }
    };

    setOptions(jQuery("div.year select[name=start]").get(0), getOptions(year, year + 10), year);
    setOptions(jQuery("div.year select[name=end]").get(0), getOptions(year, year + 10), year);
    setOptions(jQuery("div.month select[name=start]").get(0), getOptions(1, 12), 10);
    setOptions(jQuery("div.month select[name=end]").get(0), getOptions(1, 12), 10);
    setOptions(jQuery("div.day select[name=start]").get(0), getOptions(1, 31), 1);
    setOptions(jQuery("div.day select[name=end]").get(0), getOptions(1, 31), 31);
    setOptions(jQuery("div.hour select[name=start]").get(0), getOptions(0, 23), 6);
    setOptions(jQuery("div.hour select[name=end]").get(0), getOptions(0, 23), 6);
    setOptions(jQuery("div.minute select[name=start]").get(0), getOptions(0, 59), 0);
    setOptions(jQuery("div.minute select[name=end]").get(0), getOptions(0, 59), 0);

    jQuery("div.setting input[type=checkbox]").change(function() {
        var parent = jQuery(this).parent();

        if(this.checked == true) {
            parent.find("select").attr("disabled", true);
            parent.find("input[type=text]").val("*");
        }
        else {
            parent.find("select").attr("disabled", false);
        }
        parent.find("select[name=start], select[name=end]").change();
    });

    jQuery("select[name=start], select[name=end]").change(function() {
        var parent = jQuery(this).parent();
        var start = parent.find("select[name=start]").val();
        var end = parent.find("select[name=end]").val();

        if(parseInt(start) > parseInt(end)) {
            end = start;
            parent.find("select[name=end]").val(start);
        }

        var value = (start != end ? (start + "-" + end) : start);
        var every = (parent.find("input[type=checkbox]:checked").size() > 0);

        if(every  == true || (value == "1-31") || (value == "1-12") || (value == "0-59")) {
            parent.find("input[type=text]").val("*");
        }
        else {
            parent.find("input[type=text]").val(value);
        }
        jQuery("div.minute input[name=minute]").change();
    });

    jQuery("div.minute input[name=minute], div.hour input[name=hour], div.day input[name=day], div.month input[name=month], div.year input[name=year]").change(function() {
        var expression = [];
        expression[expression.length] = "0";
        expression[expression.length] = jQuery("div.minute input[name=minute]").val();
        expression[expression.length] = jQuery("div.hour input[name=hour]").val();
        expression[expression.length] = jQuery("div.day input[name=day]").val();
        expression[expression.length] = jQuery("div.month input[name=month]").val();
        expression[expression.length] = "?";
        expression[expression.length] = jQuery("div.year input[name=year]").val();
        jQuery("div.expression input[name=expression]").val(expression.join(" "));
        jQuery("div.remark span.remark").html(Cron.parse(expression.join(" ")));
    });

    jQuery("input[type=checkbox]").change();

    jQuery("a.ensure").click(function(){
        var scheduleName = jQuery("input[name=scheduleName]").val();
        var description = jQuery("input[name=description]").val();

        if(jQuery.trim(scheduleName).length < 1) {
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
        params.scheduleName = scheduleName;
        params.description = description;
        params.expression = expression;
        params.action = action;
        params.properties = properties;

        jQuery.ajax({
            "type": "post",
            "url": "${contextPath}/system/schedule/create.html",
            "data": jQuery.param(params, true),
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(data) {
                Response.success(data, function(result) {
                    alert("创建成功！");
                    window.history.back();
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
<div class="menu-panel"><h4>新建定时任务</h4></div>
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
            <td><input name="scheduleName" type="text" class="w300 text" value="HelloJob"/></td>
        </tr>
        <tr>
            <td class="w80 bb">定时器描述：</td>
            <td><input name="description" type="text" class="w300 text" value="定时输出Hello"/></td>
        </tr>
        <tr>
            <td class="w80 bb">定时器动作：</td>
            <td>
                <input name="action" type="text" class="w300 text" value="com.skin.finder.schedule.HelloJob"/>
                <span class="red bb">[注]：</span><span> 定时任务执行的操作。</span>
            </td>
        </tr>
        <tr>
            <td class="w80 bb" style="vertical-align: top;">定时器参数：</td>
            <td>
                <div><textarea name="properties" class="w300 text"></textarea></div>
                <div class="red bb">[注]：</span><span> 定时器参数，可以是任意格式，定时器执行类能够自己识别的格式即可。</div>
            </td>
        </tr>
        <tr>
            <td class="w80 bb" style="vertical-align: top;">定时表达式：</td>
            <td>
                <div class="setting">
                    <div class="h30 year"><input name="year" type="text" class="w60"/> 从 <select name="start" class="w60"></select> 年 到 <select name="end" class="w60"></select> 年 <input type="checkbox"/> 每年</div>
                    <div class="h30 month"><input name="month" type="text" class="w60"/> 从 <select name="start" class="w60"></select> 月 到 <select name="end" class="w60"></select> 月 <input type="checkbox"/> 每月</div>
                    <div class="h30 day"><input name="day" type="text" class="w60"/> 从 <select name="start" class="w60"></select> 日 到 <select name="end" class="w60"></select> 日 <input type="checkbox"/> 每天</div>
                    <div class="h30 hour"><input name="hour" type="text" class="w60"/> 从 <select name="start" class="w60"></select> 时 到 <select name="end" class="w60"></select> 时 <input type="checkbox"/> 每时</div>
                    <div class="h30 minute"><input name="minute" type="text" class="w60"/> 从 <select name="start" class="w60"></select> 分 到 <select name="end" class="w60"></select> 分 <input type="checkbox"/> 每分</div>
                    <div class="h30 expression"><span class="red bb">结果: </span><input name="expression" type="text" class="w300" disabled="true"/></div>
                    <div class="h30 remark"><span class="red bb">描述: </span><span class=" bb remark"></span></div>
                </div>
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
