<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Login</title>
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
            url: requestURI + "?action=finder.login",
            dataType: "json",
            data: params,
            error: function(req, status, error) {
                alert("系统错误，请稍后再试！");
            },
            success: function(result) {
                if(result.status == 200) {
                    window.location.href = "${contextPath}/index.html";
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
        <h3>登录</h3>
        <div class="login-panel">
            <div class="row"><input id="s1" type="text" class="text" spellcheck="false" placeholder="UserName" value=""/></div>
            <div class="row"><input id="s2" type="password" class="text" placeholder="Password" value=""/></div>
            <div class="row"><input id="submit" type="button" class="button" value="登录"/></div>
        </div>
    </div>
</div>
</body>
</html>
