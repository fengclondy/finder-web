<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>系统信息</title>
<meta name="keywords" content="${siteConfig.title}"/>
<meta name="description" content="${siteConfig.title}"/>
<meta name="robots" content="all"/>
<meta name="googlebot" content="all"/>
<meta name="baiduspider" content="all"/>
<meta name="copyright" content="${HtmlUtil.remove(siteConfig.copyright)}"/>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<link rel="shortcut icon" href="/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="${domainConfig.resource}/resource/finder/css/style.css"/>
<script type="text/javascript" src="${domainConfig.resource}/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/finder/jquery-1.4.2.min.js"></script>
</head>
<body>
<div style="margin: 4px 4px 4px 4px;">
    <div class="panel">
        <div class="panel-title"><h4>系统信息</h4></div>
        <div class="panel-content">
            <table class="gridview">
                <!-- Server Info -->
                <tr>
                    <td class="bb gray" colspan="2">Server Info</td>
                </tr>
                <tr>
                    <td class="w200">Server</td>
                    <td>${servletInfo.serverInfo}</td>
                </tr>
                <tr>
                    <td>ServletVersion</td>
                    <td>${servletInfo.servletVersion}</td>
                </tr>
                <!-- Database Info -->
                <tr>
                    <td class="bb gray" colspan="2">Database Info</td>
                </tr>
                <tr>
                    <td>DatabaseName</td>
                    <td>${databaseInfo.productName}</td>
                </tr>
                <tr>
                    <td>DatabaseVersion</td>
                    <td>${databaseInfo.productVersion}</td>
                </tr>
                <tr>
                    <td>URL</td>
                    <td>${HtmlUtil.encode(databaseInfo.url)}</td>
                </tr>
                <tr>
                    <td>UserName</td>
                    <td>******@localhost</td>
                </tr>
                <tr>
                    <td>DriverName</td>
                    <td>${databaseInfo.driverName}</td>
                </tr>
                <tr>
                    <td>DriverVersion</td>
                    <td>${databaseInfo.driverVersion}</td>
                </tr>
                <!-- System Info -->
                <tr>
                    <td class="bb gray" colspan="2">System Info</td>
                </tr>
                <tr>
                    <td>OS</td>
                    <td>${systemInfo.osName}</td>
                </tr>
                <tr>
                    <td>Version</td>
                    <td>${systemInfo.osVersion}</td>
                </tr>
                <tr>
                    <td>CPU</td>
                    <td>${systemInfo.cpu}</td>
                </tr>

                <!-- JVM Info -->
                <tr>
                    <td class="bb gray" colspan="2">JVM Info</td>
                </tr>
                <tr>
                    <td>Virtual Machine Name</td>
                    <td>${systemInfo.vmName}</td>
                </tr>
                <tr>
                    <td>Virtual Machine Vendor</td>
                    <td>${systemInfo.vmVendor}</td>
                </tr>
                <tr>
                    <td>Virtual Machine Version</td>
                    <td>${systemInfo.vmVersion}</td>
                </tr>
                <tr>
                    <td>Runtime Name</td>
                    <td>${systemInfo.runtimeName}</td>
                </tr>
                <tr>
                    <td>Runtime Version</td>
                    <td>${systemInfo.runtimeVersion}</td>
                </tr>
                <tr>
                    <td>Max Memory</td>
                    <td>${systemInfo.maxMemory} (${systemInfo.maxMemory / 1048576}M)</td>
                </tr>
                <tr>
                    <td>Total Memory</td>
                    <td>${systemInfo.totalMemory} (${systemInfo.totalMemory / 1048576}M)</td>
                </tr>
                <tr>
                    <td>Free Memory</td>
                    <td>${systemInfo.freeMemory} (${systemInfo.freeMemory / 1048576}M)</td>
                </tr>
                <!-- Version Info -->
                <tr>
                    <td class="bb gray" colspan="2">Version Info</td>
                </tr>
                <tr>
                    <td>version</td>
                    <td><c:if test="${util.notNull(version)}">${version.getVersionName()}</c:if></td>
                </tr>
                <tr>
                    <td>features</td>
                    <td>${version.features}</td>
                </tr>
                <tr>
                    <td>dependencies</td>
                    <td>${version.dependencies}</td>
                </tr>
                <tr>
                    <td>developer</td>
                    <td>${version.developer}</td>
                </tr>
                <tr>
                    <td>buildTime</td>
                    <td><fmt:formatDate value="${version.buildTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
                <tr>
                    <td>updateTime</td>
                    <td><fmt:formatDate value="${version.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </table>
        </div>
    </div>
<div>
<t:include file="/include/common/footer.jsp"/>
</body>
</html>