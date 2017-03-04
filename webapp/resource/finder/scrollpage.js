if(window.Parameter == null){Parameter = {};}

Parameter.getParameter = function(name) {
    var values = (this.parse(window.location.search))[name];

    if(values != null && values.length > 0) {
        return values[0];
    }
    return null;
};

Parameter.parse = function(query) {
    var parameters = {};

    if(query == null || query == "") {
        return parameters;
    }

    var name  = [];
    var value = [];

    for(var i = 0, length = query.length; i < length; i++) {
        var c = query.charAt(i);

        if(c == "?") {
            if(i + 1 < length) {
                query = query.substring(i + 1);
            }
            else {
                query = "";
            }

            break;
        }
    }

    for(var i = 0, length = query.length; i < length; i++) {
        var c = query.charAt(i);

        if(c == "?" || c == "&") {
            continue;
        }
        else if(c == "#") {
            for(i++; i < length; i++) {
                c = query.charAt(i);

                if(c == "?" || c == "&" || c == "#") {
                    i--;
                    break;
                }
            }
        }
        else if(c == "=") {
            for(i++; i < length; i++) {
                c = query.charAt(i);

                if(c == "?" || c == "&" || c == "#") {
                    if(c == "#") {
                        i--;
                    }

                    break;
                }
                else {
                    value[value.length] = c;
                }
            }

            if(name.length > 0) {
                var n = name.join("");
                var a = parameters[n];

                if(a == null) {
                    a = [];
                    parameters[n] = a;
                }

                a[a.length] = value.join("");
            }

            name = [];
            value = [];
        }
        else {
            name[name.length] = c;
        }
    }

    return parameters;
};

Parameter.serialize = function(parameters) {
    var a = [];
    if(parameters != null) {
        if(typeof(json) == "object") {
            for(var name in json) {
                var value = parameters[name];
                var className = typeof(value);

                if(value != null) {
                    if(className == "object" && value.length != null) {
                        for(var j = 0; j < value.length; j++) {
                            if(value[j] != null) {
                                a[a.length] = encodeURIComponent(name) + "=" + encodeURIComponent(value[j]);
                            }
                        }
                    }
                    else {
                        a[a.length] = encodeURIComponent(name) + "=" + encodeURIComponent(value.toString());
                    }
                }
            }
        }
    }

    return a.join("&");
};

Parameter.remove = function(url, name) {
    var s = url;

    if(s != null && name != null) {
        var i = 0;
        var j = 0;
        var c = 0;

        while(i > -1) {
            i = s.indexOf(name + "=", i);

            if(i > -1) {
                j = i + name.length + 1;

                for(; j < s.length; j++) {
                    c = s.charAt(j);

                    if(c == "?" || c == "&" || c == "#") {
                        break;
                    }
                }

                if(i > 0) {
                    c = s.charAt(i - 1);

                    if(c == "?" || c == "&" || c == "#") {
                        s = s.substring(0, i - 1) + s.substring(j);
                    }
                    else {
                        s = s.substring(0, i) + s.substring(j);
                    }
                }
                else {
                    s = s.substring(0, i) + s.substring(j);
                }
            }
        }
    }

    var i = s.indexOf("?");

    if(i < 0) {
        i = s.indexOf("&");

        if(i > -1) {
            s = s.substring(0, i) + "?" + s.substring(i + 1);
        }
    }

    return s;
};

