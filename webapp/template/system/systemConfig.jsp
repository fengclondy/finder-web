<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>系统变量</title>
<meta name="keywords" content="${siteConfig.title}"/>
<meta name="description" content="${siteConfig.title}"/>
<meta name="robots" content="all"/>
<meta name="googlebot" content="all"/>
<meta name="baiduspider" content="all"/>
<meta name="copyright" content="${HtmlUtil.remove(siteConfig.copyright)}"/>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/pagebar.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/scrollpage.js"></script>
<script type="text/javascript">
<!--
var SystemVariableManager = {};

SystemVariableManager.save = function(variable){
    jQuery.ajax({
        "type": "post",
        url: "/system/config/save.html",
        data: variable,
        dataType: "json",
        error: function(){
            alert("系统错误, 请稍后再试!");
        },
        success: function(data) {
            Response.success(data, function(result) {
                alert("修改成功!");
                window.location.reload();
            });
        }
    });
};

SystemVariableManager.remove = function(variable){
    jQuery.ajax({
        "type": "post",
        url: "/system/config/delete.html",
        data: variable,
        dataType: "json",
        error: function(){
            alert("系统错误, 请稍后再试!");
        },
        success: function(data) {
            Response.success(data, function(result) {
                alert("删除成功!");
                window.location.reload();
            });
        }
    });
};

jQuery(function(){
    jQuery("#newSystemVariable").click(function(){
        jQuery("#systemVariablePanel").show();
    });

    jQuery("a.update").click(function(){
        var tr = jQuery(this).closest("tr");
        var variableName = tr.find("input[name=variableName]").val();
        var variableValue = tr.find("input[name=variableValue]").val();
        var variableDesc = tr.find("input.[name=variableDesc]").val();

        var variable = {};
        variable.variableName = variableName;
        variable.variableValue = variableValue;
        variable.variableDesc = variableDesc;
        SystemVariableManager.save(variable);
    });

    jQuery("a.delete").click(function(){
        var tr = jQuery(this).closest("tr");
        var variableName = tr.find("input[name=variableName]").val();
        var variableValue = tr.find("input[name=variableValue]").val();
        var variableDesc = tr.find("input.[name=variableDesc]").val();

        var variable = {};
        variable.variableName = variableName;
        variable.variableValue = variableValue;
        variable.variableDesc = variableDesc;
        SystemVariableManager.remove(variable);
    });

    jQuery("#save").click(function(){
        var form = jQuery("form[name=systemVariableForm]");
        var variableName = form.find("input[name=variableName]").val();
        var variableValue = form.find("input[name=variableValue]").val();
        var variableDesc = form.find("input.[name=variableDesc]").val();

        if(variableName == null || variableName == "") {
            alert("变量名不能为空！");
            return;
        }

        if(variableValue == null || variableValue == "") {
            alert("变量值不能为空！");
            return;
        }

        var variable = {};
        variable.variableName = variableName;
        variable.variableValue = variableValue;
        variable.variableDesc = variableDesc;
        SystemVariableManager.save(variable);
    });
});
//-->
</script>
</head>
<body>
<div class="menu-panel"><h4>系统变量管理</h4></div>
<div class="menu-bar">
    <div class="button-wrap">
        <a class="button" href="javascript:void(0)" onclick="window.location.reload();"><span class="refresh">&nbsp;</span>刷新</a>
        <a id="newSystemVariable" class="button" href="javascript:void(0)"><span class="refresh">&nbsp;</span>新建系统变量</a>
    </div>
</div>

<div id="systemVariablePanel" class="hide">
    <form name="systemVariableForm">
        <table class="form">
            <tr>
                <td class="w80 right">变量名：</td>
                <td><input name="variableName" type="text" class="w300 text" value=""/></td>
            </tr>
            <tr>
                <td class="w80 right">变量值：</td>
                <td><input name="variableValue" type="text" class="w300 text" value=""/></td>
            </tr>
            <tr>
                <td class="w80 right">变量描述：</td>
                <td><input name="variableDesc" type="text" class="w300 text" value=""/></td>
            </tr>
            <tr>
                <td colspan="2"><input id="save" class="button" type="button" value=" 保存 "/></td>
            </tr>
        </table>
    </form>
</div>

<table class="list">
    <tr class="head">
        <td class="w60">&nbsp;</td>
        <td class="w200">变量名</td>
        <td class="w300">变量值</td>
        <td class="w300">变量描述</td>
        <td>操 作</td>
    </tr>
    <c:forEach items="${systemVariableList}" var="systemVariable" varStatus="status">
        <tr>
            <td>${status.index + 1}</td>
            <td><input name="variableName" type="hidden" value="${systemVariable.variableName}"/><span class="bb">${systemVariable.variableName}</span></td>
            <td><input name="variableValue" type="text" class="text2 w292" value="${systemVariable.variableValue}"/></td>
            <td><input name="variableDesc" type="text" class="text2 w292" value="${systemVariable.variableDesc}"/></td>
            <td>
                <a class="btn update" href="javascript:void(0)">保存修改</a>
                <a class="btn delete" href="javascript:void(0)">删 除</a>
            </td>
        </tr>
    </c:forEach>
</table>
<div class="pagebar">
    <div name="scrollpage" class="scrollpage" theme="2" pageNum="${pageNum}" pageSize="${pageSize}" count="${variableCount}" pattern="" href="${siteConfig.host}/system/config.html?pageNum=%s"></div>
</div>
<%@include file="/include/common/footer.jsp"%>
</body>
</html>