if(typeof(window.localStorage) == "undefined") {
    window.localStorage = {};
}

var Grep = {};
Grep.rows = 0;
Grep.length = 0;
Grep.loadding = 0;
Grep.maxRows = 3000;
Grep.charset = "utf-8";
Grep.editorId = "less-editor";
Grep.containerId = "less-container";
Grep.grepURL = window.location.pathname + "?";

Grep.getContextPath = function() {
    if(this.contextPath == null || this.contextPath == undefined) {
        var contextPath = document.body.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/") {
            contextPath = "";
        }
        this.contextPath = contextPath;
    }
    return this.contextPath;
};

Grep.getGrepURL = function(keyword, regexp, position, rows) {
    var params = [];
    params[params.length] = "workspace=" + encodeURIComponent(this.workspace);
    params[params.length] = "path=" + encodeURIComponent(this.path);
    params[params.length] = "keyword=" + encodeURIComponent(keyword);
    params[params.length] = "regexp=" + (regexp == true);
    params[params.length] = "position=" + position;
    params[params.length] = "rows=" + rows;
    params[params.length] = "charset=" + this.charset;
    return this.grepURL + params.join("&");
};

Grep.parse = function(text) {
    if(text == null) {
        return {"status": 999, "message": "empty response."};
    }

    try {
        var k = text.indexOf("\n");

        if(k > -1) {
            var json = text.substring(0, k);
            var content = text.substring(k + 1);
            var range = window.eval("(" + json + ")");

            if(range != null && range.value != null) {
                range.value.content = content;
            }
            return range;
        }
        else {
            return window.eval("(" + text + ")");
        }
    }
    catch(e) {
        return {"status": 999, "message": e.name + ": " + e.message};
    }
};

/**
 * 拉取数据
 */
Grep.poll = function(url, callback) {
    this.setStatus(1);

    jQuery.ajax({
        type: "get",
        url: url,
        dataType: "text",
        error: function(req, status, error) {
            Grep.setStatus(0, status + ": " + error);
        },
        success: function(text) {
            var result = Grep.parse(text);
            var range = result.value;

            if(result.status == 200 && range != null) {
                callback(range);
                Grep.setStatus(0);
            }
            else {
                Grep.setStatus(0, result.message);
            }
        }
    });
};

/**
 * 请求下一块内容
 */
Grep.next = function(callback) {
    var keyword = this.getKeyword();
    var regexp = this.getRegexp(keyword);

    if(keyword.length < 1) {
        alert("请输入查找内容！");
        return;
    }

    var position = this.getEnd();
    var rows = this.getRows();
    var url = this.getGrepURL(keyword, regexp, position, rows);

    Grep.poll(url, function(range) {
        Grep.success(range);
        Grep.append(range);

        if(callback != null) {
            callback();
        }
    });
};

/**
 * 请求指定位置的内容
 * @param percent - 百分比位置
 */
Grep.load = function(position) {
    var keyword = this.getKeyword();
    var regexp = this.getRegexp(keyword);

    if(keyword.length < 1) {
        alert("请输入查找内容！");
        return;
    }

    var rows = this.getRows();
    var url = this.getGrepURL(keyword, regexp, position, rows);

    Grep.poll(url, function(range) {
        Grep.clear();
        Grep.success(range);
        Grep.append(range);
        Grep.scroll(160);
    });
};

/**
 * 显示区域追加内容
 * @param range - 服务端返回的文件片段对象
 */
Grep.append = function(range) {
    /**
     * 此处与less功能不同
     * 无论何种情况都追加
     */
    if(range == null) {
        return;
    }

    if(range.rows < 1 || range.content == null || range.content.length < 1) {
        range.rows = 1;
        range.content = "";
    }

    var e = this.getEditor();
    var p = this.create("append", range);
    e.appendChild(p);
};

Grep.cut1 = function() {
    var flag = false;
    var e = this.getEditor();

    while(e.childNodes.length > 100) {
        var node = e.childNodes[0];
        var count = parseInt(node.getAttribute("rows"));
        e.removeChild(node);
        this.rows -= count;
        flag = true;
    }

    while(this.rows > this.maxRows) {
        if(e.childNodes.length > 1) {
            var node = e.childNodes[0];
            var count = parseInt(node.getAttribute("rows"));
            e.removeChild(node);
            this.rows -= count;
            flag = true;
        }
        else {
            break;
        }
    }
    return flag;
};

