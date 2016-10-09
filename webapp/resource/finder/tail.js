/**
 * Skin JavaScript Library v1.0.0
 * Copyright (c) 2010 xuesong.net
 * 
 * mailto: xuesong.net@163.com
 * Date: 2010-04-28 10:24:21
 * Revision: 1012
 */
var Tail = {"running": false, "position": -1};
Tail.rows = 300;
Tail.length = 0;
Tail.scroll = true;
Tail.editorId = "tail-editor";
Tail.containerId = "tail-container";
Tail.tailURL = "/finder/getTail.html";

Tail.getTailURL = function(position, rows) {
    var params = [];
    params[params.length] = "workspace=" + encodeURIComponent(this.workspace);
    params[params.length] = "work=" + encodeURIComponent(this.work);
    params[params.length] = "parent=" + encodeURIComponent(this.parent);
    params[params.length] = "path=" + encodeURIComponent(this.path);
    params[params.length] = "position=" + position;
    params[params.length] = "rows=" + rows;
    params[params.length] = "charset=" + this.charset;
    return this.tailURL + "?" + params.join("&");
};

Tail.setScroll = function(b) {
    this.scroll = b;
};

Tail.getTimeout = function() {
    var e = document.getElementById("reloadInterval");

    if(e != null) {
        var timeout = parseInt(e.value);

        if(isNaN(timeout) == false && timeout >= 1) {
            return timeout * 1000;
        }
    }
    return 2000;
};

Tail.start = function(){
    this.stop();
    this.running = true;
    this.poll();
};

Tail.stop = function(){
    this.running = false;

    if(this.timer != null) {
        clearTimeout(this.timer);
    }
};

Tail.poll = function() {
    if(this.running != true) {
        return;
    }

    var position = this.getEnd();
    var rows = this.getRows();
    var length = this.length;
    var url = this.getTailURL(position, rows);

    jQuery.ajax({
        type: "get",
        url: url,
        dataType: "json",
        error: function(req, status, error) {
            var message = "/* ** ERROR: ** */\r\n" + status + ": " + error + "\r\n\r\n\r\n";
            Tail.append({"start": position, "end": position, "rows": 1, "length": length, "content": message});
        },
        success: function(result) {
            var range = result.value;

            if(result.status == 200) {
                Tail.append(range);
            }
            else {
                // Tail.append("error - " + result.message + ": " + result.message);
            }
        }
    });
};

Tail.append = function(range){
    if(range != null && range.rows > 0) {
        var e = this.getEditor();
        var p = this.create(range);

        while(this.rows > this.maxRows) {
            var list = e.childNodes;

            if(list.length > 1) {
                var node = list[0];
                var count = parseInt(node.getAttribute("rows"));
                e.removeChild(node);
                this.rows -= count;
            }
            else {
                break;
            }
        }
        e.appendChild(p);

        if(this.scroll == true) {
            e.parentNode.scrollTop = e.parentNode.scrollHeight;
        }
    }
    var timeout = this.getTimeout();
    this.timer = setTimeout(function(){Tail.poll();}, timeout);
};

Tail.select = function() {
    var e = this.getEditor();

    if(document.all) {
        /* for IE */
        var range = document.body.createTextRange();
        range.moveToElementText(e);
        range.select();
    }
    else {
        var selection = window.getSelection();

        if(selection.setBaseAndExtent) {
            /* for Safari */
            selection.setBaseAndExtent(e, 0, e, 1);
        }
        else {
            /* for FF, Opera */
            var range = document.createRange();
            range.selectNodeContents(e);
            selection.removeAllRanges();
            selection.addRange(range);
            window.focus();
        }
    }
};

/**
 * 清除显示区域全部内容
 */
Tail.clear = function() {
    this.rows = 0;
    this.getEditor().innerHTML = "";
};

/**
 * 获取当前显示区域显示的起始位置
 */
Tail.getStart = function() {
    var e = this.getEditor();
    var list = e.childNodes;

    if(list.length > 0) {
        var node = list[0];
        return parseInt(node.getAttribute("start"));
    }
    else {
        return 0;
    }
};

/**
 * 获取当前显示区域显示的终止位置
 */
Tail.getEnd = function() {
    var e = this.getEditor();
    var list = e.childNodes;

    if(list.length > 0) {
        var node = list[list.length - 1];
        return parseInt(node.getAttribute("end"));
    }
    else {
        return 0;
    }
};

/**
 * 获取发送请求时请求的行数
 * 每次请求时请求的行数为当前显示区域可显示行数的3倍
 * 表达式中的16为行高, 如果显示区域的行高调整, 需要调整此参数
 * 暂时先写死吧
 */
Tail.getRows = function() {
    return this.rows;
};

/**
 * 获取根容器
 */
Tail.getContainer = function() {
    if(this.container == null) {
        this.container = document.getElementById(this.containerId);
    }
    return this.container;
};

/**
 * 获取显示区域的容器
 */
Tail.getEditor = function() {
    if(this.editor == null) {
        this.editor = document.getElementById(this.editorId);
    }
    return this.editor;
};

/**
 * 创建块内容
 * range: {"start": 269027, "end": 269361, "length": 269389, "rows": 2, "content": "1 0123456789\r\n2 0123456789\r\n"}}
 * start  - content中的第一个字符的位置
 * end    - content中最后一个字符的位置
 * rows   - content中包含的行数
 * length - 文件总大小, 对于实时变化的系统日志文件, 每次请求返回的文件总大小也总在变化.
 *          一般情况下日志文件会不断增大, 但也有可能从很大变为很小, 例如按天滚动的日志文件, 在凌晨时会重新产生新文件.
 * @param range - 服务器返回的文本块
 */
Tail.create = function(range) {
    var p = document.createElement("pre");
    p.setAttribute("start", range.start);
    p.setAttribute("end",   range.end);
    p.setAttribute("rows",  range.rows);
    p.setAttribute("length",  range.length);
    p.appendChild(document.createTextNode(range.content));
    return p;
};

Tail.init = function() {
    Charset.setup(jQuery("select[name=charset]").get(0));

    jQuery("select[name=charset]").change(function() {
        Tail.charset = this.value;
    });

    this.workspace = document.body.getAttribute("workspace");
    this.work = document.body.getAttribute("work");
    this.parent = document.body.getAttribute("parent");
    this.path = document.body.getAttribute("path");
    this.charset = document.body.getAttribute("charset");
    this.start();
};
