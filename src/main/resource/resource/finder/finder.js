var StringUtil = {};
StringUtil.trim = function(s) {return (s != null ? new String(s).replace(/(^\s*)|(\s*$)/g, "") : "");}
StringUtil.startsWith = function(source, search) {
    if(source.length >= search.length) {
        return (source.substring(0, search.length) == search);
    }
    return false;
};

StringUtil.endsWith = function(source, search) {
    if(source.length >= search.length) {
        return (source.substring(source.length - search.length, source.length) == search);
    }
    return false;
};

/**
 * @param text
 * @param value
 * @return boolean
 */
StringUtil.contains = function(text, value) {
    if(text == null || value == null) {
        return false;
    }

    var array = text;

    if(typeof(text) == "string") {
        if(text == "*") {
            return true;
        }
        array = text.split(",");
    }

    for(var i = 0; i < array.length; i++) {
        if(array[i] != null) {
            array[i] = StringUtil.trim(array[i]);

            if(array[i] == value) {
                return true;
            }
        }
    }
    return false;
};

/**
 * @param source
 * @param search
 * @param replacement
 * @return String
 */
StringUtil.replace = function(source, search, replacement) {
    if(source == null) {
        return "";
    }

    if(search == null || search.length < 1) {
        return source;
    }

    if(replacement == null) {
        replacement = "";
    }

    var s = 0;
    var e = 0;
    var d = search.length;
    var buffer = [];

    while(true) {
        e = source.indexOf(search, s);

        if(e == -1) {
            buffer[buffer.length] = source.substring(s);
            break;
        }
        buffer[buffer.length] = source.substring(s, e);
        buffer[buffer.length] = replacement;
        s = e + d;
    }
    return buffer.join("");
};

var HtmlUtil = {};

HtmlUtil.replacement = {
    "<": "&lt;",
    ">": "&gt;",
    "\"": "&quot;",
    "\u00ae": "&reg;",
    "\u00a9": "&copy;"
};

HtmlUtil.replace = function(e) {return com.skin.util.Html.replacement[e];};

HtmlUtil.encode = function(source, crlf) {
    if(source == null) {
        return "";
    }

    if(crlf == null) {
        crlf = "";
    }
    return source.replace(new RegExp("[<>\"\\u00ae\\u00a9]", "g"), HtmlUtil.replace).replace(new RegExp("\\r?\\n", "g"), crlf);
};

var I18N = {};

/**
 * 资源文件覆盖该变量
 */
I18N.bundle = {};
I18N.format = function(pattern) {
    var c;
    var buffer = [];
    var args = arguments;

    /**
     * var pattern = "这是一个示例\\{\\}, 这是一个示例{1}, 这是一个示例{2}";
     */
    for(var i = 0, length = pattern.length; i < length; i++) {
        c = pattern.charAt(i);

        if(c == "\\" && i < length - 1) {
            i = i + 1;
            c = pattern.charAt(i);

            if(c == "{" || c == "}") {
                buffer[buffer.length] = c;
                continue;
            }
            else {
                buffer[buffer.length] = "\\";
                buffer[buffer.length] = c;
                continue;
            }
        }
        else if(c == "{") {
            var k = pattern.indexOf("}", i + 1);

            if(k > -1) {
                var index = parseInt(pattern.substring(i + 1, k));

                if(!isNaN(index) && index > 0 && index < args.length) {
                    buffer[buffer.length] = (args[index] || "");
                }
                i = k;
            }
            else {
                buffer[buffer.length] = pattern.substring(i + 1);
                break;
            }
        }
        else {
            buffer[buffer.length] = c;
        }
    }
    return buffer.join("");
};

I18N.getLang = function(/* String */ key /* Object ... args */) {
    var value = this.bundle[key];

    if(value == null || value == undefined) {
        return key;
    }

    var args = [value].concat([].slice.call(arguments, 1));
    return this.format.apply(this, args);
};

var DispatchAction = {};

DispatchAction.listeners = [];

DispatchAction.dispatch = function(pattern, action) {
    var listeners = this.listeners;
    var object = {"pattern": pattern, "action": action};
    listeners[listeners.length] = object;
    return object;
};

DispatchAction.call = function(hash, rule) {
    var pattern = rule.pattern;
    var action = rule.action;

    if(pattern == "*") {
        action.apply(null, []);
        return true;
    }

    var regExp = new RegExp(pattern);
    var arr = regExp.exec(hash);

    if(arr != null) {
        var args = [];

        for(var i = 1; i < arr.length; i++) {
            args[args.length] = arr[i];
        }

        action.apply(null, args);
        return true;
    }
    return false;
};

DispatchAction.execute = function(hash) {
    var hash = (hash != null ? hash : window.location.hash);

    if(hash == null || hash == undefined) {
        hash = "";
    }

    hash = hash.replace(/(^\s*)|(\s*$)/g, "");

    if(hash.charAt(0) == "#") {
        hash = hash.substring(1);
    }

    var listeners = this.listeners;

    for(var i = 0, length = listeners.length; i < length; i++) {
        DispatchAction.call(hash, listeners[i]);
    }
};

var ResourceLoader = {};

ResourceLoader.css = function() {
    var doc, url;

    if(arguments.length == 0) {
        return;
    }
    else if(arguments.length == 1) {
        doc = document;
        url = arguments[0];
    }
    else {
        doc = arguments[0];
        url = arguments[1];
    }

    var e = doc.createElement("link");
    e.rel = "stylesheet";
    e.type = "text/css";
    e.href = url;
    doc.body.appendChild(e);
};

ResourceLoader.script = function() {
    var doc, url;

    if(arguments.length == 0) {
        return;
    }
    else if(arguments.length == 1) {
        doc = document;
        url = arguments[0];
    }
    else {
        doc = arguments[0];
        url = arguments[1];
    }

    var e = doc.createElement("script");
    e.type = "text/javascript";
    e.src = url;
    doc.body.appendChild(e);
};

var FileType = {};

FileType.executors = [];

FileType.registe = function(types, handler) {
    this.executors[this.executors.length] = {"types": types, "handler": handler};
};

FileType.execute = function(file, options) {
    if(options.download == null) {
        var executors = this.executors;
        var extension = this.getType(file.fileName);

        for(var i = 0; i < executors.length; i++) {
            var item = executors[i];

            if(this.contains(item.types, extension)) {
                item.handler(file, options);
                return true;
            }
        }
    }

    /**
     * default executor
     */
    var url = null;
    var params = [];
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath();
    params[params.length] = "host=" + encodeURIComponent(host);
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path + "/" + file.fileName);

    if(options.download != null) {
        url = Finder.getRequestURI() + "?action=finder.download";
    }
    else {
        url = Finder.getRequestURI() + "?action=finder.display";
    }

    if(options.target != null) {
        window.open(url + "&" + params.join("&"), options.target);
    }
    else {
        window.location.href = url + "&" + params.join("&");
    }
    return true;
};

FileType.contains = function(source, searchment) {
    var a = source.split(",");

    for(var i = 0; i < a.length; i++) {
        var e = StringUtil.trim(a[i]);

        if(e == searchment) {
            return true;
        }
    }
    return false;
};

/**
 * @param path
 * @return String
 */
FileType.getType = function(path) {
    return FileType.getExtension(path).toLowerCase();
};

/**
 * @param path
 * @return
 */
FileType.getName = function(path) {
    if(path != null && path.length > 0) {
        var c = null;
        var i = path.length - 1;
        var buffer = [];

        for(; i > -1; i--) {
            c = path.charAt(i);

            if(c == "/" || c == "\\" || c == ":") {
                break;
            }
        }

        buffer[buffer.length] = path.substring(i + 1);
        return buffer.join("");
    }
    return "";
};

/**
 * @param path
 * @return String
 */
FileType.getExtension = function(path) {
    if(path != null && path.length > 0) {
        var c = null;
        var i = path.length - 1;
        var buffer = [];

        for(; i > -1; i--) {
            c = path.charAt(i);

            if(c == ".") {
                break;
            }
            else if(c == "/" || c == "\\" || c == ":") {
                break;
            }
        }

        if(c == ".") {
            buffer[buffer.length] = path.substring(i + 1);
        }
        return buffer.join("");
    }
    return "";
};

var FileSort = {};

FileSort.marge = function(folderList, fileList, files) {
    var index = 0;

    for(var i = 0; i < folderList.length; i++) {
        files[index] = folderList[i];
        index++;
    }

    for(var i = 0; i < fileList.length; i++) {
        files[index] = fileList[i];
        index++;
    }
};