var ScrollPage = {};
ScrollPage.setup = function(src) {
    var c = src;

    if(c == null) {
        return;
    }

    var pageNum = ScrollPage.getPageNum(c);
    var pageSize = ScrollPage.getPageSize(c);
    var count = ScrollPage.getInt(c.getAttribute("count"), 0);
    var start = (pageNum - 1) * pageSize + 1;
    var end = Math.min(pageNum * pageSize, count);
    var total = Math.floor((count + pageSize - 1) / pageSize);

    var f = function(event){
        var parent = ScrollPage.getContainer(this);
        var pageNum = parseInt(this.getAttribute("pageNum"));
        var pageSize = ScrollPage.getPageSize(this);
        ScrollPage.go(parent.getAttribute("href"), pageNum, pageSize);
    };

    ScrollPage.bind(ScrollPage.find(c, "a.page"), "click", f);
    ScrollPage.bind(ScrollPage.find(c, "div.first"), "click", f);
    ScrollPage.bind(ScrollPage.find(c, "div.prev"), "click", f);
    ScrollPage.bind(ScrollPage.find(c, "div.next"), "click", f);
    ScrollPage.bind(ScrollPage.find(c, "div.last"), "click", f);
    ScrollPage.bind(ScrollPage.find(c, "select.pagesize"), "change", function(event){
        var parent = ScrollPage.getContainer(this);
        ScrollPage.go(parent.getAttribute("href"), ScrollPage.getPageNum(this), this.value);
    });
    ScrollPage.bind(ScrollPage.find(c, "input.pagenum"), "focus", function(event){
        this.select();
    });

    ScrollPage.bind(ScrollPage.find(c, "div.refresh"), "click", function(){
        window.location.reload();
    });

    ScrollPage.bind(ScrollPage.find(c, "input.pagenum"), "change", function(event){
        var parent = ScrollPage.getContainer(this);
        var message = ScrollPage.validate(this.value);
        if(message != null) {
            alert(message);
            return false;
        }
        ScrollPage.go(parent.getAttribute("href"), this.value, ScrollPage.getPageSize(this));
    });
};

ScrollPage.getInt = function(value, defaultValue){
    return (isNaN(value) ? defaultValue : parseInt(value));
};

ScrollPage.getContainer = function(e){
    var p = e;
    while(p != null && p.className != "scrollpage" && (p != p.parentNode) && (p = p.parentNode) != null){};
    return p;
};

ScrollPage.find = function(parent, selector){
    var list = [];

    if(parent != null) {
        var search = selector.split(".");
        var nodeName = search[0];
        var elements = parent.getElementsByTagName(nodeName);

        for(var i = 0; i < elements.length; i++) {
            var e = elements[i];

            if(e.nodeType == 1) {
                if(search.length > 1) {
                    if(e.className != null && e.className.indexOf(search[1]) > -1) {
                        list[list.length] = e;
                    }
                }
                else {
                    list[list.length] = e;
                }
            }
        }
    }
    return list;
};

ScrollPage.getPageUrl = function(url, parameters, remove){
    var result = url;

    if(result != null) {
        var k = 0;
        var a = [];
        var name = null;
        var type = null;
        var value = null;

        for(var i in parameters) {
            name = i;

            if(remove != false) {
                result = Parameter.remove(result, name);
            }

            value = parameters[name];

            if(value != null) {
                type = typeof(value);

                if(type == "number" || type == "boolean") {
                    a[a.length] = name + "=" + encodeURIComponent(value);
                }
                else if(type== "string" && value.length > 0) {
                    a[a.length] = name + "=" + encodeURIComponent(value);
                }
                else if(type == "object" && value.length != null) {
                    for(var j = 0; j < value.length; j++) {
                        if(value[j] != null) {
                            a[a.length] = name + "=" + encodeURIComponent(value[j]);
                        }
                    }
                }
                else {
                    a[a.length] = name + "=" + encodeURIComponent(value.toString());
                }
            }
        }

        k = result.indexOf("?");

        if(k < 0) {
            k = result.indexOf("&");

            if(k > -1) {
                result = result.substring(0, k) + "?" + result.substring(k + 1);
            }
        }

        if(a.length > 0) {
            if(result.indexOf("?") < 0) {
                result = result + "?" + a.join("&");
            }
            else {
                result = result + "&" + a.join("&");
            }
        }

        return result;
    }

    return null;
};

ScrollPage.getScrollPageUrl = function(url, pageNum, pageSize){
    var parameters = {"pageNum": pageNum, "pageSize": pageSize};

    if(url != null) {
        return ScrollPage.getPageUrl(url, parameters, true);
    }
    else {
        return ScrollPage.getPageUrl(window.location.href, parameters, true);
    }
};

ScrollPage.getPageNum = function(src){
    var c = ScrollPage.getContainer(src);

    if(c != null) {
        var pageNum = parseInt(c.getAttribute("pageNum"));

        if(isNaN(pageNum) || pageNum < 1) {
            pageNum = 1;
        }

        return pageNum;
    }

    return 1;
};