/**
 * 当成功接收到文件片段时调用
 * @param range - 服务端返回的文件片段对象
 */
Grep.success = function(range) {
    this.rows += range.rows;
    this.length = range.length;
};

/**
 * 清除显示区域全部内容
 */
Grep.clear = function() {
    this.rows = 0;
    this.length = 0;
    this.getEditor().innerHTML = "";
};

Grep.getKeyword = function() {
    return jQuery("#grep-keyword").val();
};

Grep.getRegexp = function(keyword) {
    /**
     * 尽可能的不使用正则, 这样可以避免再去取消勾选正则
     * 如果存在查找的文本, 那么客户端做一个校验
     * 如果查找的文本一定不是正则, 那么直接返回false
     */
    if(keyword != null) {
        var regExp = new RegExp("^[0-9a-zA-Z]+$");

        /**
         * 如果全部是字母或者数字, 说明一定不是正则
         */
        if(regExp.test(keyword)) {
            return false;
        }
    }
    return (document.getElementById("grep-regexp").checked == true);
};

/**
 * 获取当前显示区域显示的起始位置
 */
Grep.getStart = function() {
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
Grep.getEnd = function() {
    var e = this.getEditor();
    var list = e.childNodes;

    if(list.length > 0) {
        var node = list[list.length - 1];
        return parseInt(node.getAttribute("end"));
    }
    else {
        return this.length - 1;
    }
};

/**
 * 获取发送请求时请求的行数
 * 每次请求时请求的行数为当前显示区域可显示行数的12倍
 * 表达式中的16为行高, 如果显示区域的行高调整, 需要调整此参数
 * 暂时先写死吧
 */
Grep.getRows = function() {
    var c = this.getContainer();
    var rows = 12 * Math.floor(jQuery(c).height() / 16);

    if(rows < 100) {
        rows = 100;
    }

    if(rows > 2000) {
        rows = 2000;
    }
    return rows;
};

/**
 * 根据当前显示区域显示的内容显示当前显示的文件进度
 */
Grep.showProgress = function() {
    var c = this.getContainer();
    var top = jQuery(c).position().top;
    var height = jQuery(c).height();
    var scrollTop = jQuery(c).scrollTop();
    var list = c.childNodes;

    for(var i = list.length - 1; i > -1; i--) {
        var e = list[i];
        var offsetTop = e.offsetTop - scrollTop;
        var bottom = offsetTop + jQuery(e).height();

        if((offsetTop >= 0 && offsetTop < height)
            || (bottom > 0 && bottom <= height)
            || (offsetTop <= 0 && bottom >= height)) {
            var end = parseInt(e.getAttribute("end"));
            this.setProgress(end);
            break;
        }
    }
};

Grep.scroll = function(height) {
    this.getContainer().scrollTop = height;
};

/**
 * 显示进度
 * @param end - 当前位置
 */
Grep.setProgress = function(position) {
    var length = this.length;
    var percent = (length > 0 ? (position / length) : 0);
    var ratio = percent * 100;

    jQuery("#less-progress-bar").find(".progress .pace").css("width", ratio + "%");
    jQuery("#less-progress-bar").find(".progress .slider a").css("left", ratio + "%").attr("percent", Math.floor(ratio) + "%");
    jQuery("#less-info").val(position + "/" + length + " B");
    jQuery("#less-info").attr("title", (length / 1024 / 1024).toFixed(2) + " M");
};

/**
 * 设置状态
 * @param status - 状态, 0: 空闲, 1: 正在加载
 * @param message - 显示信息
 */
Grep.setStatus = function(status, message) {
    if(status == 0) {
        Grep.loadding = 0;

        if(message != null) {
            jQuery("#less-status").val(message);
        }
        else {
            jQuery("#less-status").val("READY");
        }
    }
    else {
        Grep.loadding = 1;

        if(message != null) {
            jQuery("#less-status").val(message);
        }
        else {
            jQuery("#less-status").val("loading...");
        }
    }
};

/**
 * 获取显示区域的容器
 */
Grep.getContainer = function() {
    if(this.container == null) {
        this.container = document.getElementById(this.containerId);
    }
    return this.container;
};

/**
 * 获取显示区域的容器
 */
Grep.getEditor = function() {
    if(this.container == null) {
        this.container = document.getElementById(this.containerId);
    }
    return this.container;
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
Grep.create = function(type, range) {
    var p = document.createElement("pre");
    p.setAttribute("action", type);
    p.setAttribute("start", range.start);
    p.setAttribute("end",   range.end);
    p.setAttribute("rows",  range.rows);
    p.setAttribute("length",  range.length);
    p.appendChild(document.createTextNode(range.content));

    if(range.truncate == true) {
        p.setAttribute("rows",  range.rows + 1);
        p.appendChild(document.createTextNode("<<< data truncated >>>"));
    }
    return p;
};

Grep.init = function() {
    var container = this.getContainer();

    this.workspace = document.body.getAttribute("workspace");
    this.path = document.body.getAttribute("path");
    this.charset = Finder.getConfig("global.charset", "utf-8");
    this.fontFamily = Finder.getConfig("less.fontFamily", "Lucida Console");
    this.fontColor = Finder.getConfig("less.fontColor", "#999999");
    this.backgroundColor = Finder.getConfig("less.backgroundColor", "#000000");

    container.style.color = this.fontColor;
    container.style.fontFamily = this.fontFamily;
    container.style.backgroundColor = this.backgroundColor;

    Charset.setup(jQuery("select[name=charset]").get(0), this.charset);

    jQuery("select[name=charset]").change(function() {
        Grep.charset = this.value;
    });

    var lastScrollTop = 0;

    jQuery(container).bind("scroll", function() {
        if(Grep.loadding != 0 || Grep.flag == true) {
            return;
        }

        /**
         * 计算当前显示的进度
         */
        Grep.showProgress();

        var c = Grep.getContainer();
        var scrollTop = c.scrollTop;
        var scrollHeight = c.scrollHeight;
        var clientHeight = c.clientHeight;

        if(lastScrollTop < scrollTop) {
            /**
             * offset = 128 * 16, 提前n行加载，16为行高
             * 当还有n行未显示时提前加载，避免滚动到内容时加载的延迟
             */
            if((clientHeight + scrollTop + offset) >= scrollHeight) {
                Grep.next(function() {
                    Grep.showProgress();
                });
                Grep.cut1();
            }
        }
        lastScrollTop = scrollTop;
    });

    jQuery("#less-progress-bar .progress .slider .mask").unbind();
    jQuery("#less-progress-bar .progress .slider .mask").click(function(event) {
        if(event.target != this) {
            return;
        }

        var x = event.offsetX;
        var width = jQuery(this).width();
        var position = Math.floor(x / width * Grep.length);
        Grep.setProgress(position);
        Grep.load(position);
    });

    jQuery("#less-progress-bar .progress .slider .mask").mousemove(function(event) {
        if(event.target != this) {
            return;
        }

        var x = event.offsetX;
        var top = (event.clientY - 30) + "px";
        var left = (event.clientX - 16) + "px";
        var width = jQuery(this).width();
        var percent = Math.floor(x / width * 100) + "%";
        jQuery("#less-tooltip").css({"top": top, "left": left}).html(percent).show();
    });

    jQuery("#less-progress-bar .progress .slider .mask").mouseout(function() {
        jQuery("#less-tooltip").hide();
    });

    /**
     * grep
     */
    jQuery("#grep-ensure").click(function() {
        var keyword = Grep.getKeyword();

        if(jQuery.trim(keyword).length > 0) {
            Grep.clear();
            jQuery("#find-panel").hide();

            Grep.next(function() {
                Grep.showProgress();
            });
        }
    });

    jQuery("#grep-close").click(function() {
        jQuery("#find-panel").hide();
    });

    jQuery(document.body).keydown(function(event) {
        var keyCode = event.keyCode;

        if(event.ctrlKey == true && keyCode == 66) {
            if(jQuery("#find-panel").is(":hidden")) {
                jQuery("#find-panel").show();

                try {
                    document.getElementById("grep-keyword").select();
                }
                catch(e) {
                }
            }
            else {
                jQuery("#find-panel").hide();
            }
            return false;
        }
    });
};