FileSort.byName = function(files) {
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++) {
        if(files[i].isFile) {
            fileList[fileList.length] = files[i];
        }
        else {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2) {
        var s1 = f1.fileName.toLowerCase();
        var s2 = f2.fileName.toLowerCase();

        if(s1 == s2) {
            return 0;
        }

        if(s1 > s2) {
            return 1;
        }
        else {
            return -1;
        }
    };

    fileList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

FileSort.byType = function(files) {
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++) {
        if(files[i].isFile) {
            fileList[fileList.length] = files[i];
        }
        else {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2) {
        var s1 = FileType.getType(f1.fileName).toLowerCase();
        var s2 = FileType.getType(f2.fileName).toLowerCase();

        if(s1 == s2) {
            return 0;
        }

        if(s1 > s2) {
            return 1;
        }
        else {
            return -1;
        }
    };

    fileList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

FileSort.bySize = function(files) {
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++) {
        if(files[i].isFile) {
            fileList[fileList.length] = files[i];
        }
        else {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2) {
        return f1.fileSize - f2.fileSize;
    };

    fileList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

FileSort.byLastModified = function(files) {
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++) {
        if(files[i].isFile) {
            fileList[fileList.length] = files[i];
        }
        else {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2) {
        return f1.lastModified - f2.lastModified;
    };

    fileList.sort(comparator);
    folderList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

var Finder = {};

Finder.setCookie = function(cookie) {
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

Finder.getCookie = function(name) {
    var value = null;
    if(document.cookie && document.cookie != "") {
        var cookies = document.cookie.split(';');
        for(var i = 0; i < cookies.length; i++) {
            var cookie = StringUtil.trim(cookies[i]);
            if(cookie.substring(0, name.length + 1) == (name + "=")) {
                value = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return value;
};

Finder.getLocalVariable = function(name) {
    if(typeof(window.localStorage) != "undefined") {
        return window.localStorage[name];
    }
    else {
        return Finder.getCookie(name);
    }
};

Finder.setLocalVariable = function(name, value) {
    if(typeof(window.localStorage) != "undefined") {
        window.localStorage[name] = value;
    }
    else {
        Finder.setCookie({"name": name, "value": value, "expires": 7 * 24 * 60 * 60});
    }
};

Finder.setAttribute = function(name, value) {
    var e = document.getElementById("pageContext");

    if(e != null) {
        if(value == null || value == undefined) {
            e.removeAttribute(name);
        }
        else {
            e.setAttribute(name, value);
        }
    }
};

Finder.getAttribute = function(name, defaultValue) {
    var value = null;
    var e = document.getElementById("pageContext");

    if(e != null) {
        value = e.getAttribute(name);
    }

    if(value == null || value == undefined) {
        value = defaultValue;
    }

    if(value != null && value != undefined) {
        return value;
    }
    return null;
};

Finder.getInteger = function(name, defaultValue) {
    var value = this.getAttribute(name);

    if(value == null || isNaN(value)) {
        return defaultValue;
    }
    return parseInt(value);
};

Finder.getContextPath = function() {
    if(this.contextPath == null || this.contextPath == undefined) {
        var contextPath = document.body.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/") {
            contextPath = "";
        }
        this.contextPath = contextPath;
    }
    return this.contextPath;
};

Finder.getRequestURI = function() {
    return window.location.pathname;
};

Finder.getHost = function() {
    if(this.host == null || this.host == undefined) {
        this.host = document.body.getAttribute("host");
    }
    return this.host;
};

Finder.getWorkspace = function() {
    if(this.workspace == null || this.workspace == undefined) {
        this.workspace = document.body.getAttribute("workspace");
    }
    return this.workspace;
};

Finder.getWork = function() {
    if(this.work == null || this.work == undefined) {
        this.work = document.body.getAttribute("work");

        if(this.work == null || this.work == "" || this.work == "\\") {
            this.work = "/";
        }
    }
    return this.work;
};

Finder.getPath = function() {
    if(this.path == null || this.path == undefined) {
        this.path = document.body.getAttribute("path");

        if(this.path == null || this.path == "/" || this.path == "\\") {
            this.path = "";
        }
    }
    return this.path;
};

Finder.getParentPath = function() {
    if(this.parentPath == null || this.parentPath == undefined) {
        this.parentPath = document.body.getAttribute("parent");

        if(this.parentPath == null || this.parentPath == "\\") {
            this.parentPath = "/";
        }
    }
    return this.parentPath;
};

Finder.getViewMode = function() {
    return Finder.getCookie("view_mode");
};

Finder.format = function(date) {
    if(date == null) {
        date = new Date();
    }

    if(typeof(date) == "number") {
        var temp = new Date();
        temp.setTime(date);
        date = temp;
    }

    var y = date.getFullYear();
    var M = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var m = date.getMinutes();
    var a = [];

    a[a.length] = y;
    a[a.length] = "-";

    if(M < 10) {
        a[a.length] = "0";
    }

    a[a.length] = M.toString();
    a[a.length] = "-";

    if(d < 10) {
        a[a.length] = "0";
    }

    a[a.length] = d.toString();

    a[a.length] = " ";
    
    if(h < 10) {
        a[a.length] = "0";
    }

    a[a.length] = h.toString();
    a[a.length] = ":";

    if(m < 10) {
        a[a.length] = "0";
    }

    a[a.length] = m.toString();
    return a.join("");
};

/**
 * 返回li元素
 */
Finder.getItem = function(name) {
    var element = null;

    Finder.each(function(e) {
        var file = Finder.getFile(e);

        if(file != null && file.fileName == name) {
            element = e;
            return false;
        }
        return true;
    });
    return element;
};

/**
 * @element li.item
 * @return {fileName: 'xxx', fileIcon: 'xxx', ...}
 */
Finder.getFile = function(element) {
    var parent = this.getParent(element);
    var isFile = parent.getAttribute("isFile");
    var fileIcon = parent.getAttribute("fileIcon");
    var fileName = parent.getAttribute("fileName");
    var lastModified = parent.getAttribute("lastModified");
    var file = {"fileName": fileName};

    if(isFile != "false") {
        file.isFile = true;
        file.fileIcon = fileIcon;
        file.fileSize = parseInt(parent.getAttribute("fileSize"));
        file.lastModified = parseInt(lastModified);
    }
    else {
        file.lastModified = parseInt(lastModified);
    }
    return file;
};

Finder.getFileList = function() {
    if(this.fileList == null) {
        var fileList = [];

        Finder.each(function(e) {
            var file = Finder.getFile(e);

            if(file != null) {
                fileList[fileList.length] = file;
            }
        });
        Finder.fileList = fileList;
    }
    return Finder.fileList;
};

Finder.open = function(file, options) {
    FileType.execute(file, (options || {}));
    return true;
};

Finder.download = function(file, options) {
    return this.open(file, {"download": "true"});
};

Finder.tail = function(file, options) {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath() + "/" + file.fileName;

    var params = [];
    params[params.length] = "host=" + encodeURIComponent(host);
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);

    if(options.target != null) {
        window.open(this.getRequestURI() + "?action=finder.tail&" + params.join("&"), options.target);
    }
    else {
         window.location.href = this.getRequestURI() + "?action=finder.tail&" + params.join("&");
    }
    return true;
};

Finder.less = function(file, options) {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath() + "/" + file.fileName;

    var params = [];
    params[params.length] = "host=" + encodeURIComponent(host);
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);

    if(options.target != null) {
        window.open(this.getRequestURI() + "?action=finder.less&" + params.join("&"), options.target);
    }
    else {
         window.location.href = this.getRequestURI() + "?action=finder.less&" + params.join("&");
    }
    return true;
};

Finder.grep = function(file, options) {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath() + "/" + file.fileName;

    var params = [];
    params[params.length] = "host=" + encodeURIComponent(host);
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);

    if(options.target != null) {
        window.open(this.getRequestURI() + "?action=finder.grep&" + params.join("&"), options.target);
    }
    else {
         window.location.href = this.getRequestURI() + "?action=finder.grep&" + params.join("&");
    }
    return true;
};

Finder.mkdir = function(options) {
    var params = [];
    params[params.length] = "host=" + encodeURIComponent(Finder.getHost());
    params[params.length] = "workspace=" + encodeURIComponent(Finder.getWorkspace());
    params[params.length] = "path=" + encodeURIComponent(Finder.getPath());
    params[params.length] = "name=" + encodeURIComponent("新建文件夹");

    jQuery.ajax({
        "type": "post",
        "url": this.getRequestURI() + "?action=finder.mkdir&" + params.join("&"),
        "dataType": "json",
        "error": function() {
            DialogUtil.alert(I18N.getLang("finder.message.system.error"));
        },
        "success": function(response) {
            var callback = options.callback;

            if(callback != null) {
                callback((response || {}));
            }
        }
    });
};

Finder.remove = function(files, options) {
    if(files.length < 1) {
        return;
    }

    var host = this.getHost();
    var workspace = this.getWorkspace();
    var path = this.getPath();

    if(options.force != true) {
        var message = null;

        if(files.length == 1) {
            message = I18N.getLang("finder.message.file.delete.confirm1", path + "/" + files[0].fileName);
        }
        else {
            message = I18N.getLang("finder.message.file.delete.confirm2", files.length);
        }

        DialogUtil.confirm(message, function(ok) {
            if(ok) {
                options.force = true;
                Finder.remove(files, options);
            }
        });
        return;
    }

    var params = {};
    params.path = [];

    for(var i = 0; i < files.length; i++) {
        params.path[ params.path.length] = (path + "/" + files[i].fileName);
    }

    jQuery.ajax({
        "type": "post",
        "url": this.getRequestURI() + "?action=finder.delete&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace),
        "dataType": "json",
        "data": jQuery.param(params, true),
        "error": function() {
            DialogUtil.alert(I18N.getLang("finder.message.system.error"));
        },
        "success": function(response) {
            var callback = options.callback;

            if(callback != null) {
                callback((response || {}));
            }
            Finder.fileList = null;
        }
    });
};

Finder.rename = function(file, options) {
    var params = [];
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath() + "/" + file.fileName;

    params[params.length] = "host=" + encodeURIComponent(host);
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);
    params[params.length] = "newName=" + encodeURIComponent(file.newName);

    jQuery.ajax({
        "type": "post",
        "url": this.getRequestURI() + "?action=finder.rename&" + params.join("&"),
        "dataType": "json",
        "error": function() {
            DialogUtil.alert(I18N.getLang("finder.message.system.error"));
        },
        "success": function(response) {
            var callback = options.callback;

            if(callback != null) {
                callback((response || {}));
            }
        }
    });
};

Finder.back = function() {
    var host = this.getHost();
    var workspace = this.getWorkspace();
    var parent = this.getParentPath();

    if(parent == null || parent.length < 1) {
        parent = "/";
    }
    window.location.href = this.getRequestURI() + "?action=finder.display&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(parent);
};

Finder.forward = function(host, workspace, path) {
    if(path != null) {
        window.location.href = this.getRequestURI() + "?action=finder.display&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path);
    }
    else {
        window.location.href = this.getRequestURI() + "?action=finder.display&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace);
    }
};

Finder.refresh = function(event) {
    var e = (event || window.event);

    if(e.ctrlKey == true) {
        window.open(window.location.href, "_blank");
    }
    else {
        window.location.reload();
    }
};

Finder.getFirst = function(handler) {
    var parent = document.getElementById("file-list");

    if(parent != null) {
        var list = parent.childNodes;

        for(var i = 0; i < list.length; i++) {
            var e = list[i];

            if(e.nodeType == 1 && e.className != null) {
                var className = e.className;

                if(className.indexOf("item") > -1) {
                     return e;
                }
            }
        }
    }
    return null;
};

Finder.getLast = function(handler) {
    var parent = document.getElementById("file-list");

    if(parent != null) {
        var list = parent.childNodes;

        for(var i = list.length - 1; i > -1; i--) {
            var e = list[i];

            if(e.nodeType == 1 && e.className != null) {
                var className = e.className;

                if(className.indexOf("item") > -1) {
                     return e;
                }
            }
        }
    }
    return null;
};

Finder.getSelected = function() {
    var list = [];

    Finder.each(function(e) {
        if(e.className.indexOf("selected") > -1) {
            list[list.length] = e;
        }
    });
    return list;
};

Finder.getNext = function(e) {
    var next = e;

    while((next = next.nextSibling) != null) {
        if(next.nodeType == 1) {
            return next;
        }
    }
    return null;
};

Finder.getPrevious = function(e) {
    var previous = e;

    while((previous = previous.previousSibling) != null) {
        if(previous.nodeType == 1) {
            return previous;
        }
    }
    return null;
};

Finder.each = function(handler) {
    var parent = document.getElementById("file-list");

    if(parent != null) {
        var list = parent.childNodes;

        for(var i = 0; i < list.length; i++) {
            var e = list[i];

            if(e.nodeType == 1 && e.className != null) {
                if(e.className.indexOf("item") > -1) {
                    var flag = handler(e);

                    if(flag == false) {
                        break;
                    }
                }
            }
        }
    }
};

Finder.setVisible = function(element, target, center) {
    var height = target.clientHeight;
    var scrollTop = target.scrollTop;
    var offsetTop = element.offsetTop;
    var clientHeight = element.clientHeight;

    if(target == document.body) {
        height = document.documentElement.clientHeight;
    }

    if(scrollTop > offsetTop) {
        var top = offsetTop;

        if(center == true) {
            top = top - Math.floor(top - (height / 2));
        }

        if(target == document.body) {
            document.body.scrollTop = top;
            document.documentElement.scrollTop = top;
        }
        else {
            target.scrollTop = top;
        }
    }

    if(offsetTop > (height + scrollTop - clientHeight)) {
        var top = offsetTop - height + clientHeight;

        if(center == true) {
            top = top - Math.floor(top - (height / 2));
        }

        if(target == document.body) {
            document.body.scrollTop = top;
            document.documentElement.scrollTop = top;
        }
        else {
            target.scrollTop = top;
        }
    }
};

Finder.scroll = function(offset, multiple) {
    var prev = null;
    var next = null;
    var active = this.active;

    if(active == null) {
        active = this.active = this.getFirst();

        if(active != null) {
            active.className = "item active selected";
        }
        return;
    }

    if(multiple == true) {
        if(offset > 0) {
            next = this.getNext(active);

            if(next != null) {
                if(next.className.indexOf("selected") > -1) {
                    active.className = "item";
                }

                next.className = "item active selected";
                this.active = next;
            }
        }
        else {
            prev = this.getPrevious(active);

            if(prev != null) {
                if(prev.className.indexOf("selected") > -1) {
                    active.className = "item";
                }

                prev.className = "item active selected";
                this.active = prev;
            }
        }

        if(active != this.active) {
            if(active.className.indexOf("selected") > -1) {
                active.className = "item selected";
            }
            else {
                active.className = "item";
            }
        }
        return;
    }

    var list = Finder.getSelected();

    if(list.length > 0) {
        for(var i = 0; i < list.length; i++) {
            list[i].className = "item";
        }
    }

    if(offset > 0) {
        next = this.getNext(active);

        if(next == null) {
            next = this.getFirst();
        }
    }
    else {
        next = this.getPrevious(active);

        if(next == null) {
            next = this.getLast();
        }
    }

    if(next != null) {
        next.className = "item active selected";
        this.active = next;
        this.setVisible(next, document.body);
    }
    else {
        this.active = null;
    }
};

Finder.reload = function(callback) {
    var host = this.getHost();
    var workspace = this.getWorkspace();
    var path = this.getPath();

    jQuery.ajax({
        "type": "get",
        "url": this.getRequestURI() + "?action=finder.getFileList&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path),
        "dataType": "json",
        "error": function() {
        },
        "success": function(response) {
            if(response == null || response.status != 200 || response.value == null) {
                if(callback != null) {
                    callback();
                }
                return;
            }

            var fileList = response.value.fileList;

            if(fileList != null) {
                Finder.fileList = fileList;
                var viewMode = Finder.getViewMode();

                if(viewMode == "outline") {
                    jQuery("#view-options li[option-value=outline]").click();
                }
                else {
                    jQuery("#view-options li[option-value=detail]").click();
                }
            }
            if(callback != null) {
                callback();
            }
        }
    });
};

Finder.getTooltip = function(work, path, file) {
    if(file.isFile) {
        return HtmlUtil.encode(path + "/" + file.fileName) + "&#10;类型: " + FileType.getType(file.fileName) + "文件&#10;修改日期: " + Finder.format(file.lastModified) + "&#10;大小: " + file.fileSize + " 字节";
    }
    else {
        return HtmlUtil.encode(work + path + "/" + file.fileName);
    }
};

Finder.list = function() {
    var container = document.getElementById("file-list");

    if(container == null) {
        return;
    }

    var index = 1;
    var buffer = [];
    var work = this.getWork();
    var path = this.getPath();
    var fileList = Finder.getFileList();
    var allows = Finder.getAttribute("display-operate-button", "");
    var buttons = {
        "tail":     "",
        "less":     "",
        "grep":     "",
        "open":     "",
        "download": "",
        "delete":   ""
    };

    for(var name in buttons) {
        if(StringUtil.contains(allows, name)) {
            buttons[name] = "<a action=\"finder-" + name + "\" href=\"javascript:void(0)\">" + I18N.getLang("finder.list.button." + name) +"</a>";
        }
    }

    for(var i = 0; i < fileList.length; i++) {
        var file = fileList[i];

        if(file.isFile != true) {
            buffer[buffer.length] = "<li class=\"item\" isFile=\"false\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" lastModified=\"" + file.lastModified + "\">";
            buffer[buffer.length] = "   <span class=\"icon\"><img src=\"" + this.getRequestURI() + "?action=res&path=/finder/images/folder.gif\"/></span>";
            buffer[buffer.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\" bind-event=\"dblclick\" title=\"" + Finder.getTooltip(work, path, file) + "\">" + HtmlUtil.encode(file.fileName) + "</a></span>";
            buffer[buffer.length] = "   <span class=\"fileSize\">&nbsp;</span>";
            buffer[buffer.length] = "   <span class=\"fileType\">文件夹</span>";
            buffer[buffer.length] = "   <span class=\"lastModified\">" + this.format(file.lastModified) + "</span>";
            buffer[buffer.length] = "   <span class=\"operate\">";
            buffer[buffer.length] = buttons["open"];
            buffer[buffer.length] = buttons["delete"];
            buffer[buffer.length] = "   </span>";
            buffer[buffer.length] = "</li>";
        }
        else {
            buffer[buffer.length] = "<li class=\"item\" fileIcon=\"" + file.fileIcon + "\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" fileSize=\"" + file.fileSize + "\" lastModified=\"" + file.lastModified + "\">";
            buffer[buffer.length] = "   <span class=\"icon\"><img src=\"" + this.getRequestURI() + "?action=res&path=/finder/icon/" + file.fileIcon + ".png\"/></span>";
            buffer[buffer.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\" bind-event=\"dblclick\" title=\"" + Finder.getTooltip(work, path, file) + "\">" + HtmlUtil.encode(file.fileName) + "</a></span>";
            buffer[buffer.length] = "   <span class=\"fileSize\">" + ByteUtil.getByteSize(file.fileSize) + "</span>";
            buffer[buffer.length] = "   <span class=\"fileType\">" + FileType.getType(file.fileName) + "文件</span>";
            buffer[buffer.length] = "   <span class=\"lastModified\">" + Finder.format(file.lastModified) + "</span>";
            buffer[buffer.length] = "   <span class=\"operate\">";
            buffer[buffer.length] = buttons["tail"];
            buffer[buffer.length] = buttons["less"];
            buffer[buffer.length] = buttons["grep"];
            buffer[buffer.length] = buttons["open"];
            buffer[buffer.length] = buttons["download"];
            buffer[buffer.length] = buttons["delete"];
            buffer[buffer.length] = "   </span>";
            buffer[buffer.length] = "</li>";
        }
    }

    container.innerHTML = buffer.join("");
    container.parentNode.className = "detail-view";
    container.parentNode.style.display = "block";
    container.setAttribute("view-mode", "detail");
    document.getElementById("head-view").style.display = "block";
    Finder.setCookie({"name": "view_mode", "value": "detail", "path": "/", "expires": 365 * 24 * 60 * 60 * 1000});
};

Finder.outline = function() {
    var container = document.getElementById("file-list");

    if(container == null) {
        return;
    }

    var fileList = Finder.getFileList();
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var work = Finder.getWork();
    var path = Finder.getPath();
    var map = {
        "ico":  "ico",
        "jpg":  "jpg",
        "jpeg": "jpeg",
        "gif":  "gif",
        "bmp":  "bmp",
        "png":  "png"
    };

    var b = [];

    for(var i = 0; i < fileList.length; i++) {
        var file = fileList[i];

        if(file.isFile) {
            b[b.length] = "<li class=\"item\" isFile=\"true\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" fileIcon=\"" + file.fileIcon + "\" fileSize=\"" + file.fileSize + "\" lastModified=\"" + file.lastModified + "\">";
            b[b.length] = "<div class=\"box\">";
            b[b.length] = "<div class=\"icon\">";

            var type = FileType.getType(file.fileName).toLowerCase();

            if(map[type] != null) {
                b[b.length] = "<img onload=\"Finder.resize(this)\" src=\"" + this.getRequestURI() + "?action=finder.display&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path + "/" + file.fileName) + "\"/>";
            }
            else {
                b[b.length] = "<img class=\"icon\" src=\"" + this.getRequestURI() + "?action=res&path=/finder/icon/" + file.fileIcon + ".png\"/>";
            }
            b[b.length] = "</div>";
        }
        else {
            b[b.length] = "<li class=\"item\" isFile=\"false\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" lastModified=\"" + file.lastModified + "\">";
            b[b.length] = "<div class=\"box\">";
            b[b.length] = "<div class=\"icon\"><img class=\"icon\" src=\"" + this.getRequestURI() + "?action=res&path=/finder/icon/folder.png\"/></div>";
        }
        b[b.length] = "<div class=\"filename\" title=\"" + Finder.getTooltip(work, path, file) + "\">";
        b[b.length] = "    <a class=\"file\" href=\"javascript:void(0)\" bind-event=\"dblclick\">" + file.fileName + "</a>";
        b[b.length] = "</div>";
        b[b.length] = "</div>";
        b[b.length] = "</li>";
    }

    container.innerHTML = b.join("");
    container.parentNode.className = "outline-view";
    container.setAttribute("view-mode", "outline");
    document.getElementById("head-view").style.display = "none";
    document.getElementById("file-view").style.display = "block";
    Finder.setCookie({"name": "view_mode", "value": "outline", "path": "/", "expires": 365 * 24 * 60 * 60 * 1000});
};

Finder.detail = function() {
    this.list(Finder.getFileList());
};

Finder.resize = function(img) {
    ImageUtil.resize(img, 64, 64);
};

Finder.getParent = function(src) {
    var parent = src;

    while(parent != null && (parent.className == null || parent.className.indexOf("item") < 0)) {
        parent = parent.parentNode;
    };
    return parent;
};

Finder.click = function(event, name) {
    var e = (event || window.event);
    var src = (e.srcElement || e.target);

    if(src == null) {
        return;
    }

    var parent = Finder.getParent(src);

    if(parent == null) {
        Finder.each(function(e) {
            e.className = "item";
        });
        return;
    }

    Finder.select(src, (e.ctrlKey == true), (e.shiftKey == true));

    var bindEvent = "dblclick";

    if(src.nodeName.toLowerCase() == "a") {
        bindEvent = src.getAttribute("bind-event");
    }

    if(bindEvent != null && bindEvent != name) {
        return;
    }

    var file = Finder.getFile(src);
    var action = src.getAttribute("action");
    var target = src.getAttribute("target");
    var options = {};

    if(event.ctrlKey == true) {
        options.target = "_blank";
    }

    if(target != null) {
        options.target = target;
    }

    if(action == "finder-download") {
        Finder.download(file, options);
    }
    else if(action == "finder-delete") {
        options.callback = function(result) {
            if(result == null || result.status != 200) {
                DialogUtil.alert(I18N.getLang(result.message));
                return;
            }

            if(result.value > 0) {
                Finder.scroll(1);
                parent.parentNode.removeChild(parent);
            }
            else {
                DialogUtil.alert(I18N.getLang("finder.message.file.delete.failed"));
            }
        };
        Finder.remove([file], options);
    }
    else if(action == "finder-tail") {
        Finder.tail(file, options);
    }
    else if(action == "finder-less") {
        Finder.less(file, options);
    }
    else if(action == "finder-grep") {
        Finder.grep(file, options);
    }
    else {
        Finder.open(file, options);
    }
    return true;
};

Finder.edit = function(src) {
    if(src == null || src.nodeName.toLowerCase() != "a" || src.className != "file") {
        return;
    }

    var parent = Finder.getParent(src);

    if(parent.className.indexOf("selected") > -1) {
        var file = Finder.getFile(src);
        var fileName = file.fileName;
        var input = document.createElement("input");
        input.type = "text";
        input.value = fileName;
        input.style.cssText = "height: 14px; outline: none; font-size: 12px;";
        input.setAttribute("oldValue", fileName);

        src.innerHTML = "";
        src.appendChild(input);

        var handler = function(event) {
            var oldValue = input.getAttribute("oldValue");
            var newValue = StringUtil.trim(input.value);

            if(newValue.length > 0 && newValue != oldValue) {
                file.newName = newValue;
                Finder.rename(file, {"callback": function(result) {
                    if(result != null && result.status == 200 && result.value == true) {
                        src.removeChild(input);
                        src.innerHTML = newValue;
                        parent.setAttribute("fileName", newValue);
                    }
                    else {
                        src.removeChild(input);
                        src.innerHTML = fileName;
                        DialogUtil.alert(I18N.getLang("finder.message.file.rename.failed"));
                        return;
                    }
                }});
            }
            else {
                src.removeChild(input);
                src.innerHTML = fileName;
            }
        };

        input.onblur = handler;
        input.onkeydown = function(event) {
            var e = (event || window.event);

            if(e.keyCode == 13) {
                input.blur();
            }
        };
        input.select();
        return;
    }
};

Finder.cut = function() {
    var list = Finder.getSelected();

    Finder.each(function(e) {
        e.style.opacity = "1.0";
        e.style.filter = "alpha(opacity=100)";
    });

    if(list.length > 0) {
        var object = {};
        var fileList = [];
        object.host = Finder.getHost();
        object.workspace = Finder.getWorkspace();
        object.path = Finder.getPath();
        object.fileList = fileList;
        object.operate = "cut";

        for(var i = 0; i < list.length; i++) {
            list[i].style.opacity = "0.5";
            list[i].style.filter = "alpha(opacity=50)";

            var file = Finder.getFile(list[i]);
            fileList[fileList.length] = file.fileName;
        }
        FinderClipboard.set(JSON.stringify(object));
    }
};

Finder.copy = function() {
    var list = Finder.getSelected();

    Finder.each(function(e) {
        e.style.opacity = "1.0";
        e.style.filter = "alpha(opacity=100)";
    });

    if(list.length > 0) {
        var object = {};
        var fileList = [];
        object.host = Finder.getHost();
        object.workspace = Finder.getWorkspace();
        object.path = Finder.getPath();
        object.fileList = fileList;
        object.operate = "copy";

        for(var i = 0; i < list.length; i++) {
            var file = Finder.getFile(list[i]);
            fileList[fileList.length] = file.fileName;
        }
        FinderClipboard.set(JSON.stringify(object));
    }
};

Finder.getClipboardFiles = function(event) {
    var files = [];
    var clipboard = event.clipboardData;

    if(clipboard != null) {
        var items = clipboard.items;

        for(var i = 0, length = items.length; i < length; i++) {
            var item = items[i];

            if(item.kind == "file" || item.type.indexOf("image") > -1) {
                var file = item.getAsFile();
                file.name = "screenshot_" + new Date().getTime() + "." + item.type.replace("image/", "");
                files[files.length] = file;
            }
        }
    }
    return files;
};

Finder.paste = function() {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath();
    var object = FinderClipboard.getObject();

    if(object == null) {
        return;
    }

    if(host != object.host) {
        DialogUtil.alert(I18N.getLang("finder.message.file.cut.failed100"));
        return;
    }

    var url = null;
    var params = {};
    params.sourceHost = object.host;
    params.sourceWorkspace = object.workspace;
    params.sourcePath = object.path;
    params.file = object.fileList;

    if(object.operate == "cut") {
        url = Finder.getRequestURI() + "?action=finder.cut";
    }
    else if(object.operate == "copy") {
        url = Finder.getRequestURI() + "?action=finder.copy";
    }
    else {
        return;
    }

    url = url + "&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path);

    jQuery.ajax({
        "type": "post",
        "url": url,
        "data": jQuery.param(params, true),
        "dataType": "json",
        "error": function() {
            FinderClipboard.clear();
            DialogUtil.alert(I18N.getLang("finder.message.system.error"));
        },
        "success": function(response) {
            if(response.status != 200) {
                DialogUtil.alert(I18N.getLang(response.message));
                return;
            }

            Finder.reload();
            FinderClipboard.clear();
        }
    });
};

Finder.upload = function(files) {
    if(files.length < 1) {
        return;
    }

    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = Finder.getPath();
    var uploadUrl = Finder.getRequestURI() + "?action=finder.upload&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path);

    var index = 0;
    var uploadBytes = 0;
    var totalBytes = ByteUtil.getTotalSize(files);
    var startTime = new Date().getTime();
    var progressDialog = new ProgressDialog().open();
    var multipartUpload = new MultipartUpload();
    var partSize = ByteUtil.parse(Finder.getAttribute("upload-part-size", "5m"));

    var settings = {
        "url": uploadUrl,
        "name": "uploadFile",
        "file": files[index],
        "partSize": (partSize > 0 ? partSize : 5 * 1024 * 1024)
    };

    progressDialog.cancel = function() {
        var self = this;
        var confirmDialog = new ConfirmDialog({parent: this});

        confirmDialog.ensure = function() {
            self.close();
            self.destroy();
            multipartUpload.abort();
        };
        confirmDialog.open("您确定要取消上传吗？");
    };

    multipartUpload.progress = function(file, loaded, total) {
        var bytes = uploadBytes + loaded;
        var percent = Math.floor(bytes / totalBytes * 100);
        var size = ByteUtil.getByteSize(bytes) + "/" + ByteUtil.getByteSize(totalBytes);
        var seconds = (new Date().getTime() - startTime) / 1000;
        var speed = ByteUtil.getByteSize((seconds > 0 ? (bytes / seconds) : loaded));

        if(percent > 100) {
            percent = 100;
        }

        var html = [
            "<p>当前文件：", file.name, "</p>",
            "<p>上传进度：", size, "&nbsp;-&nbsp;", percent, "%</p>",
            "<p>平均速度：", speed, "/秒</p>",
            "<p>合计用时：", ByteUtil.getTimes(seconds), "</p>"
        ];
        progressDialog.update(percent + "%", html.join(""));
    };

    multipartUpload.success = function(file, result) {
        index++;

        setTimeout(function() {
            Finder.reload();
        }, 100);

        uploadBytes += file.size;

        if(index < files.length) {
            settings.file = files[index];
            multipartUpload.submit(settings);
        }
        else {
            progressDialog.close();
            progressDialog.destroy();
            Finder.uploadCount--;
        }
    };

    multipartUpload.cancel = function(xhr) {
        setTimeout(function() {
            Finder.reload();
        }, 1000);
        Finder.uploadCount--;
    };

    multipartUpload.error = function(xhr) {
        DialogUtil.alert(I18N.getLang("finder.message.file.upload.failed"));

        setTimeout(function() {
            Finder.reload();
        }, 1000);
        Finder.uploadCount--;
    };

    Finder.uploadCount++;
    multipartUpload.submit(settings);
};

Finder.select = function(src, multiple, shift) {
    if(multiple != true && shift != true) {
        this.each(function(e) {
            e.className = "item";
        });
    }

    var parent = Finder.getParent(src);

    if(parent != null) {
        if(this.active == null) {
            this.active = this.getFirst();
        }

        if(shift == true) {
            var j = -1;
            var k = -1;
            var list = [];
            var active = this.active;

            this.each(function(e) {
                list[list.length] = e;

                if(e == parent) {
                    j = list.length - 1;
                }

                if(e == active) {
                    k = list.length - 1;
                }
            });

            if(j > -1 && k > -1) {
                var min = Math.min(j, k);
                var max = Math.max(j, k);

                for(var i = 0; i < list.length; i++) {
                    if(i < min || i > max) {
                        list[i].className = "item";
                    }
                    else {
                        list[i].className = "item selected";
                    }
                }
            }
        }
        else {
            this.active = parent;
        }

        if(multiple == true) {
            if(parent.className.indexOf("selected") > -1) {
                parent.className = "item";
            }
            else {
                parent.className = "item selected";
            }
        }
        else {
            parent.className = "item selected";
        }

        if(this.active != null) {
            if(this.active.className.indexOf("selected") > -1) {
                this.active.className = "item active selected";
            }
            else {
                this.active.className = "item";
            }
        }
    }
};

Finder.keydown = function(event) {
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var keyCode = (event.keyCode || event.which);
    var path = StringUtil.trim(src.value);

    switch(keyCode) {
        case KeyCode.BACKSPACE: {
            if(e.ctrlKey == true) {
                var i = path.lastIndexOf("/", path.length - 2);

                if(i > -1) {
                    src.value = path.substring(0, i + 1);
                }
                else {
                    src.value = "/";
                }
                return false;
            }
            else {
                return true;
            }
        }
        case KeyCode.UP: {
            FinderSuggestDialog.scroll(-1);
            return false;
        }
        case KeyCode.DOWN: {
            FinderSuggestDialog.scroll(+1);
            return false;
        }
    }
    return true;
};

Finder.keyup = function(event) {
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var keyCode = (event.keyCode || event.which);
    var path = src.value;

    switch(keyCode) {
        case KeyCode.ESC: {
            Finder.back();
            return false;
        }
        case KeyCode.ENTER: {
            Finder.forward(Finder.getHost(), Finder.getWorkspace(), path);
            return false;
        }
        case KeyCode.UP: {
            EventUtil.stop(e);
            return false;
        }
        case KeyCode.DOWN: {
            EventUtil.stop(e);
            return false;
        }
    }
    Finder.suggest(Finder.getHost(), Finder.getWorkspace(), path);
    EventUtil.stop(e);
    return false;
};

Finder.showContextMenu = function(event) {
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var parent = this.getParent(src);

    if(parent != null && parent.className.indexOf("selected") < 0) {
        if(src.nodeName.toLowerCase() == "a") {
            Finder.select(src.parentNode);
        }
        else {
            Finder.select(src);
        }
    }

    var contextMenu = Finder.getContextMenu();

    if(parent != null) {
        contextMenu.setEnabled("open", true);
        contextMenu.setEnabled("download", true);
        contextMenu.setEnabled("cut", true);
        contextMenu.setEnabled("copy", true);
        contextMenu.setEnabled("remove", true);
        contextMenu.setEnabled("rename", true);
        contextMenu.setEnabled("info", true);
    }
    else {
        contextMenu.setEnabled("open", false);
        contextMenu.setEnabled("download", false);
        contextMenu.setEnabled("cut", false);
        contextMenu.setEnabled("copy", false);
        contextMenu.setEnabled("remove", false);
        contextMenu.setEnabled("rename", false);
        contextMenu.setEnabled("info", false);
    }

    var object = FinderClipboard.getObject();

    if(object != null) {
        contextMenu.setEnabled("paste", true);
    }
    else {
        contextMenu.setEnabled("paste", false);
    }
    contextMenu.open(e);
    return false;
};

Finder.getSuggest = function(workspace, path) {
    if(Finder.cache == null) {
        Finder.cache = {};
    }

    var i = path.lastIndexOf("/");
    var key = path.substring(0, i + 1);
    var prefix = path.substring(i + 1).toLowerCase();
    var fileList = Finder.cache[key];

    if(fileList != null) {
        var result = [];

        for(var i = 0; i < fileList.length; i++) {
            var file = fileList[i];

            if(StringUtil.startsWith(file.fileName.toLowerCase(), prefix)) {
                result[result.length] = file;
            }
        }
        return result;
    }
    return null;
};

Finder.suggest = function(host, workspace, path) {
    path = StringUtil.trim(path);
    path = StringUtil.replace(path, "\\", "/");
    path = StringUtil.replace(path, "//", "/");

    if(path.length < 1) {
        path = "/";
    }

    if(path.charAt(0) != "/") {
        path = "/" + path;
    }

    var json = Finder.getSuggest(workspace, path);

    if(json != null) {
        FinderSuggestDialog.open(json);
        return;
    }

    jQuery.ajax({
        "type": "get",
        "url": this.getRequestURI() + "?action=finder.suggest&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path),
        "dataType": "json",
        "success": function(response) {
            if(response != null && response.status == 200) {
                Finder.cache[path] = response.value;
                FinderSuggestDialog.open(response.value);
            }
        }
    });
};

Finder.getPropertyDialog = function() {
    if(this.propertyDialog == null) {
        this.propertyDialog = new PropertyDialog({"container": "finder-properties"});
    }
    return this.propertyDialog;
};

Finder.getContextMenu = function() {
    if(this.contextMenu == null) {
        this.contextMenu = new ContextMenu({"container": "finder-contextmenu"});
        this.contextMenu.context = {
            open: function(event) {
                var list = Finder.getSelected();

                if(list.length > 0) {
                    Finder.open(Finder.getFile(list[0]), {"target": "_blank"});
                }
                else {
                    DialogUtil.alert(I18N.getLang("finder.message.file.download.unselect"));
                }
            },
            upload: function(event) {
                var input = document.getElementById("_finder_file_input");

                if(input == null) {
                    input = document.createElement("input");
                    input.id = "_finder_file_input";
                    input.type = "file";
                    input.name = "uploadFile";
                    input.accept = "*";
                    input.multiple = "multiple";

                    var div = document.createElement("div");
                    div.style.display = "none";
                    div.appendChild(input);
                    document.body.appendChild(div);
                }

                input.value = "";
                jQuery(input).unbind();
                jQuery(input).change(function() {
                    var files = this.files;
                    var message = I18N.getLang("finder.message.file.upload.confirm1");

                    DialogUtil.confirm(message, function(ok) {
                        if(ok) {
                            Finder.upload(files);
                        }
                    });
                });
                input.click();
            },
            download: function(event) {
                var list = Finder.getSelected();

                if(list.length > 0) {
                    Finder.download(Finder.getFile(list[0]));
                }
                else {
                    DialogUtil.alert(I18N.getLang("finder.message.file.download.unselect"));
                }
            },
            cut: function(event) {
                Finder.cut();
            },
            copy: function(event) {
                Finder.copy();
            },
            paste: function(event) {
                Finder.paste(event);
            },
            remove: function(event) {
                var files = [];
                var list = Finder.getSelected();

                for(var i = 0; i < list.length; i++) {
                    files[files.length] = Finder.getFile(list[i]);
                }

                Finder.remove(files, {"callback": function(response) {
                    if(response == null || response.status != 200) {
                        DialogUtil.alert(I18N.getLang("finder.message.file.notexists"));
                        return;
                    }

                    if(response.value >= files.length) {
                        for(var i = 0; i < list.length; i++) {
                            list[i].parentNode.removeChild(list[i]);
                        }
                    }
                    else {
                        DialogUtil.alert(I18N.getLang("finder.message.file.delete.failed"));
                    }
                }});
                return false;
            },
            rename: function(event) {
                var list = Finder.getSelected();

                if(list.length > 0) {
                    var parent = Finder.getParent(list[0]);
                    Finder.edit(jQuery(parent).find("a.file").get(0));
                }
            },
            mkdir: function(event) {
                Finder.mkdir({"callback": function(result) {
                    if(result == null || result.status != 200 || result.value != true) {
                        DialogUtil.alert(I18N.getLang("finder.message.file.mkdir.failed"));
                        return;
                    }
                    Finder.reload();
                }});
            },
            refresh: function(event) {
                window.location.reload();
            },
            viewsource: function(event) {
                window.open("view-source:" + window.location.href, "_blank");
            },
            help: function(event) {
                window.open(Finder.getRequestURI() + "?action=finder.help", "_blank");
            },
            info: function(event) {
                var list = Finder.getSelected();

                if(list.length > 0) {
                    var file = Finder.getFile(list[0]);
                    Finder.getPropertyDialog().display(file);
                }
            }
        };

        var items = [
            {"key": "O",  "command": "open"},
            {"key": "F",  "command": "upload"},
            {"key": "G",  "command": "download"},
            {"key": "X",  "command": "cut"},
            {"key": "C",  "command": "download"},
            {"key": "V",  "command": "paste"},
            {"key": "D",  "command": "remove"},
            {"key": "F2", "command": "rename"},
            {"key": "N",  "command": "mdkir"},
            {"key": "E",  "command": "refresh"},
            {"key": "U",  "command": "viewsource"},
            {"key": "H",  "command": "help"},
            {"key": "R",  "command": "info"}
        ];

        for(var i = 0; i < items.length; i++) {
            var item = items[i];
            this.contextMenu.addShortcut(item.key, item.command);
        }
    }
    return this.contextMenu;
};

Finder.getPlugin = function() {
    if(this.plugin == null) {
        var plugin = {};

        plugin.getHome = function() {
            if(this.home == null) {
                var home = null;
                var scripts = document.getElementsByTagName("script");

                for(var i = 0, length = scripts.length; i < length; i++) {
                    var src = scripts[i].src;

                    if(src != null && src != undefined && src.length > 0) {
                        var k = src.indexOf("/finder/plugins/");
                        if(k > -1) {
                            home = src.substring(0, k + "/finder/plugins/".length);
                            break;
                        }
                    }
                }

                if(home == null) {
                    this.home = "";
                }
                else {
                    home = StringUtil.trim(home);

                    if(home.length >= 1) {
                        if(home.substring(home.length - 1) == "/") {
                            home = home.substring(0, home.length - 1);
                        }
                    }
                    this.home = home;
                }
            }
            return this.home;
        };

        plugin.loadCss = function(url) {
            var e = document.createElement("link");
            e.rel = "stylesheet";
            e.type = "text/css";
            e.href = url;
            document.body.appendChild(e);
        };

        plugin.loadScript = function(url) {
            var e = document.createElement("script");
            e.type = "text/javascript";
            e.src = url;
            document.body.appendChild(e);
        };
        this.plugin = plugin;
    }
    return this.plugin;
};

/*
 * $RCSfile: ProgressDialog.js,v $$
 * $Revision: 1.1 $
 * $Date: 2012-10-18 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var PropertyDialog = com.skin.framework.Class.create(Dialog, null);

PropertyDialog.prototype.create = function() {
    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    container.className = "dialog props-dialog";

    parent.find("div.button button.ensure").click(function() {
        var input = jQuery("#finder-properties input[name=fileName]");
        var newName = input.val();
        var oldName = input.attr("oldValue");

        if(newName == oldName) {
            jQuery("#finder-properties").hide();
            return;
        }

        var item = Finder.getItem(oldName);
        var file = Finder.getFile(item);

        if(item == null || file == null) {
            return;
        }

        file.newName = newName;
        Finder.rename(file, {"callback": function(result) {
            if(result != null && result.status == 200 && result.value > 0) {
                jQuery(item).attr("fileName", newName);
                jQuery(item).find("a.file").html(newName);
            }
            else {
                DialogUtil.alert(I18N.getLang("finder.message.file.rename.failed"));
                return;
            }
            jQuery("#finder-properties").hide();
        }});
    });

    parent.find("div.button button.cancel").click(function() {
        self.close();
    });

    parent.find("div.title span.close").click(function() {
        self.close();
    });

    this.addShortcut("ESC", function() {
        self.close();
    });
    Dragable.registe(parent.find("div.title").get(0), container);
};

PropertyDialog.prototype.display = function(file) {
    var icon = null;
    var type = null;
    var size = null;
    var panel = jQuery(this.getContainer());

    if(file.isFile != true) {
        type = "文件夹";
        icon = Finder.getRequestURI() + "?action=res&path=/finder/images/folder.gif";
    }
    else {
        type = "文件";
        icon = Finder.getRequestURI() + "?action=res&path=/finder/type/" + file.fileIcon + ".gif";
    }

    if(!isNaN(file.fileSize)) {
        size = ByteUtil.getByteSize(file.fileSize) + " (" + file.fileSize + " 字节)";
    }
    else {
        size = "未知";
    }

    panel.find(".title h4").html(file.fileName + " 属性");
    panel.find("div.cp-file-name img.file-icon").attr("src", icon);
    panel.find("div.cp-file-name input.file-name").attr("oldValue", file.fileName);
    panel.find("div.cp-file-name input.file-name").val(file.fileName);
    panel.find("div.cp-file-type span.field").html(type);
    panel.find("div.cp-file-path span.field").html(Finder.getPath() + "/" + file.fileName);
    panel.find("div.cp-file-size span.field").html(size);
    panel.find("div.cp-file-modified span.field").html(Finder.format(file.lastModified));
    this.open();
};

/*
 * $RCSfile: ProgressDialog.js,v $$
 * $Revision: 1.1 $
 * $Date: 2012-10-18 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var ProgressDialog = com.skin.framework.Class.create(Dialog, null);

ProgressDialog.prototype.create = function() {
    var self = this;
    var buffer = [];
    buffer[buffer.length] = "<div id=\"" + this.id + "_title_bar\" class=\"title\" onselectstart=\"return false;\">";
    buffer[buffer.length] = "    <h4>上传文件（文件上传过程中请勿刷新页面）</h4>";
    buffer[buffer.length] = "    <span id=\"" + this.id + "_close\" class=\"close\" dragable=\"false\"></span>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"body\">";
    buffer[buffer.length] = "    <div style=\"padding: 20px;\">";
    buffer[buffer.length] = "        <div class=\"progress\">";
    buffer[buffer.length] = "            <div class=\"bar\"><div id=\"" + this.id + "_percent\" class=\"percent\" style=\"width: 0%\"></div></div>";
    buffer[buffer.length] = "            <div id=\"" + this.id + "_text\" class=\"text\"><p>上传：0.0M/10M&nbsp;-&nbsp;0%</p><p>速度：0M/秒</p><p>用时：00:00</p></div>";
    buffer[buffer.length] =  "        </div>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "    <div class=\"button right\">";
    buffer[buffer.length] = "        <button id=\"" + this.id + "_cancel\" type=\"button\" class=\"button cancel\" href=\"javascript:void(0)\">取 消</button>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "</div>";
    this.setContent(buffer.join(""));

    var container = this.getContainer();
    var parent = jQuery(container);
    parent.css("width", "480px");

    parent.find("div.button button.cancel").click(function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    parent.find("div.title span.close").click(function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    this.addShortcut("ESC", function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    this.addShortcut("ESC", function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });
    Dragable.registe(parent.find("div.title").get(0), container);
};

ProgressDialog.prototype.update = function(percent, message) {
    var e = this.getContainer();

    if(percent != null && message != null) {
        var e1 = document.getElementById(this.id + "_percent");
        var e2 = document.getElementById(this.id + "_text");

        if(e1 != null) {
            e1.style.width = percent;
        }

        if(e2 != null) {
            e2.innerHTML = message;
        }
    }
};

var FinderSuggestDialog = {"id": "finder-suggest", "handler": null};

/**
 * @Override
 */
FinderSuggestDialog.handler = function(value, action) {
    var e = document.getElementById("address");

    if(e == null) {
        return;
    }

    var path = StringUtil.trim(e.value);
    path = StringUtil.replace(path, "\\", "/");
    path = StringUtil.replace(path, "//", "/");

    var k = path.lastIndexOf("/");

    if(k > -1) {
        path = path.substring(0, k);
    }

    if(path.length < 1 || path == "/") {
        e.value = "/" + value;
    }
    else {
        if(StringUtil.endsWith(path, "/")) {
            e.value = path + value;
        }
        else {
            e.value = path + "/" + value;
        }
    }

    if(action == true) {
        Finder.forward(Finder.getHost(), Finder.getWorkspace(), e.value);
    }
};

FinderSuggestDialog.click = function(src) {
    var list = this.list();
    var length = list.length;

    for(var i = 0; i < length; i++) {
        if(list[i].className == "selected") {
            list[i].className = "";
        }
    }

    src.className = "selected";

    if(this.handler != null) {
        this.handler.apply(null, [src.getAttribute("option-value"), true]);
    }
};

FinderSuggestDialog.open = function(fileList) {
    var e = document.getElementById(this.id);

    if(e == null) {
        return;
    }

    var b = [];
    b[b.length] = "<ul>";
    b[b.length] = "<li option-value=\"\" index=\"0\" onclick=\"FinderSuggestDialog.click(this)\">";
    b[b.length] = "   <span class=\"icon\"><img src=\"" + Finder.getRequestURI() + "?action=res&path=/finder/images/folder.gif\"/></span>";
    b[b.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\">...</a></span>";
    b[b.length] = "   <span class=\"fileSize\">&nbsp;</span>";
    b[b.length] = "</li>";

    if(fileList != null && fileList.length != null && fileList.length > 0) {
        for(var i = 0; i < fileList.length; i++) {
            var file = fileList[i];

            if(file.isFile != true) {
                b[b.length] = "<li option-value=\"" + HtmlUtil.encode(file.fileName) + "\" index=\"0\" onclick=\"FinderSuggestDialog.click(this)\">";
                b[b.length] = "   <span class=\"icon\"><img src=\"" + Finder.getRequestURI() + "?action=res&path=/finder/images/folder.gif\"/></span>";
                b[b.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\">" + HtmlUtil.encode(file.fileName) + "</a></span>";
                b[b.length] = "   <span class=\"fileSize\">&nbsp;</span>";
                b[b.length] = "</li>";
            }
        }

        for(var i = 0; i < fileList.length; i++) {
            var file = fileList[i];

            if(file.isFile == true) {
                b[b.length] = "<li option-value=\"" + HtmlUtil.encode(file.fileName) + "\" index=\"0\" onclick=\"FinderSuggestDialog.click(this)\">";
                b[b.length] = "   <span class=\"icon\"><img style=\"\" src=\"" + Finder.getRequestURI() + "?action=res&path=/finder/icon/" + file.fileIcon + ".png\"/></span>";
                b[b.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\">" + HtmlUtil.encode(file.fileName) + "</a></span>";
                b[b.length] = "   <span class=\"fileSize\">" + ByteUtil.getByteSize(file.fileSize) + "</span>";
                b[b.length] = "</li>";
            }
        }
    }

    b[b.length] = "</ul></div>";
    e.innerHTML = b.join("");

    if(fileList.length < 1) {
        e.style.height = "20px";
    }
    else if(fileList.length < 20) {
        e.style.height = ((fileList.length + 1) * 20) + "px";
    }
    else {
        e.style.height = "480px";
    }

    e.setAttribute("status", "0");
    e.style.display = "block";
};

FinderSuggestDialog.close = function() {
    var e = document.getElementById(this.id);

    if(e != null && e.getAttribute("status") != "1") {
        e.style.display = "none";
    }
};

FinderSuggestDialog.list = function() {
    var list = [];
    var e = document.getElementById(this.id);

    if(e != null) {
        var elements = e.getElementsByTagName("ul");

        if(elements != null && elements.length > 0) {
            var temp = elements[0].childNodes;

            for(var i = 0, length = temp.length; i < length; i++) {
                if(temp[i].nodeType == 1 && temp[i].nodeName.toLowerCase() == "li") {
                    if(temp[i].className != "empty") {
                        list[list.length] = temp[i];
                    }
                }
            }
        }
    }
    return list;
};

FinderSuggestDialog.scroll = function(offset) {
    var selected = -1;
    var list = this.list();
    var length = list.length;

    if(length < 1) {
        return;
    }

    for(var i = 0; i < length; i++) {
        if(list[i].className == "selected") {
            selected = i;
            list[i].className = "";
        }
    }

    selected = selected + offset;

    if(selected < 0) {
        selected = list.length - 1;
    }

    if(selected >= list.length) {
        selected = 0;
    }

    if(selected < length) {
        var e = list[selected];
        var p = document.getElementById(this.id);
        var value = e.getAttribute("option-value");
        e.className = "selected";
        p.scrollTop = e.offsetTop;

        if(value != null && value.length > 0) {
            if(this.handler != null) {
                this.handler(value);
            }
        }
    }
};

var FinderClipboard = {};

FinderClipboard.set = function(value) {
    if(encodeURIComponent(value).length > 20 * 1024 * 1024) {
        throw {"name": "LargeDataException", "message": "The data is too large."};
    }
    Finder.setLocalVariable("finder_clipboard", value);
};

FinderClipboard.get = function() {
    return Finder.getLocalVariable("finder_clipboard");
};

FinderClipboard.getObject = function() {
    var value = Finder.getLocalVariable("finder_clipboard");

    if(value != null && value.length > 0) {
        try {
            return window.eval("(" + value + ")");
        }
        catch(e) {
        }
    }
    return null;
};

FinderClipboard.clear = function() {
    FinderClipboard.set("");
};

/*
 * $RCSfile: ImageUtil.js,v $$
 * $Revision: 1.1 $
 * $Date: 2012-10-18 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var ImageUtil = {};

ImageUtil.resize = function(img, maxWidth, maxHeight) {
    if(img.readyState != "complete") {
    }

    if(img.offsetWidth > maxWidth || img.offsetHeight > maxHeight) {
        var scaleWidth = 0;
        var scaleHeight = 0;
        var ratio = img.offsetWidth / img.offsetHeight;

        if(img.offsetWidth > maxWidth) {
            scaleWidth = maxWidth;
            scaleHeight = Math.floor(maxWidth / ratio);

            if(scaleHeight > maxHeight) {
                scaleHeight = maxHeight;
                scaleWidth = Math.floor(maxHeight * ratio);
            }
        }
        else {
            scaleHeight = maxHeight;
            scaleWidth = Math.floor(maxHeight * ratio);

            if(scaleWidth > maxWidth) {
                scaleWidth = maxWidth;
                scaleHeight = Math.floor(maxWidth / ratio);
            }
        }
        img.style.width = scaleWidth + "px";
        img.style.height = scaleHeight + "px";
    }
    img.style.opacity = 100;
};

/*
 * $RCSfile: ImageViewer.js,v $$
 * $Revision: 1.1 $
 * $Date: 2012-10-18 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var ImageViewer = {};

ImageViewer.getImageDialog = function() {
    if(this.imageDialog == null) {
        var ImageDialog = com.skin.framework.Class.create(Dialog);

        ImageDialog.prototype.create = function() {
            var self = this;
            var container = this.getContainer();
            var panel = jQuery(container);

            panel.find("div.title span.close").click(function(){
                self.close();
            });

            this.addShortcut("LEFT | UP", function(event) {
                ImageViewer.prev();
                return false;
            });

            this.addShortcut("RIGHT | DOWN | BLANKSPACE", function(event) {
                ImageViewer.next();
                return false;
            });

            this.addShortcut("ESC", function(event) {
                self.close();
            });
            Dragable.registe(panel.find("div.title").get(0), container);
        };
        this.imageDialog = new ImageDialog({"container": "finder-imageviewer"});
    }
    return this.imageDialog;
};

ImageViewer.open = function(urls, index) {
    this.urls = urls;
    this.index = index;

    if(this.urls == null || this.urls.length < 1) {
        this.close();
        return;
    }

    if(isNaN(this.index)) {
        this.index = 0;
    }

    this.getImageDialog().open();
    ImageViewer.show();
};

ImageViewer.show = function() {
    if(this.urls == null || this.urls.length < 1) {
        this.close();
        return;
    }

    if(isNaN(this.index) || this.index >= this.urls.length) {
        this.index = 0;
    }

    if(this.index < 0) {
        this.index = this.urls.length - 1;
    }

    var url = this.urls[this.index];
    var path = decodeURIComponent(url.substring(url.indexOf("path=")));
    var fileName = FileType.getName(path);

    jQuery("#finder-imageviewer div.title h4").html(fileName);
    jQuery("#finder_imgviewer_img").css({"width": "auto", "height": "auto", "opacity": 0});
    jQuery("#finder_imgviewer_img").attr("src", url);
    jQuery("#finder_imgviewer_url").attr("href", url);
};

ImageViewer.prev = function() {
    this.index--;
    this.show();
};

ImageViewer.next = function() {
    this.index++;
    this.show();
};

ImageViewer.close = function() {
    this.index = -1
    this.urls = null;
    this.getImageDialog().close();
};

var Util = {};
Util.open = function(url, name, width, height, features){
    var w = width;
    var h = height;
    if(w == null) w = window.screen.availWidth;
    if(h == null) h = window.screen.availHeight;

    var x = Math.floor((screen.availWidth  - w) / 2);
    var y = Math.floor((screen.availHeight - h - 60) / 2);

    if(x < 0) {
        x = 0;
    }

    if(y < 0) {
        y = 0;
    }

    if(features == null || features == "") {
        features = "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h;
    }
    else {
        features = "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + "," + features;
    }
    return window.open(url, name, features);
};

/* =========================================================
 * init
 * =========================================================
 */
jQuery(function() {
    var path = document.body.getAttribute("path");

    if(path == null || path.length <= 1) {
        jQuery("#back").addClass("disabled");
        jQuery("#back span").attr("class", "back-disabled");
    }

    jQuery("#uiTypeOption").change(function() {
        var host = Finder.getHost();
        var workspace = Finder.getWorkspace();
        var path = StringUtil.trim(jQuery("body").attr("path"));
        var type = StringUtil.trim(jQuery("#uiTypeOption").val());
        var theme = StringUtil.trim(jQuery("#uiThemeOption").val());
        var encoding = StringUtil.trim(jQuery("#uiEncodingOption").val());
        window.location.href = Finder.getRequestURI() + "?action=finder.display&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path) + "&type=" + encodeURIComponent(type) + "&theme=" + encodeURIComponent(theme) + "&encoding=" + encodeURIComponent(encoding);
    });

    jQuery("#uiThemeOption").change(function() {
        jQuery("#uiTypeOption").change();
    });

    jQuery("#uiEncodingOption").change(function() {
        jQuery("#uiTypeOption").change();
    });
});

/**
 * finder
 */
jQuery(function() {
    jQuery("ul.file-list li a.file").each(function() {
        var work = Finder.getWork();
        var path = Finder.getPath();
        var file = Finder.getFile(this);
        var tooltip = Finder.getTooltip(work, path, file);
        jQuery(this).attr("title", tooltip.replace(/&#10;/g, "\r\n"));
    });

    jQuery("a.button span.back").click(function(event) {
        Finder.back();
    });

    jQuery("a.button span.refresh").click(function(event) {
        if(event.ctrlKey == true) {
            window.open(window.location.href, "_blank");
        }
        else {
            window.location.reload();
        }
    });

    jQuery("a.button span.view").click(function(event) {
        jQuery("#view-options").show();
        return false;
    });

    jQuery("a.home").attr("href", Finder.getRequestURI() + "?action=finder.index&workspace=" + encodeURIComponent(Finder.getWorkspace()));
    jQuery("a.help").attr("href", Finder.getRequestURI() + "?action=finder.help");
    jQuery("a.setting").click(function(event) {
        Util.open(Finder.getRequestURI() + "?action=finder.config", "_blank", 800, 600);
    });

    jQuery("#address").keydown(function(event) {
        return Finder.keydown(event);
    });

    jQuery("#address").keyup(function(event) {
        return Finder.keyup(event);
    });

    jQuery("#address").click(function(event) {
        Finder.keyup(event);
        return true;
    });

    jQuery(document).click(function(event) {
        FinderSuggestDialog.close();
        jQuery("#view-options").hide();
        return true;
    });

    jQuery("#view-options li").click(function() {
        jQuery("#view-options li").attr("class", "");
        jQuery(this).attr("class", "selected");

        if(jQuery(this).attr("option-value") == "detail") {
            Finder.detail();
        }
        else {
            Finder.outline();
        }
    });
});

jQuery(function() {
    var viewMode = Finder.getViewMode();

    if(viewMode == "outline") {
        jQuery("#view-options li[option-value=outline]").click();
    }
    else {
        jQuery("#file-view").show();

        DispatchAction.dispatch("orderby_([^_]+)_([^_]+)", function(field, asc) {
            if(field == "file-name" && asc == "asc") {
                return;
            }

            var src = jQuery("#fileList tr.head td.orderable[orderBy=" + field + "]");

            if(asc == "asc") {
                src.find("span.order").attr("class", "order desc");
            }
            else {
                src.find("span.order").attr("class", "order asc");
            }
            src.click();
        });
    }

    DispatchAction.execute(window.location.hash);
    var leftFrame = window.top.leftFrame;

    if(leftFrame != null && leftFrame.expand != null) {
        leftFrame.expand("/" + Finder.getHost() + "/" + Finder.getWorkspace() + Finder.getPath());
    }
});

/**
 * sort
 */
jQuery(function() {
    jQuery("#file-view span.orderable").click(function() {
        var src = jQuery(this).find("em.order");

        if(src.size() < 1) {
            return;
        }

        var asc = "asc";
        var orderBy = jQuery(this).attr("orderBy");
        var className = src.attr("class");

        jQuery("#file-view span.orderable em.order").attr("class", "order");

        if(className.indexOf("asc") > -1) {
            asc = "desc";
            src.attr("class", "order desc");
        }
        else {
            src.attr("class", "order asc");
        }

        var fileList = Finder.getFileList();

        if(orderBy == "file-name") {
            FileSort.byName(fileList);
        }
        else if(orderBy == "file-type") {
            FileSort.byType(fileList);
        }
        else if(orderBy == "file-size") {
            FileSort.bySize(fileList);
        }
        else if(orderBy == "last-modified") {
            FileSort.byLastModified(fileList);
        }

        if(asc == "desc") {
            fileList.reverse();
        }

        Finder.list(fileList, true);
        window.location.href = "#orderby_" + orderBy + "_" + asc;
    });
});

/**
 * ImageViewer
 */
jQuery(function() {
    jQuery("#finder_imgviewer_img").load(function() {
        ImageUtil.resize(this, 600, 440);
    });

    jQuery("#finder-imageviewer div.pagebar a.prev").click(function(event) {
        ImageViewer.prev();
    });

    jQuery("#finder-imageviewer div.pagebar a.next").click(function(event) {
        ImageViewer.next();
    });
});

/**
 * short cut & contextmenu
 */
jQuery(function() {
    if(document.body.getAttribute("page") == "display") {
        return;
    }

    /*
     * $RCSfile: FileListFrame.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FileListFrame = com.skin.framework.Class.create(Dialog, null);

    FileListFrame.prototype.create = function() {
    };

    FileListFrame.prototype.setActiveStyle = function() {
    };

    FileListFrame.prototype.close = function() {
    };

    FileListFrame.prototype.destroy = function() {
    };

    /**
     * root
     */
    var root = new FileListFrame({"container": "file-view"});
    var listener = root.getListener();

    listener.click = function(event) {
        Finder.click(event);
    };

    listener.dblclick = function(event) {
        Finder.click(event, "dblclick");
    };

    /**
     * root.addShortcut("CTRL + V", function() { cookie.file });
     */
    listener.paste = function(event) {
        var files = Finder.getClipboardFiles(event);

        if(files != null && files.length > 0) {
            Finder.upload(files);
        }
    };

    listener.contextmenu = function(event) {
        var src = (event.srcElement || event.target);
        var nodeName = src.nodeName.toLowerCase();

        if(nodeName == "input" || nodeName == "select" || nodeName == "textarea" || nodeName == "button") {
            Finder.getContextMenu().close();
            return true;
        }

        if(event.ctrlKey == true) {
            return true;
        }

        if(jQuery(src).closest("div[contextmenu=false]").size() > 0) {
            Finder.getContextMenu().close();
            return true;
        }

        if(jQuery(src).closest("div.contextmenu").size() > 0) {
            return false;
        }
        return Finder.showContextMenu(event);
    };

    root.addShortcut("F5", function(event) {
        window.location.reload();
        return false;
    });

    root.addShortcut("ESC | Q", function(event) {
        if(window.parent == window) {
            window.close();
        }
        return false;
    });

    root.addShortcut("BACKSPACE", function(event) {
        Finder.back();
        return false;
    });

    root.addShortcut("LEFT | UP", function(event) {
        Finder.scroll(-1, (event.shiftKey == true));
        return false;
    });

    root.addShortcut("RIGHT | DOWN", function(event) {
        Finder.scroll(+1, (event.shiftKey == true));
        return false;
    });

    root.addShortcut("ENTER", function(event) {
        Finder.getContextMenu().execute("open", event);
        return false;
    });

    root.addShortcut("DELETE", function(event) {
        Finder.getContextMenu().execute("remove", event);
        return false;
    });

    root.addShortcut("SHIFT + F", function(event) {
        Finder.getContextMenu().execute("upload");
        return false;
    });

    root.addShortcut("SHIFT + G", function(event) {
        Finder.getContextMenu().execute("download");
        return false;
    });

    root.addShortcut("SHIFT + N", function(event) {
        Finder.getContextMenu().execute("mkdir");
        return false;
    });

    root.addShortcut("F2", function(event) {
        Finder.getContextMenu().execute("rename");
        return false;
    });

    /**
     * The default shortcut key for the operating system is still returned to true
     */
    root.addShortcut("CTRL + A", function(event) {
        Finder.each(function(e) {
            e.className = "item selected";
        });
        return false;
    });

    /**
     * The default shortcut key for the operating system is still returned to true
     */
    root.addShortcut("CTRL + X", function(event) {
        Finder.getContextMenu().execute("cut");
        return true;
    });

    /**
     * The default shortcut key for the operating system is still returned to true
     */
    root.addShortcut("CTRL + C", function(event) {
        Finder.getContextMenu().execute("copy");
        return true;
    });

    /**
     * The default shortcut key for the operating system is still returned to true
     */
    root.addShortcut("CTRL + V", function(event) {
        Finder.getContextMenu().execute("paste", event);
        return true;
    });

    root.addShortcut("SHIFT + U", function(event) {
        Finder.getContextMenu().execute("viewsource");
        return false;
    });

    root.addShortcut("SHIFT + R", function(event) {
        Finder.getContextMenu().execute("info");
        return false;
    });
    DialogManager.setActive(root, true);

    /**
     * Reload the file every 10 seconds
     */
    var handler = function() {
        // Finder.reload();

        /**
         * Reload the file
         */
        setTimeout(handler, 10000);
    };
    setTimeout(handler, 10000);
});

/**
 * Register file open mode
 */
jQuery(function() {
    FileType.registe("ico, jpg, jpeg, gif, bmp, png", function(file, options) {
        if(options.download != null) {
            return false;
        }

        var index = 0;
        var urls = [];
        var host = Finder.getHost();
        var workspace = Finder.getWorkspace();
        var path = Finder.getPath();
        var fileList = Finder.getFileList();
        var prefix = Finder.getRequestURI() + "?action=finder.display&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace);

        for(var i = 0; i < fileList.length; i++) {
            var fileName = fileList[i].fileName;
            var fileType = FileType.getType(fileName);

            if(FileType.contains("ico, jpg, jpeg, gif, bmp, png", fileType)) {
                if(fileName == file.fileName) {
                    index = urls.length;
                }
                urls[urls.length] = prefix + "&path=" + encodeURIComponent(path + "/" + fileName);
            }
        }
        ImageViewer.open(urls, index);
        return true;
    });
});

/**
 * Register context menu click event
 */
jQuery(function() {
    jQuery("#finder-contextmenu").bind("selectstart", function() {
        return false;
    });
});

/**
 * File upload support
 */
jQuery(function() {
    /**
     * dragleave dragenter dragover
     */
    EventUtil.addEventListener(document, "dragover", function(event) {
        event.preventDefault();
    });

    /**
     * JQuery's drop event has bug and cannot get dataTransfer
     * See: https://bugs.jquery.com/ticket/10756
     */
    EventUtil.addEventListener(document, "drop", function(event) {
        var dataTransfer = event.dataTransfer;

        if(dataTransfer == null || dataTransfer == undefined || dataTransfer.files == null || dataTransfer.files.length < 1) {
            return;
        }

        var files = dataTransfer.files;

        DialogUtil.confirm(I18N.getLang("finder.message.file.upload.confirm1"), function(ok) {
            if(ok) {
                Finder.upload(files);
            }
        });
        EventUtil.stop(event);
    });

    jQuery(window).bind("beforeunload", function(event) {
        if(Finder.uploadCount > 0) {
            return I18N.getLang("finder.message.file.upload.confirm2");
        }
        /**
         * Do not return any value other than undefined: null, true, false
         * return undefined;
         */
    });
});

/**
 * 拦截当前页面的全部事件, 并将控制权转交到widget
 * widget负责管理所有的窗口并负责将页面的事件转发给当前活动窗口
 * widget完成的功能有：
 * 1. 窗口管理, zIndex分配, 活动窗口管理
 * 2. 事件转发, 将事件转发给当前活动的窗口
 */
jQuery(function() {
    /**
     * The paste event of the jQuery-1.7.2 gets less than clipboardData
     * jQuery-1.7.2版本的paste事件获取不到clipboardData
     * The event is as much as possible before the jQuery event
     * bug: IE11 does not trigger the paste event
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

jQuery(function() {
    if(document.body.getAttribute("page") == "display") {
        return;
    }

    /**
     * file-veiw实际上是顶级窗口
     * 只有把顶级窗口设置为覆盖整个页面, 才可能使页面都响应顶级窗口的事件
     * 顶级窗口不能使用document或者document.body，这样将会使窗口出现嵌套。
     */
    jQuery(window).bind("resize", function() {
        var src = jQuery("#file-view");
        var position = src.position();
        var height = (document.documentElement.scrollTop + document.documentElement.clientHeight);
        src.css("height", (height - position.top) + "px");
    });
    jQuery(window).trigger("resize");
});

/**
 * 父页面初始化
 */
jQuery(function() {
    if(window == window.parent) {
        return;
    }

    if(window.parent.__finder_loaded__ == true) {
        return;
    }

    var win = window.parent;
    var doc = win.document;
    var scripts = doc.getElementsByTagName("script");
    var loaded = false;

    /**
     * 父页面可能自己在页面上手动加载了相关资源
     */
    for(var i = 0, length = scripts.length; i < length; i++) {
        var src = scripts[i].src;

        if(src != null && src != undefined && src.length > 0) {
            var k = src.indexOf("/finder/widget.js");

            if(k > -1) {
                loaded = true;
                break;
            }
        }
    }

    /**
     * 如果父页面自己手动加载了相关资源则标记变量为null
     * 所以无论何种情况都重设为true
     */
    win.__finder_loaded__ = true;

    if(loaded == false) {
        ResourceLoader.script(doc, Finder.getRequestURI() + "?action=res&path=/finder/widget.js");
        ResourceLoader.script(doc, Finder.getRequestURI() + "?action=res&path=/finder/fileupload.js");
    }
});

/**
 * 加载插件
 */
jQuery(function() {
    var plugins = Finder.plugins;

    if(plugins != null && plugins.length > 0) {
        for(var i = 0; i < plugins.length; i++) {
            var name = plugins[i];
            var e = document.createElement("script");
            e.type = "text/javascript";
            e.src = Finder.getRequestURI() + "?action=res&path=/finder/plugins/" + name + "/" + name + ".js";
            document.body.appendChild(e);
        }
    }
});

/**
 * 加载文件
 */
jQuery(function() {
    DialogUtil.messageTitle = I18N.getLang("finder.dialog.message.title");
    DialogUtil.confirmTitle = I18N.getLang("finder.dialog.confirm.title");
    DialogUtil.ensureButtonText = I18N.getLang("finder.dialog.confirm.ensure");
    DialogUtil.cancelButtonText = I18N.getLang("finder.dialog.confirm.cancel");

    Finder.reload(function() {
        jQuery("#loading").hide().html("");
    });
});