ScrollPage.getPageSize = function(src){
    var c = ScrollPage.getContainer(src);

    if(c != null) {
        var pageSize = parseInt(c.getAttribute("pageSize"));

        if(isNaN(pageSize) || pageSize < 1) {
            pageSize = 1;
        }

        return pageSize;
    }

    return 10;
};

ScrollPage.getCount = function(src){
    var c = ScrollPage.getContainer(src);

    if(c != null) {
        var count = parseInt(c.getAttribute("count"));

        if(isNaN(count)) {
            count = 0;
        }
        return count;
    }
    return 0;
};

ScrollPage.submit = function(event, instance){
    var e1 = this.elements["pageNum"];
    var e2 = this.elements["pageSize"];

    if(e1 == null) {
        e1 = document.createElement("input");
        e1.type = "text";
        e1.name = "pageNum";
        this.appendChild(e1);
    }

    if(e2 == null) {
        e2 = document.createElement("input");
        e2.type = "text";
        e2.name = "pageSize";
        this.appendChild(e2);
    }

    e1.value = instance.getPageNum();
    e2.value = instance.getPageSize();
    return false;
};

ScrollPage.go = function(href, pageNum, pageSize){
    if(href != null) {
        var url = href.replace("%s", pageNum).replace("%s", pageSize);
        ScrollPage.redirect(url);
    }
    else {
        ScrollPage.scroll(pageNum, pageSize);
    }
};

ScrollPage.scroll = function(pageNum, pageSize){
    var url = ScrollPage.getScrollPageUrl(window.location.href, pageNum, pageSize);
    ScrollPage.redirect(url);
};

ScrollPage.validate = function(page){
    var n = parseInt(page);

    if(isNaN(n) == true) {
        return "您输入的不是数字 !";
    }

    if(n < 1 || n > this.total) {
        return "无效的页码 !";
    }

    if(n == this.pageNum) {
        return "当前已经是第" + n + "页 !";
    }
    return null;
};

ScrollPage.refresh = function(){
    window.location.reload();
};

ScrollPage.setPageInfo = function(e, pattern){
    var text = pattern;

    if(text != null) {
        text = text.replace("${count}", this.count);
        text = text.replace("${from}", this.startRow);
        text = text.replace("${to}", this.endRow);
    }

    var e = this.getElement(this.getContainer(), "div.pageinfo");

    if(e != null) {
        e.innerHTML = text;
    }
};

ScrollPage.redirect = function(url){
    if(document.all != null) {
        var a = document.createElement("a");
        a.setAttribute("href", url);
        a.setAttribute("target", "_self");
        a.style.cssText = "width: 0px; height: 0px; font-size: 0px; display: none;";
        a.onclick = function(){return true;};
        document.body.appendChild(a);
        a.click();
    }
    else {
        window.location.href = url;
    }
};

ScrollPage.form = function(form){
    ScrollPage.addEventListener(form, "submit", function(event){
        return ScrollPage.submit.apply(this, [event]);
    });
};

ScrollPage.bind = function(list, type, handler){
    for(var i = 0; i < list.length; i++) {
        ScrollPage.addEventListener(list[i], type, handler);
    }
};

ScrollPage.addEventListener = function(target, type, handler){
    if(target != null) {
        var method = function(event){
            return handler.call(target, (event || window.event));
        }

        if(target.attachEvent) {
            target.attachEvent("on" + type, method);
        }
        else if(target.addEventListener) {
            target.addEventListener(type, method, false);
        }
        else {
            target["on" + type] = method;
        }
    }
};

ScrollPage.render = function(src){
    var e = src;

    if(typeof(src) == "string") {
        e = document.getElementById(id);
    }

    if(e == null) {
        return;
    }

    var pageNum = parseInt(e.getAttribute("pageNum"));
    var pageSize = parseInt(e.getAttribute("pageSize"));
    var count = parseInt(e.getAttribute("count"));
    var theme = e.getAttribute("theme");

    if(isNaN(pageNum)) {
        pageNum = 1;
    }

    if(isNaN(pageSize)) {
        pageSize = 20;
    }

    if(isNaN(count)) {
        count = 0;
    }

    if(theme != "2") {
        e.innerHTML = this.build1(pageNum, pageSize, count);
    }
    else {
        e.innerHTML = this.build2(pageNum, pageSize, count);
    }
};

