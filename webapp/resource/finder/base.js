/**
 * Copyright (C) 2003-2008 xuesong.net
 * http://www.xuesong.net
 * Author: xuesong.net
 **/
var Domain = {};
Domain.host = "http://" + window.location.host;
Domain.resource = "http://" + window.location.host;

var CookieUtil = {};
CookieUtil.trim = function(s){return (s != null ? s.replace(/(^\s*)|(\s*$)/g, "") : "");};
CookieUtil.setCookie = function(cookie) {
    var expires = "";
    if(cookie.value == null) {
        cookie.value = "";
        cookie.expires = -1;
    }

    if(cookie.expires != null) {
        var date = null;

        if(typeof(cookie.expires) == "number") {
            date = new Date();
            date.setTime(date.getTime() + cookie.expires * 1000);
        }
        else if(cookie.expires.toUTCString != null) {
            date = cookie.expires;
        }
        expires = "; expires=" + date.toUTCString();
    }
    var path = cookie.path ? "; path=" + (cookie.path) : "";
    var domain = cookie.domain ? "; domain=" + (cookie.domain) : "";
    var secure = cookie.secure ? "; secure" : "";
    document.cookie = [cookie.name, "=", (cookie.value != null ? encodeURIComponent(cookie.value) : ""), expires, path, domain, secure].join("");
};

