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
<link rel="stylesheet" type="text/css" href="${domainConfig.resource}/resource/finder/css/style.css"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/domain.min.js"></script>
<script type="text/javascript">
<!--
var SystemVariableManager = {};

SystemVariableManager.save = function(variable){
    jQuery.ajax({
        "type": "post",
        url: "/ajax/system/config/save.html",
        data: variable,
        dataType: "json",
        error: function(){
            alert("系统错误, 请稍后再试!");
        },
        success: function(data){
            if(data == null)
            {
                data = {"code": -1, "message": "系统错误, 请稍后再试!"};
            }

            if(data.code == 0)
            {
                alert("修改成功!");
                window.location.reload();
            }
            else
            {
                alert(data.message);
            }
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

    jQuery("#save").click(function(){
        var form = jQuery("form[name=systemVariableForm]");
        var variableName = form.find("input[name=variableName]").val();
        var variableValue = form.find("input[name=variableValue]").val();
        var variableDesc = form.find("input.[name=variableDesc]").val();

        if(variableName == null || variableName == "")
        {
            alert("变量名不能为空！");
            return;
        }

        if(variableValue == null || variableValue == "")
        {
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
<div style="margin: 4px 4px 4px 4px;">
    <div style="margin: 4px 4px 0px 4px; height: 30px; line-height: 30px;">
        <input id="newSystemVariable" class="button create" type="button" value=" 新建系统变量 "/>
    </div>

    <div class="panel">
        <div class="panel-title"><h4>系统变量</h4></div>
        <div class="panel-content">
            <div class="formpanel">
                <table class="gridview" style="margin-top: -1px; margin-left: -1px;">
                    <tr class="head">
                        <td class="w60">&nbsp;</td>
                        <td class="w200">变量名</td>
                        <td class="w300">变量值</td>
                        <td>变量描述</td>
                        <td class="w150">操 作</td>
                    </tr>
                    <c:forEach items="${systemVariableList}" var="systemVariable" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td><input name="variableName" type="hidden" value="${HtmlUtil.encode(systemVariable.variableName)}"/><span class="bb">${HtmlUtil.encode(systemVariable.variableName)}</span></td>
                            <td><input name="variableValue" type="text" class="w300" value="${HtmlUtil.encode(systemVariable.variableValue)}"/></td>
                            <td><input name="variableDesc" type="text" class="w300" value="${HtmlUtil.encode(systemVariable.variableDesc)}"/></td>
                            <td>
                                <a class="update" href="javascript:void(0)">保存修改</a>
                                <a class="delete" href="/system/config/delete.html?variableName=${URLUtil.encode(systemVariable.variableName)}">删 除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="pagebar">
                    <c:scrollpage pageNum="${pageNum}" pageSize="${pageSize}" count="${variableCount}" className="scrollpage" pattern="" href="${domainConfig.host}/system/config.html?pageNum=%s"/>
                </div>
            </div>
        </div>
    </div>
<div>

<div id="systemVariablePanel" class="hide">
    <form name="systemVariableForm">
    <table class="list">
        <tr>
            <td class="w80 right">变量名：</td>
            <td><input name="variableName" type="text" class="w300" value=""/></td>
        </tr>
        <tr>
            <td class="w80 right">变量值：</td>
            <td><input name="variableValue" type="text" class="w300" value=""/></td>
        </tr>
        <tr>
            <td class="w80 right">变量描述：</td>
            <td><input name="variableDesc" type="text" class="w300" value=""/></td>
        </tr>
    </table>
    <input id="save" class="button" type="button" value=" 保存 "/>
    </form>
</div>
<t:include file="/include/common/footer.jsp"/>
</body>
</html>