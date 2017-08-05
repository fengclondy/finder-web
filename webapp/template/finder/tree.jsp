<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.skin.finder.i18n.I18N"%>
<%@ page import="com.skin.finder.i18n.LocalizationContext"%>
<%
    LocalizationContext i18n = I18N.getBundle(request);
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Finder v1.0</title>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/finder/css/finder.css"/>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/htree/css/htree.css"/>
<script type="text/javascript" src="${requestURI}?action=res&path=/htree/htree.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/htree/htree.util.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
//<![CDATA[
HTree.click = function(src) {
    var url = src.getAttribute("data");

    if(url == null || url.length < 1) {
        return;
    }

    try {
        var doc = window.parent.window.document;
        var iframe = doc.getElementById("mainFrame");

        if(iframe != null) {
            iframe.src = url;
        }
        else {
            alert("系统错误，请稍后再试！");
        }
    }
    catch(e) {
        if(typeof(window.console) != "undefined") {
            window.console.error(e.name + ": " + e.message);
        }
        alert("系统错误，请稍后再试！");
    }
};

function expand(path){
    var a = [];
    var s = path.split("/");

    for(var i = 0; i < s.length; i++) {
        s[i] = HTree.trim(s[i]);

        if(s[i].length > 0) {
            a[a.length] = s[i];
        }
    }

    var root = HTree.Util.getRootNode(document.getElementById("htree"));

    var handler = function(node, i) {
        if(i >= a.length) {
            return;
        }

        var e = getTreeNodeByValue(node, a[i]);

        if(e != null) {
            var height = document.documentElement.clientHeight;
            var scrollTop = document.body.scrollTop;
            var offsetTop = e.offsetTop;

            if(scrollTop > offsetTop) {
                document.body.scrollTop = offsetTop - Math.floor(height / 2);
                document.documentElement.scrollTop = offsetTop - Math.floor(height / 2);
            }

            if(offsetTop > (height + scrollTop)) {
                document.body.scrollTop = offsetTop - Math.floor(height / 2);
                document.documentElement.scrollTop = offsetTop - Math.floor(height / 2);
            }

            HTree.expand(e, {"expand": true, "callback": function(e){
                handler(e, i + 1);
            }});
        }
        else {
            // alert("node [" + value + "] not found!");
        }
    };
    handler(root, 0);
}

function getTreeNodeByValue(node, value) {
    if(node == null) {
        return null;
    }

    var list = getChildTreeNodes(node);
    var length = list.length;

    for(var i = 0; i < length; i++) {
        var a = HTree.Util.getChildNode(list[i], "//a");

        if(a != null && a.getAttribute("value") == value) {
            return list[i];
        }
    }
    return null;
}

function getChildTreeNodes(node) {
    var c = null;
    var n = node.nextSibling;

    while(n != null) {
        if(n.nodeType == 1) {
            c = n;
            break;
        }
        else {
            n = n.nextSibling;
        }
    }

    var temp = [];

    if(c != null) {
        var list = c.childNodes;
        var length = list.length;

        for(var i = 0; i < length; i++) {
            n = list[i];

            if(n.nodeType == 1 && n.className == "node") {
                temp[temp.length] = n;
            }
        }
    }
    return temp;
}

///////////////////
function buildTree(id, xmlUrl, rootUrl){
    HTree.config.stylePath = window.location.pathname + "?action=res&path=/htree/images/";

    var e = document.getElementById(id);

    if(e == null) {
        return;
    }

    var name = e.getAttribute("data-name");
    var tree = new HTree.TreeNode({text: name, href: rootUrl, xmlSrc: xmlUrl});

    tree.load(function(){
        this.render(document.getElementById(id));
    });
}

jQuery(function() {
    var resize = function() {
        var e = document.getElementById("htree");

        if(e != null) {
            var parent = e.parentNode;
            var offset = parseInt(parent.getAttribute("offset-top"));

            if(isNaN(offset)) {
                offset = 0;
            }

            var height = document.documentElement.clientHeight - offset;
            parent.style.height = height + "px";
        }
    };
    jQuery(window).load(resize);
    jQuery(window).resize(resize);
});

jQuery(function() {
    var requestURI = window.location.pathname;
    buildTree("htree", requestURI + "?action=finder.getWorkspaceXml", requestURI + "?action=finder.blank");
});
//]]>
</script>
</head>
<body>
<div class="left-nav">
    <div class="menu-body" style="padding-left: 8px; overflow-x: auto; overflow-y: scroll;">
        <div id="htree" class="htree" style="margin-top: 10px; white-space: nowrap;" data-name="<%=i18n.format("finder.tree.root.name")%>"></div>
    </div>
</div>
</body>
</html>