ScrollPage.build1 = function(pageNum, pageSize, count){
    var a = [];
    var f = true;
    var b = [5, 10, 15, 20, 30, 40];
    var total = Math.floor((count + pageSize - 1) / pageSize);
    a[a.length] = "<div class=\"pagesize\">";
    a[a.length] = "<select class=\"pagesize\">";

    for(var i = 0; i < b.length; i++) {
        if(pageSize == b[i]) {
            f = false;
            a[a.length]="<option value=\"" + b[i] + "\" selected=\"true\">" + b[i] + "</option>";
        }
        else {
            a[a.length]="<option value=\"" + b[i] + "\">" + b[i] + "</option>";
        }
    }

    if(f) {
        a[a.length]="<option value=\"" + pageSize + "\" selected=\"true\">" + pageSize + "</option>";
    }

    a[a.length] = "</select>";
    a[a.length] = "</div>";
    a[a.length] = "<div class=\"separator\"></div>";

    if(pageNum > 1) {
        a[a.length] = "<div class=\"first\" pageNum=\"1\" title=\"第一页\"></div><div class=\"prev\" pageNum=\"" + (pageNum - 1) + "\" title=\"上一页\"></div>";
    }
    else {
        a[a.length] = "<div class=\"first_disabled\" pageNum=\"1\" title=\"第一页\"></div><div class=\"prev_disabled\" pageNum=\"1\" title=\"上一页\"></div>";
    }
    a[a.length] = "    <div class=\"separator\"></div>";
    a[a.length] = "    <div class=\"pagenum\">第 <input type=\"text\" class=\"pagenum\" title=\"页码\" value=\"" + pageNum + "\"/> 页</div>";
    a[a.length] = "    <div class=\"separator\"></div>";

    if(pageNum < total) {
        a[a.length] = "<div class=\"next\" pageNum=\"" + (pageNum + 1) + "\" title=\"下一页\"></div>";
        a[a.length] = "<div class=\"last\" pageNum=\"" + total + "\" title=\"末一页\"></div>";
    }
    else {
        a[a.length] = "<div class=\"next_disabled\" pageNum=\"" + pageNum + "\" title=\"下一页\"></div>";
        a[a.length] = "<div class=\"last_disabled\" pageNum=\"" + total + "\" title=\"末一页\"></div>";
    }
    a[a.length] = "    <div class=\"separator\"></div>";
    a[a.length] = "    <div class=\"refresh\"></div>";
    a[a.length] = "    <div class=\"separator\"></div>";
    a[a.length] = "    <div class=\"pageinfo\">共 [" + count + "] 项</div>";
    return a.join("");
};

