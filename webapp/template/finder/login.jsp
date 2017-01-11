<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Login</title>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    jQuery("#submit").click(function() {
        var userName = jQuery.trim(jQuery("#s1").val());
        var password = jQuery.trim(jQuery("#s2").val());
        var params = "userName=" + encodeURIComponent(userName) + "&password=" + encodeURIComponent(password);

        jQuery.ajax({
            type: "post",
            url: "${contextPath}/finder/login.html",
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
<div style="margin: 0px auto 0px auto; width: 600px;">
    <h3>User Login</h3>
    <table>
        <tr>
            <td style="height: 32px;"><input id="s1" type="text" style="width: 196px; height: 24px;" placeholder="UserName" value=""/></td>
        </tr>
        <tr>
            <td style="height: 32px;"><input id="s2" type="password" style="width: 196px; height: 24px;" placeholder="Password" value=""/></td>
        </tr>
        <tr>
            <td style="height: 32px;"><input id="submit" type="button" style="width: 200px; height: 40px;" value="login"/></td>
        </tr>
    </table>
</div>
</body>
</html>