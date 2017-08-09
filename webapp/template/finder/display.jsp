<%@ page contentType="text/html; charset=utf-8"%>
<%
    String path = (String)(request.getAttribute("path"));
    String theme = (String)(request.getAttribute("theme"));
    String type = (String)(request.getAttribute("type"));
    String encoding = (String)(request.getAttribute("encoding"));
    Long start = (Long)(request.getAttribute("start"));

    if(path == null || (path = path.trim()).length() <= 1) {
        path = "/";
    }

    if(theme == null || (theme = theme.trim()).length() < 1) {
        theme = "Default";
    }

    if(type == null || (type = type.trim()).length() < 1) {
        type = "";
    }

    if(encoding == null || (encoding = encoding.trim()).length() < 1) {
        encoding = "utf-8";
    }

    if(start == null) {
        start = Long.valueOf(0);
    }
    request.setAttribute("path", path);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>${path}</title>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/finder/css/finder.css"/>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/sh/style/shCore${theme}.css"/>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/sh/style/shTheme${theme}.css"/>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/ajax.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/widget.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/finder.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/sh/shCore.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/sh/shAutoloader.js"></script>
</head>
<body style="overflow: hidden;" contextPath="${contextPath}" host="${host}" workspace="${workspace}" work="${work}" parent="${parent}" path="${path}" page="display">
<div class="finder">
    <div class="menu-bar">
        <div style="float: left; width: 80px;">
            <a class="button" href="javascript:void(0)" title="后退"><span class="back"></span></a>
            <a class="button" href="javascript:void(0)" title="刷新"><span class="refresh"></span></a>
        </div>
        <div style="float: left; height: 28px; position: relative;">
            <div style="float: left;"><input id="address" type="text" class="address" autocomplete="off" file="true" value="${path}"/></div>
            <div id="finder-suggest" class="list suggest"></div>

            <span class="label">theme:</span>
            <select id="uiThemeOption">
                <%
                    String[] themes = new String[]{"Default", "Django", "Eclipse", "Emacs", "FadeToGrey", "MDUltra", "Midnight", "RDark"};

                    for(String current : themes) {
                %>
                    <option value="<%=current%>" <% if(theme.equals(current)) {%>selected="true"<%}%>><%=current%></option>
                <%
                    }
                %>
            </select>

            <span class="label">type:</span>
            <select id="uiTypeOption">
                <%
                    String[] types = new String[]{"", "as", "sh", "bsh", "bash", "log", "shell", "cpp", "cs", "css", "dpi", "diff", "erl", "erlang", "groovy", "java", "js", "pl", "php", "txt", "text", "py", "ruby", "sass", "scala", "sql", "vb", "vbs", "xml", "xhtml", "xslt", "html", "htm", "asp", "jsp", "jspf", "asp", "php"};

                    for(String current : types) {
                %>
                    <option value="<%=current%>" <% if(type.equals(current)) {%>selected="true"<%}%>><%=current%></option>
                <%
                    }
                %>
            </select>

            <span class="label">encoding:</span>
            <select id="uiEncodingOption">
                <%
                    String[] encodings = new String[]{"utf-8", "gbk", "gb2312", "iso-8859-1"};

                    for(String current : encodings) {
                %>
                    <option value="<%=current%>" <% if(type.equals(current)) {%>selected="true"<%}%>><%=current%></option>
                <%
                    }
                %>
            </select>
        </div>
        <div style="float: right; width: 40px;">
            <a class="button" href="${requestURI}?action=finder.help" title="帮助"><span class="help"></span></a>
        </div>
    </div>
    <%
        if(start > 0L) {
    %>
    <div style="padding-left: 4px; height: 28px; line-height: 28px; background-color: #efefef; font-size: 12px;">
        文件较大，只显示部分数据。要查看全部数据请使用 <a href="${requestURI}?action=finder.less&workspace=${workspace}&path=${path}" style="color: #ff0000;">less</a> 打开。
        [${start} - ${end}/${length}]
    </div>
    <%
        }
    %>
    <div id="content" file-type="${type}" style="display: none;"><pre class="brush: bash;">${content}</pre></div>
