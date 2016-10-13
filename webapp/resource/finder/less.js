if(typeof(window.localStorage) == "undefined") {
    window.localStorage = {};
}

var Less = {};
Less.rows = 0;
Less.length = 0;
Less.loadding = 0;
Less.maxRows = 3000;
Less.charset = "utf-8";
Less.editorId = "less-editor";
Less.containerId = "less-container";
Less.rangeURL = "/finder/getRange.html";

Less.getContextPath = function() {
    if(this.contextPath == null || this.contextPath == undefined) {
        var contextPath = document.body.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/") {
            contextPath = "";
        }
        this.contextPath = contextPath;
    }
    return this.contextPath;
};

Less.getRangeURL = function(position, type, rows) {
    var params = [];
    params[params.length] = "workspace=" + encodeURIComponent(this.workspace);
    params[params.length] = "work=" + encodeURIComponent(this.work);
    params[params.length] = "parent=" + encodeURIComponent(this.parent);
    params[params.length] = "path=" + encodeURIComponent(this.path);
    params[params.length] = "position=" + position;
    params[params.length] = "type=" + type;
    params[params.length] = "rows=" + rows;
    params[params.length] = "charset=" + this.charset;
    return this.getContextPath() + this.rangeURL + "?" + params.join("&");
};

/**
 * 请求前一块内容
 */
Less.prev = function() {
    var position = this.getStart() - 1;

    if(position <= 0) {
        return;
    }

    this.setStatus(1);

    var rows = this.getRows();
    var url = this.getRangeURL(position, 0, rows);

    jQuery.ajax({
        type: "get",
        url: url,
        dataType: "json",
        error: function(req, status, error) {
            Less.setStatus(0, status + ": " + error);
        },
        success: function(result) {
            var range = result.value;

            if(result.status == 200 && range != null) {
                Less.success(range);
                Less.insert(range);
                Less.setStatus(0);
            }
            else {
                Less.setStatus(0, result.message);
            }
        }
    });
};

/**
 * 请求下一块内容
 */
Less.next = function() {
    this.setStatus(1);

    var position = this.getEnd();
    var rows = this.getRows();
    var url = this.getRangeURL(position, 1, rows);

    jQuery.ajax({
        type: "get",
        url: url,
        dataType: "json",
        error: function(req, status, error) {
            Less.setStatus(0, status + ": " + error);
        },
        success: function(result) {
            var range = result.value;

            if(result.status == 200 && range != null) {
                Less.success(range);
                Less.append(range);
                Less.setStatus(0);
            }
            else {
                Less.setStatus(0, result.message);
            }
        }
    });
};

/**
 * 请求指定位置的内容
 * @param percent - 百分比位置
 */
Less.load = function(percent) {
    this.setStatus(1);

    var position = (percent < 1 ? Math.floor(percent * this.length) : this.length);
    var rows = this.getRows();
    var url = this.getRangeURL(position, 1, rows);

    jQuery.ajax({
        type: "get",
        url: url,
        dataType: "json",
        error: function(req, status, error) {
            Less.setStatus(0, status + ": " + error);
        },
        success: function(result) {
            var range = result.value;

            if(result.status == 200 && range != null) {
                Less.clear();
                Less.success(range);
                Less.append(range);

                /**
                 * 追加完成之后会导致立即触发scroll事件, 然后再次出发next请求, 导致接下来计算的进度总是比点击的位置大很多
                 * 所以此处稍等一会再设置状态为空闲
                 *
                 */
                setTimeout(function() {
                    Less.setStatus(0);
                }, 200);
            }
            else {
                Less.setStatus(0, result.message);
            }
        }
    });
};

/**
 * 显示区域追加内容
 * @param range - 服务端返回的文件片段对象
 */
