<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Add User</title>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/finder/css/user.css"/>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    jQuery("#submit").click(function() {
        var userName = jQuery.trim(jQuery("#s1").val());
        var password = jQuery.trim(jQuery("#s2").val());
        var params = "userName=" + encodeURIComponent(userName) + "&password=" + encodeURIComponent(password);
        var requestURI = window.location.pathname;

        jQuery.ajax({
            type: "post",
            url: requestURI + "?action=user.save",
            dataType: "json",
            data: params,
            error: function(req, status, error) {
                alert("系统错误，请稍后再试！");
            },
            success: function(result) {
                if(result.status == 200) {
                    alert("添加用户成功！");
                    window.location.reload();
                }
                else {
                    alert(result.message);
                }
            }
        });
    });
});
//-->
</script>
</head>
<body>
<div class="wrap">
    <div class="login-container">
        <h3>添加用户</h3>
        <div class="login-panel">
            <div class="row"><input id="s1" type="text" class="text" spellcheck="false" placeholder="UserName" value=""/></div>
            <div class="row"><input id="s2" type="password" class="text" placeholder="Password" value=""/></div>
            <div class="row"><input id="submit" type="button" class="button" value="提交"/></div>
        </div>
    </div>
</div>
</body>
</html>