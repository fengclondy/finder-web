<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Finder - ${workspace}</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/resource/finder/css/frame.css"/>
<script type="text/javascript" src="${contextPath}/resource/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/widget.js"></script>
<script type="text/javascript" src="${contextPath}/resource/finder/fileupload.js"></script>
<script type="text/javascript">
<!--
if(window.parent != window) {
    window.parent.location.href = window.location.href;
}

var App = {};
App.setViewType = function(flag) {
    var f1 = document.getElementById("leftPanel");
    var f2 = document.getElementById("ctrlPanel");
    var f3 = document.getElementById("mainPanel");
    var f4 = document.getElementById("ctrlBtn");
    var viewType = parseInt(flag);

    if(viewType == 1) {
        /* [200, 0, *] */
        f1.parentNode.style.display = "block";
        f1.parentNode.style.width = "240px";
        f2.style.marginLeft = "0px";
        f2.style.display = "none";
        f1.style.display = "block";
        f3.style.marginLeft = "240px";
    }
    else if(viewType == 2) {
        /* [200, 10, *] */
        f1.parentNode.style.display = "block";
        f1.parentNode.style.width = "250px";
        f2.style.marginLeft = "240px";
        f2.style.display = "block";
        f1.style.display = "block";
        f3.style.marginLeft = "250px";
        f4.src = "${contextPath}/resource/images/lt.gif";
    }
    else if(viewType == 3) {
        /* [0, 10, *] */
        f1.style.display = "none";
        f1.parentNode.style.display = "block";
        f1.parentNode.style.width = "10px";
        f2.style.marginLeft = "0px";
        f2.style.display = "block";
        f3.style.marginLeft = "10px";
        f4.src = "${contextPath}/resource/images/gt.gif";
    }
    else if(viewType == 4) {
        /* [0, 0, *] */
        f1.parentNode.style.display = "none";
        f3.style.marginLeft = "0px";
        f4.src = "${contextPath}/resource/images/gt.gif";
    }
};

jQuery(function() {
    App.resize = function(){
        var offset = document.getElementById("viewPanel").offsetTop + 24;
        var clientHeight = document.documentElement.clientHeight;
        document.getElementById("leftFrame").style.height = (clientHeight - offset) + "px";
        document.getElementById("mainFrame").style.height = (clientHeight - offset) + "px";
        document.getElementById("ctrlPanel").style.height = (clientHeight - offset) + "px";
    };

    jQuery("body").click(function() {
        jQuery("#setting_menu").hide();
        jQuery("#application_menu").hide();
    });

    jQuery("#ctrlPanel").hover(function(event){
        this.style.backgroundColor = "#aec9fb";
    }, function(event){
        this.style.backgroundColor = "#dfe8f6";
    });
    jQuery("#ctrlBtn").click(function(event){
        var viewType = 2;

        if(this.src.indexOf("${contextPath}/resource/images/gt.gif") > -1) {
            viewType = 2;
        }
        else {
            viewType = 3;
        }
        App.setViewType(viewType);
    });

    jQuery("#statusBar").show();
    jQuery(window).bind("resize", App.resize);
    App.setViewType(1);
    App.resize();
});

/**
 * 事件入口
 * 拦截当前页面的全部事件, 并将控制权转交到widget
 * widget负责管理所有的窗口并负责将页面的事件转发给当前活动窗口
 * widget完成的功能有：
 * 1. 窗口管理, zIndex分配, 活动窗口管理
 * 2. 事件转发, 将事件转发给当前活动的窗口
 */
jQuery(function() {
    /**
     * jQuery-1.7.2版本的paste事件获取不到clipboardData
     * 该事件尽可能放到jQuery事件之前
     * bug: IE11不触发paste事件
     */
    EventUtil.addEventListener(document, "paste", function(event) {
        return DialogManager.dispatch("paste", event);
    });

    jQuery(document).click(function(event) {
        return DialogManager.dispatch("click", event);
    });

    jQuery(document).dblclick(function(event) {
        return DialogManager.dispatch("dblclick", event);
    });

    /**
     * keydown事件先于paste触发
     * 因此要保证paste被触发必须使Ctrl + V操作返回true
     * 如果Ctrl + V事件存在弹框, 那么root将无法捕获到paste事件
     * 因为当弹框出现的时候, 弹框是活动窗口, 因此paste事件不会被传递到root
     */
    jQuery(document).keydown(function(event) {
        var flag = DialogManager.dispatch("keydown", event);
        return flag;
    });

    jQuery(document).bind("contextmenu", function(event) {
        var e = (event || window.event);
        var src = (e.srcElement || e.target);
        var nodeName = src.nodeName.toLowerCase();

        if(nodeName == "input" || nodeName == "textarea") {
            return true;
        }
        else {
            return DialogManager.dispatch("contextmenu", e);
        }
    });
});
//-->
</script>
</head>
<body contextPath="${contextPath}">
<div id="viewPanel" mainPanel="view">
    <div style="float: left; width: 10px;">
        <div id="leftPanel" class="left-panel"><iframe id="leftFrame" name="leftFrame" class="left-frame"
            src="${contextPath}/finder/tree.html?workspace=${workspace}" frameborder="0" scrolling="no" marginwidth="0" marginheight="0"></iframe></div>
        <div id="ctrlPanel" class="ctrl-panel"><img id="ctrlBtn" style="border: none; cursor: default;" src="${contextPath}/resource/finder/images/gt.gif"/></div>
    </div>
    <div id="mainPanel" class="main-panel"><iframe id="mainFrame" name="mainFrame" class="main-frame"
        src="${contextPath}/finder/display.html?workspace=${workspace}" frameborder="0" scrolling="auto" marginwidth="0" marginheight="0"></iframe></div>
</div>
<div id="statusBar" class="status-bar hide">
    <div id="_task_bar" class="widget-task-bar"></div>
    <div style="float: right;">
        <img style="margin-top: -2px; margin-right: 4px;" src="${contextPath}/resource/finder/images/sound.gif"/>欢迎您，祝您工作愉快！
    </div>
</div>
</body>
</html>
