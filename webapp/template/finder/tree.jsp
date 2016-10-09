<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Finder v1.0</title>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/finder.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/htree/css/htree.css"/>
<script type="text/javascript" src="${contextPath}/resource/htree/htree.js"></script>
<script type="text/javascript" src="${contextPath}/resource/htree/htree.util.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
//<![CDATA[
HTree.treeNodeOnClick = function(url){
    if(url != null && url.length > 0) {
        window.top.mainFrame.location.href = url;
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

function getChildTreeNodes(node){
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
    HTree.config.stylePath = "${contextPath}/resource/htree/images/";
    var tree = new HTree.TreeNode({text: "finder", href: rootUrl});
    tree.load(xmlUrl, function(){
        this.render(document.getElementById(id));
    });
}

jQuery(function() {
    var resize = function(){
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
    jQuery(window).bind("resize", resize);
});

jQuery(function() {
    jQuery("select[name=workspace]").change(function() {
        var workspace = this.value;

        if(workspace != null && workspace.length > 0) {
            setTimeout(function() {
                var xmlUrl = "${contextPath}/finder/getFolderXml.html?workspace=" + encodeURIComponent(workspace);
                var rootUrl = "${contextPath}/finder/display.html?workspace=" + encodeURIComponent(workspace);
                buildTree("htree", xmlUrl, rootUrl);
            }, 100);
        }
    });
    buildTree("htree", "${contextPath}/finder/getFolderXml.html?workspace=${workspace}", "${contextPath}/finder/display.html?workspace=${workspace}");
});
//]]>
</script>
</head>
<body>
<div class="left-nav">
    <div class="menu-body" style="padding-left: 8px; overflow: scroll;">
        <div id="htree" class="htree" style="margin-top: 10px; white-space: nowrap;"></div>
    </div>
</div>
</body>
</html>