ScrollPage.build2 = function(pageNum, pageSize, count){
    var page = {"pageNum": pageNum, "pageSize": pageSize, "count": count};

    if(page.href == null || page.href.trim().length() < 1) {
        page.href = "javascript:void(0)";
    }

    if(page.pattern == null) {
        page.pattern = "";
    }

    var buffer = [];
    var count = Math.floor((page.count + (page.pageSize - 1)) / page.pageSize);
    var pages = this.getPages(page.pageNum, count, 7);

    if(page.pageNum > 1) {
        var prev = this.replace(page.href, "%s", page.pageNum - 1);
        buffer[buffer.length] = "<a class=\"prev page\" href=\"" + prev + "\" pageNum=\"" + (page.pageNum - 1) + "\" title=\"上一页\"><span class=\"prev\"></span></a>";
    }
    else {
        var prev = "javascript:void(0)";
        buffer[buffer.length] = "<a class=\"prev\" href=\"" + prev + "\" pageNum=\"" + (page.pageNum - 1) + "\" title=\"上一页\"><span class=\"prev\"></span></a>";
    }

    for(var i = 0; i < pages.length; i++) {
        var n = pages[i];

        if(n != 0) {
            if(n == page.pageNum) {
                buffer[buffer.length] = "<a class=\"active page\" href=\"" + this.replace(page.href, "%s", n) + "\" pageNum=\"" + n + "\">" + n + "</a>";
            }
            else {
                buffer[buffer.length] = "<a class=\"page\" href=\"" + this.replace(page.href, "%s", n) + "\" pageNum=\"" + n + "\">" + n + "</a>";
            }
        }
        else {
            buffer[buffer.length] = "<span class=\"ellipsis\">...</span>";
        }
    }

    // buffer[buffer.length] = "<span class=\"pagenum\">";
    // buffer[buffer.length] = "<input type=\"text\" class=\"pagenum\" value=\"" + pageNum + "\"/>/" + count + "页";
    // buffer[buffer.length] = "</span>";

    if(page.pageNum < count) {
        var next = this.replace(page.href, "%s", page.pageNum + 1);
        buffer[buffer.length] = "<a href=\"" + next + "\" class=\"next page\" pageNum=\"" + (page.pageNum + 1) + "\" title=\"下一页\"><span class=\"next\"></span></a>";
    }
    else {
        var next = "javascript:void(0)";
        buffer[buffer.length] = "<a href=\"" + next + "\" class=\"next\" pageNum=\"" + (page.pageNum + 1) + "\" title=\"下一页\"><span class=\"next\"></span></a>";
    }

    var info = page.pattern;

    if(info != null) {
        info = this.replace(info, "!{pageSize}", page.pageSize);
        info = this.replace(info, "!{pageNum}", page.pageNum);
        info = this.replace(info, "!{total}", page.count);
        info = this.replace(info, "!{count}", count);
        buffer[buffer.length] = info;
    }
    return buffer.join("\r\n");
};

ScrollPage.allocate = function(length, value){
    var a = [];

    for(var i = 0; i < length; i++) {
        a[i] = value;
    };
    return a;
};

ScrollPage.getPages = function(num, pages, size){
    var result = null;

    if(pages <= (size + 4)) {
        var length = (pages > 0 ? pages : 1);
        result = this.allocate(length, 0);

        for(var i = 0; i < length; i++) {
            result[i] = i + 1;
        }
        return result;
    }

    var start = Math.floor(num - size / 2);
    var end = start + size;

    if(start < 3) {
        start = 1;
        end = start + size + 1;
        result = this.allocate(size + 3, 0);
    }
    else if((start + size) >= pages) {
        start = pages - size;
        end = start + size;
        result = this.allocate(size + 3, 0);
    }
    else {
        end = start + size;
        result = this.allocate(size + 4, 0);
    }

    result[0] = 1;
    result[result.length - 1] = pages;

    if(start >= 3) {
        if(start == 3) {
            result[1] = 2;
        }

        for(var i = start; i < end; i++) {
            result[i - start + 2] = i;
        }
    }
    else {
        start = 1;
        end = size + 2;

        for(var i = start; i < end; i++) {
            result[i - start] = i;
        }
    }
    return result;
};

ScrollPage.replace = function(source, search, page){
    if(source == null) {
        return "";
    }

    if(search == null) {
        return source;
    }

    var s = 0;
    var e = 0;
    var buffer = [];

    do {
        e = source.indexOf(search, s);

        if(e == -1) {
            buffer[buffer.length] = source.substring(s);
            break;
        }
        else {
            buffer[buffer.length] = source.substring(s, e) + page;
            s = e + search.length;
        }
    }
    while(true);
    return buffer.join("");
};

ScrollPage.getCookie = function(name){
    var value = null;

    if(document.cookie && document.cookie != "") {
        var cookies = document.cookie.split(';');

        for(var i = 0; i < cookies.length; i++) {
            var cookie = Util.trim(cookies[i]);

            if(cookie.substring(0, name.length + 1) == (name + "=")) {
                value = decodeURIComponent(cookie.substring(name.length + 1));

                break;
            }
        }
    }
    return value;
};

(function(){
    ScrollPage.addEventListener(window, "load", function(){
        var list = document.getElementsByName("scrollpage");

        for(var i = 0; i < list.length; i++) {
            ScrollPage.render(list[i]);
            ScrollPage.setup(list[i]);
        }
    });
})();