</div>
<script type="text/javascript">
<!--
jQuery(function() {
    var map = {
        "??": "brush: bash;",
        "as": "brush: actionscript3;",
        "bsh": "brush: bash;",
        "log": "brush: bash;",
        "cpp": "brush: cpp;",
        "cs": "brush: cs;",
        "css": "brush: css;",
        "dhi": "brush: dpi;",
        "diff": "brush: diff;",
        "erl": "brush: erl;",
        "erlang": "brush: erlang;",
        "groovy": "brush: groovy;",
        "java": "brush: java;",
        "js": "brush: javascript;",
        "pl": "brush: perl;",
        "php": "brush: php;",
        "plain": "brush: plain;",
        "sh": "brush: bash;",
        "py": "brush: python;",
        "ruby": "brush: ruby;",
        "sass": "brush: sass;",
        "scala": "brush: scala;",
        "sql": "brush: sql;",
        "vb": "brush: vbscript;",
        "vbs": "brush: vbscript;",
        "xml": "brush: xml;",
        "xhtml": "brush: xml;",
        "xslt": "brush: xml;",
        "html": "brush: xml;",
        "htm": "brush: xml;",
        "jsp": "brush: xml;",
        "jspf": "brush: xml;",
        "asp": "brush: xml;",
        "php": "brush: xml;"
    };

    var type = jQuery("#content").attr("file-type");

    if(type == "??") {
        jQuery("#content pre").attr("class", "brush: bash;");
    }
    else {
        var brush = map[type];

        if(type != null) {
            jQuery("#content pre").attr("class", brush);
        }
        else {
            jQuery("#content pre").attr("class", "brush: plain;");
        }
    }
});

jQuery(function() {
    function path() {
        var result = [];
        var args = arguments;
        var requestURI = window.location.pathname;

        for(var i = 0; i < args.length; i++) {
            result.push(args[i].replace("@", requestURI + "?action=res&path=/sh/"));
        }
        return result;
    };

    var args = path(
        "applescript            @shBrushAppleScript.js",
        "actionscript3 as3      @shBrushAS3.js",
        "bash shell             @shBrushBash.js",
        "coldfusion cf          @shBrushColdFusion.js",
        "cpp c                  @shBrushCpp.js",
        "c# c-sharp csharp      @shBrushCSharp.js",
        "css                    @shBrushCss.js",
        "delphi pascal          @shBrushDelphi.js",
        "diff patch pas         @shBrushDiff.js",
        "erl erlang             @shBrushErlang.js",
        "groovy                 @shBrushGroovy.js",
        "java                   @shBrushJava.js",
        "jfx javafx             @shBrushJavaFX.js",
        "js jscript javascript  @shBrushJScript.js",
        "perl pl                @shBrushPerl.js",
        "php                    @shBrushPhp.js",
        "text plain             @shBrushPlain.js",
        "py python              @shBrushPython.js",
        "ruby rails ror rb      @shBrushRuby.js",
        "sass scss              @shBrushSass.js",
        "scala                  @shBrushScala.js",
        "sql                    @shBrushSql.js",
        "vb vbnet               @shBrushVb.js",
        "xml xhtml xslt html    @shBrushXml.js"
    );
    SyntaxHighlighter.autoloader.apply(null, args);
    SyntaxHighlighter.all();
});

jQuery(function() {
    jQuery(window).resize(function(){
        var c = jQuery("#content div.syntaxhighlighter");
        c.css("overflow", "auto");
        c.height(jQuery(window).height() - 38);
    });

    setTimeout(function() {
        jQuery(window).resize();
        jQuery("#content").show();
    }, 500);
});
//-->
</script>
</body>
</html>