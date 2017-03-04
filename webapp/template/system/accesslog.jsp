<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>用户管理</title>
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
<script type="text/javascript" src="${contextPath}/resource/finder/base.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/base64.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/scrollpage.js"></script>
<script type="text/javascript">
<!--
var UserManager = {};
UserManager.parse = function(certificate){
    var passport = (certificate != null ? certificate : CookieUtil.getCookie("passport"));
    var values = (passport != null ? passport.split("|") : []);

    if(values.length >= 6) {
        var user = {};
        user.sessionId = Base64.decode(values[0]);
        user.appId = Base64.decode(values[1]);
        user.userId = Base64.decode(values[2]);
        user.userName = Base64.decode(values[3]);
        user.nickName = Base64.decode(values[4]);
        user.createTime = Base64.decode(values[5]);
        return user;
    }
    return null;
};

jQuery(function(){
    var getCertificate = function(values){
        var cookies = values.split(";");
        var name = "passport";
        var prefix = name + "=";

        for(var i = 0; i < cookies.length; i++) {
            var cookie = StringUtil.trim(cookies[i]);

            if(cookie.substring(0, name.length + 1) == prefix) {
                return decodeURIComponent(cookie.substring(name.length + 1));
            }
        }
        return null;
    };

    jQuery("table.list span.user-info").each(function(){
        var user = null;
        var certificate = getCertificate(jQuery(this).attr("clientCookie"));

        if(certificate != null) {
            user = UserManager.parse(certificate);
        }

        if(user != null) {
            jQuery(this).html("<a href=\"/user/" + user.userId + ".html\" target=\"_blank\">" + user.nickName + "</a>");
            jQuery(this).attr("title", "userId: " + user.userId);
        }
        else {
            jQuery(this).html("无");
        }
    });
});
//-->
</script>
</head>
<body>
<div class="menu-panel"><h4>访问日志</h4></div>
<div class="menu-bar">
    <div class="button-wrap">
        <a class="button" href="javascript:void(0)" onclick="window.location.reload();"><span class="refresh">&nbsp;</span>刷新</a>
    </div>
</div>
<table class="table highlight">
    <tr class="head">
        <td class="w150">AccessTime</td>
        <td class="w100">IP</td>
        <td class="w240">URL</td>
        <td class="w240">Referer</td>
        <td class="w240">ClientId</td>
        <td>User</td>
    </tr>
    <c:forEach items="${accessLogList}" var="accessLog" varStatus="status">
    <tr>
        <td title="${status.index + 1}">${DateUtil.format(accessLog.accessTime, 'yyyy-MM-dd HH:mm:ss')}</td>
        <td>${accessLog.remoteHost}</td>
        <td><span class="w200 ellipsis" title="${accessLog.requestUrl}"><a href="${accessLog.requestUrl}" target="_blank">${StringUtil.substring(accessLog.requestUrl, 30, "...")}</a></span></td>
        <td><span class="w200 ellipsis" title="${URLUtil.decode(accessLog.requestReferer)}">${StringUtil.substring(accessLog.requestReferer, 30, "...")}</span></td>
        <td>${accessLog.clientId}</td>
        <td><span class="user-info" clientCookie="${accessLog.clientCookie}" title="${accessLog.clientUserAgent}">${accessLog.userName}</span></td>
    </tr>
    </c:forEach>
</table>
<div class="pagebar">
    <div name="scrollpage" class="scrollpage" theme="2" pageNum="${pageNum}" pageSize="${pageSize}" count="${logCount}" href="${siteConfig.host}/system/accesslog/index.html?pageNum=%s"></div>
</div>
<%@include file="/include/common/footer.jsp"%>
</body>
</html>