CookieUtil.getCookie = function(name) {
    var value = null;

    if(document.cookie && document.cookie != "") {
        var cookies = document.cookie.split(';');
        for(var i = 0; i < cookies.length; i++) {
            var cookie = CookieUtil.trim(cookies[i]);

            if(cookie.substring(0, name.length + 1) == (name + "=")) {
                value = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return value;
};

CookieUtil.remove = function(name, path) {
    CookieUtil.setCookie({"name": name, "path": path});
};

(function() {
    var reload = CookieUtil.getCookie("reload");

    if(reload == "true") {
        CookieUtil.remove("reload", "/");
        window.location.reload();
    }
})();

var Response = {};

Response.error = function(result) {
    alert("系统错误，请稍后再试！");
};

Response.success = function(result, success){
    if(result == null){
        alert("系统错误，请稍后再试！");
        return false;
    }

    if(result.code == 0 || result.status == 200){
        if(success != null){
            success(result.value);
        }
        return true;
    }

    if(result.message != null){
        alert(result.message);
    }
    else{
        alert("系统错误，请稍后再试！");
    }
    return false;
};

var PageContext = {};

PageContext.getContextPath = function(){
    if(this.contextPath != null){
        return this.contextPath;
    }

    var contextPath = this.getAttribute("contextPath");

    if(contextPath == null || contextPath == "/"){
        contextPath = "";
    }
    return (this.contextPath = contextPath);
};

PageContext.getAttribute = function(name, defaultValue){
    var e = document.getElementById("pageContext");

    if(e == null){
        return null;
    }
    var value = e.getAttribute(name);
    return (value != null ? value : defaultValue);
};

PageContext.getObject = function(name){
    var data = this.getAttribute(name);

    if(data == null){
        return null;
    }

    try {
        return window.eval("(" + data + ")");
    }
    catch(e) {
    }
    return null;
};

PageContext.getInt = function(name){
    var value = this.getAttribute(name);
    if(value == null){
        return 0;
    }
    value = parseInt(value);
    return (isNaN(value) ? 0 : value);
};

PageContext.getLong = function(name){
    var value = this.getAttribute(name);
    if(value == null){
        return 0;
    }
    value = parseInt(value);
    return (isNaN(value) ? 0 : value);
};

PageContext.back = function() {
    CookieUtil.setCookie({"name": "reload", "value": "true", "path": "/"});
    window.history.back();
};

var FormUtil = {};

FormUtil.stringify = function(form) {
    if(form == null) {
        return "";
    }

    var buffer = [];
    var elements = form.elements;

    for(var i = 0, length = elements.length; i < length; i++) {
        var e = elements[i];
        var name = e.name;
        var type = e.type;
        var nodeName = e.nodeName.toLowerCase();

        if(e.disabled == true) {
            continue;
        }

        if(nodeName == "input") {
            switch(type) {
                case "radio": {
                    if(e.checked == true) {
                        buffer[buffer.length] = name + "=" + encodeURIComponent(e.value);
                    }
                    break;
                }
                case "checkbox": {
                    if(e.checked == true) {
                        buffer[buffer.length] = name + "=" + encodeURIComponent(e.value);
                    }
                    break;
                }
                case "image": {
                    break;
                }
                case "button": {
                    break;
                }
                case "submit": {
                    break;
                }
                case "reset": {
                    break;
                }
                default: {
                    buffer[buffer.length] = name + "=" + encodeURIComponent(e.value);
                    break;
                }
            }
        }
        else if(nodeName == "select") {
            /**
             * type: [select-one | select-multiple]
             */
            if(type == "select-multiple") {
                var options = e.options;

                for(var j = 0, length = options.length; j < length; j++) {
                    if(options[j].selected == true) {
                        buffer[buffer.length] = name + "=" + encodeURIComponent(options[j].value);
                    }
                }
            }
            else {
                buffer[buffer.length] = name + "=" + encodeURIComponent(e.value);
            }
        }
        else if(nodeName == "textarea") {
            buffer[buffer.length] = name + "=" + encodeURIComponent(e.value);
        }
    }
    return buffer.join("&");
};

var ImageUtil = {};

ImageUtil.resize = function(img, maxWidth, maxHeight){
    if(img.readyState != "complete") {
    }

    img.style.width = "auto";
    img.style.height = "auto";
    var widthHeight = (img.offsetWidth / img.offsetHeight);
    var heightWidth = (img.offsetHeight / img.offsetWidth);

    var realWidth = maxWidth
    var realHeight = Math.floor(maxWidth / widthHeight);

    if(realHeight > maxHeight) {
        realHeight = maxHeight;
        realWidth = Math.floor(maxHeight / heightWidth);
    }

    if(img.offsetWidth < maxWidth && img.offsetHeight < maxHeight) {
        realWidth = img.offsetWidth
        realHeight = img.offsetHeight;
    }

    if(img.parentNode.className == "img-float") {
        img.parentNode.style.width = (realWidth + 20) + "px";
        img.parentNode.style.height = realHeight + "px";
    }

    img.style.width = realWidth + "px";
    img.style.height = realHeight + "px";
};

var StringUtil = {};

/**
 * @param source
 * @return boolean
 */
StringUtil.trim = function(source){return (source != null ? source.replace(/(^\s*)|(\s*$)/g, "") : "");};

/**
 * @param source
 * @return boolean
 */
StringUtil.startsWith = function(source, search) {
    if(source.length >= search.length) {
        return (source.substring(0, search.length) == search);
    }

    return false;
};

/**
 * @param source
 * @return boolean
 */
StringUtil.endsWith = function(source, search) {
    if(source.length >= search.length) {
        return (source.substring(source.length - search.length) == search);
    }

    return false;
};

/**
 * @param source
 * @param context
 * @return String
 */
StringUtil.replace = function(source, context){
    var c = null;
    var result = [];

    for(var i = 0, length = source.length; i < length; i++) {
        c = source.charAt(i);

        if(c == "$" && i < length - 1 && source.charAt(i + 1) == "{") {
            var buffer = [];

            for(var j = i + 2; j < length; j++) {
                i = j;
                c = source.charAt(j);

                if(c == "}") {
                    var value = context.getValue(buffer.join(""));

                    if(value != null) {
                        result.push(value.toString());
                    }

                    break;
                }
                else {
                    buffer.push(c);
                }
            }
        }
        else {
            result.push(c);
        }
    }
    return result;
};

/**
 * @param source
 * @return String
 */
StringUtil.text = function(source){
    if(source == null) {
        return "";
    }

    var c = null;
    var buffer = [];

    for(var i = 0, length = source.length; i < length; i++) {
        c = source.charAt(i);

        switch (c) {
            case "\\": {
                buffer.push("\\\\"); break;
            }
            case "\'": {
                buffer.push("\\\'"); break;
            }
            case "\"": {
                buffer.push("\\\""); break;
            }
            case "\r": {
                buffer.push("\\r"); break;
            }
            case "\n": {
                buffer.push("\\n"); break;
            }
            case "\t": {
                buffer.push("\\t"); break;
            }
            case "\b": {
                buffer.push("\\b"); break;
            }
            case "\f": {
                buffer.push("\\f"); break;
            }
            default : {
                buffer.push(c); break;
            }
        }
    }
    return buffer.join("");
};

var HtmlUtil = {};

/**
 * @param source
 * @param crlf
 * @return String
 */
HtmlUtil.encode = function(source, crlf){
    if(source == null) {
        return "";
    }

    if(crlf == null || crlf == undefined) {
        crlf = "\n";
    }

    var c;
    var buffer = [];

    for(var i = 0, length = source.length; i < length; i++) {
        c = source.charAt(i);

        switch (c) {
            case "&": {
                buffer.push("&amp;");
                break;
            }
            case "\"": {
                buffer.push("&quot;");
                break;
            }
            case "<": {
                buffer.push("&lt;");
                break;
            }
            case ">": {
                buffer.push("&gt;");
                break;
            }
            case "\r": {
                if((i + 1) < size) {
                    if(source.charAt(i + 1) == "\n") {
                        buffer.push(crlf);
                        i++;
                    }
                    else {
                        buffer.push(c);
                    }
                }
                else {
                    buffer.push(c);
                }

                break;
            }
            case "\n": {
                buffer.push(crlf);
                break;
            }
            default : {
                buffer.push(c);
                break;
            }
        }
    }
    return buffer.join("");
};

var URL = {};

URL.serialize = function(json){
    var a = [];
    if(json != null) {
        if(typeof(json) == "object") {
            for(var name in json) {
                var value = json[name];
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