Less.append = function(range) {
    if(range == null || range.rows < 1) {
        return;
    }

    var e = this.getEditor();
    var p = this.create(range);
    var list = e.childNodes;

    while(list.length > 100) {
        var node = list[0];
        var count = parseInt(node.getAttribute("rows"));
        e.removeChild(node);
        this.rows -= count;
    }

    while(this.rows > this.maxRows) {
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
    e.parentNode.scrollTop = e.parentNode.scrollTop - jQuery(p).height();
    p.setAttribute("action", "append");

    /**
     * bug: 当页面第一次进来的时候不会触发任何事件，所以也不会计算加载进度，导致进度条仍然显示0
     * 这个bug不好解决，没有地方可以方便的检查，所以在此处做一个检查，简单处理掉
     */
    if(this.first != true) {
        this.first = true;
        this.showProgress();
    }
};

/**
 * 显示区域插入内容
 * @param range - 服务端返回的文件片段对象
 */
Less.insert = function(range) {
    if(range == null || range.rows < 1) {
        return;
    }

    var e = this.getEditor();
    var p = this.create(range);
    var list = e.childNodes;

    while(list.length > 100) {
        var node = list[list.length - 1];
        var count = parseInt(node.getAttribute("rows"));
        e.removeChild(node);
        this.rows -= count;
    }

    while(this.rows > this.maxRows) {
        if(list.length > 1) {
            var node = list[list.length - 1];
            var count = parseInt(node.getAttribute("rows"));
            e.removeChild(node);
            this.rows -= count;
        }
        else {
            break;
        }
    }

    if(list.length > 0) {
        e.insertBefore(p, list[0]);
    }
    else {
        e.appendChild(p);
    }
    e.parentNode.scrollTop = p.clientHeight;
    p.setAttribute("action", "insert");
};

/**
 * 当成功接收到文件片段时调用
 * @param range - 服务端返回的文件片段对象
 */
Less.success = function(range) {
    this.rows += range.rows;
    this.length = range.length;
};

/**
 * 清除显示区域全部内容
 */
Less.clear = function() {
    this.rows = 0;
    this.getEditor().innerHTML = "";
};

/**
 * 获取当前显示区域显示的起始位置
 */
Less.getStart = function() {
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
Less.getEnd = function() {
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
 * 每次请求时请求的行数为当前显示区域可显示行数的3倍
 * 表达式中的16为行高, 如果显示区域的行高调整, 需要调整此参数
 * 暂时先写死吧
 */
Less.getRows = function() {
    var c = this.getContainer();
    return 3 * Math.floor(jQuery(c).height() / 16);
};

/**
 * 根据当前显示区域显示的内容显示当前显示的文件进度
 */
Less.showProgress = function() {
    var e = this.getEditor();
    var c = this.getContainer();
    var top = jQuery(c).position().top;
    var height = jQuery(c).height();
    var scrollTop = jQuery(c).scrollTop();
    var list = e.childNodes;
    var length = this.length;

    for(var i = list.length - 1; i > -1; i--) {
        var e = list[i];
        var offsetTop = e.offsetTop - scrollTop;
        var bottom = offsetTop + jQuery(e).height();

        if((offsetTop >= 0 && offsetTop < height)
            || (bottom > 0 && bottom <= height)
            || (offsetTop <= 0 && bottom >= height)) {
            var end = parseInt(e.getAttribute("end"));

            if(length > 0) {
                this.setProgress(end);
            }
            else {
                this.setProgress(0);
            }
            break;
        }
    }
};

/**
 * 显示进度
 * @param end - 当前位置
 */
Less.setProgress = function(position) {
    var length = this.length;
    var percent = position / length;
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
Less.setStatus = function(status, message) {
    if(status == 0) {
        Less.loadding = 0;

        if(message != null) {
            jQuery("#less-status").val(message);
        }
        else {
            jQuery("#less-status").val("READY");
        }
    }
    else {
        Less.loadding = 1;

        if(message != null) {
            jQuery("#less-status").val(message);
        }
        else {
            jQuery("#less-status").val("loading...");
        }
    }
};

/**
 * 获取根容器
 */
Less.getContainer = function() {
    if(this.container == null) {
        this.container = document.getElementById(this.containerId);
    }
    return this.container;
};

/**
 * 获取显示区域的容器
 */
Less.getEditor = function() {
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
Less.create = function(range) {
    var p = document.createElement("pre");
    p.setAttribute("action", "unknown");
    p.setAttribute("start", range.start);
    p.setAttribute("end",   range.end);
    p.setAttribute("rows",  range.rows);
    p.setAttribute("length",  range.length);
    p.appendChild(document.createTextNode(range.content));
    return p;
};

Less.init = function() {
    var editor = this.getEditor();
    var container = this.getContainer();

    this.workspace = document.body.getAttribute("workspace");
    this.work = document.body.getAttribute("work");
    this.parent = document.body.getAttribute("parent");
    this.path = document.body.getAttribute("path");
    this.charset = Finder.getConfig("global.charset", "utf-8");
    this.fontFamily = Finder.getConfig("less.fontFamily", "Lucida Console");
    this.fontColor = Finder.getConfig("less.fontColor", "#009900");
    this.backgroundColor = Finder.getConfig("less.backgroundColor", "#000000");

    container.style.color = this.fontColor;
    container.style.fontFamily = this.fontFamily;
    container.style.backgroundColor = this.backgroundColor;

    Charset.setup(jQuery("select[name=charset]").get(0), this.charset);

    jQuery("select[name=charset]").change(function() {
        Less.charset = this.value;
    });

    jQuery(container).bind("scroll", function() {
        if(Less.loadding != 0 || Less.flag == true) {
            return;
        }

        var c = Less.getContainer();
        var scrollTop = c.scrollTop;
        var scrollHeight = c.scrollHeight;
        var clientHeight = c.clientHeight;

        /**
         * 计算当前显示的进度
         * 这个不能放到加载完成之后计算，那样显示出来的进度不准，并且还与手动点击进度条显示的进度冲突
         */
        Less.showProgress();

        /**
         * 160 = 16 * 10, 提前10行加载, 16为行高
         * 当还有10行未显示时提前加载, 避免滚动到内容时加载的延迟, 可以提高使用体验
         */
        if((clientHeight + scrollTop + 160) >= scrollHeight) {
            Less.next();
        }
        else if(scrollTop <= 160) {
            Less.prev();
        }
        else {
            // do nothing
        }
    });

    jQuery("#less-progress-bar .progress .slider .mask").unbind();
    jQuery("#less-progress-bar .progress .slider .mask").click(function(event) {
        if(event.target != this) {
            return;
        }

        var x = event.offsetX;
        var width = jQuery(this).width();
        Less.setProgress(Math.floor(x / width * Less.length));
        Less.load(x / width);
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
    jQuery(container).trigger("scroll");
};
