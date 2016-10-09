(function(scope) {
    /*
     * $RCSfile: Class.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Class = {};

    Class.getClassId = function(){
        if(this.id == null) {
            this.id = 1;
        }
        return "_class_" + (this.id++);
    },

    /**
     * create a class
     * @param parent
     * @param constructor
     * @return Object
     */
    Class.create = function(parent, constructor, fields){
        var clazz = null;

        if(parent != null) {
            if(constructor != null) {
                clazz = function(){/* Class.create */ parent.apply(this, arguments); constructor.apply(this, arguments);};
            }
            else {
                clazz = function(){/* Class.create */ parent.apply(this, arguments);};
            }

            for(var property in parent.prototype) {
                clazz.prototype[property] = parent.prototype[property];
            }

            clazz.prototype["toString"] = parent.prototype["toString"];
            clazz.$super = parent.prototype;
        }
        else {
            if(constructor != null) {
                clazz = function(){/* Class.create */ constructor.apply(this, arguments);};
            }
            else {
                clazz = function(){/* Class.create */};
            }
            clazz.$super = {};
        }

        clazz.parent = parent;
        clazz.classId = this.getClassId();
        clazz.prototype.constructor = clazz;

        if(fields != null && fields.length > 0) {
            this.let(clazz, fields);
        }
        return clazz;
    };

    /**
     * @param instance
     * @param prototype
     * @retur Object
     */
    Class.$super = /* private */ function(instance, prototype) {
        var object = {};

        for(var i in prototype) {
            if(typeof(prototype[i]) == "function") {
                object[i] = function(){prototype[i].apply(instance, arguments);};
            }
        }
        return object;
    };

    /**
     * simple singleton
     * var myduck = Class.getInstance(Animal, function(){this.swim = function(){};});
     * myduck.swim();
     * @param parent
     * @param constructor
     * @return Object
     */
    Class.getInstance = function(parent, constructor){
        return new (Class.create(parent, constructor))();
    };

    /**
     * extend properties
     * @param child
     * @param parent
     * @return Object
     */
    Class.extend = function(child, parent) {
        if(child == null) {
            child = {};
        }

        for(var name in parent) {
            child[name] = parent[name];
        }
        return child;
    };

    /**
     * @param f
     * @param fields
     * @return Object
     */
    Class.let = function(f, fields){
        var p = f.prototype;

        for(var i = 0, length = fields.length; i < length; i++) {
            var name = fields[i];
            var method = name.charAt(0).toUpperCase() + name.substring(1);
            p["set" + method] = new Function(name, "this." + name + " = " + name + ";");
            p["get" + method] = new Function("return this." + name + ";");
        }
    };

    /*
     * $RCSfile: Logger.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Logger = (function() {
        var c = function(name, pattern, level) {
            this.name = name;
            this.pattern = pattern;
            this.level = level;
        };

        c.prototype = {
            getDateTime: function(date) {
                if(date == null) {
                    date = new Date();
                }

                var a = [];
                var y = date.getFullYear();
                var M = date.getMonth() + 1;
                var d = date.getDate();
                var h = date.getHours();
                var m = date.getMinutes();
                var s = date.getSeconds();
                var S = date.getTime() % 1000;

                a[a.length] = y.toString();
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
                a[a.length] = ":";

                if(s < 10) {
                    a[a.length] = "0";
                }

                a[a.length] = s.toString();
                a[a.length] = " ";

                if(S < 100) {
                    a[a.length] = "0";
                }

                if(S < 10) {
                    a[a.length] = "0";
                }

                a[a.length] = S.toString();
                return a.join("");
            },
            format: function(level, message) {
                var result = this.pattern;
                var content = (message || "null");

                if(this.pattern.indexOf("%d") > -1) {
                    result = result.replace("%d", this.getDateTime());
                }

                if(this.pattern.indexOf("%name") > -1) {
                    result = result.replace("%name", this.name);
                }

                if(this.pattern.indexOf("%level") > -1) {
                    result = result.replace("%level", level);
                }

                if(this.pattern.indexOf("%msg") > -1) {
                    result = result.replace("%level", level);
                }
                return result.replace("%msg", content.toString());
            }
        };

        if(typeof(console) != "undefined") {
            c.prototype.debug = function(message) {
                if(this.level <= 1) {
                    console.log(this.format("DEBUG", message));
                }
            };
            c.prototype.info = function(message) {
                if(this.level <= 2) {
                    console.info(this.format("INFO", message));
                }
            };
            c.prototype.warn = function(message) {
                if(this.level <= 3) {
                    console.warn(this.format("WARN", message));
                }
            };
            c.prototype.error = function(message) {
                if(this.level <= 4) {
                    console.error(this.format("ERROR", message));
                }
            };
        }
        else {
            c.prototype.debug = function(message) {
            };
            c.prototype.info = function(message) {
            };
            c.prototype.warn = function(message) {
            };
            c.prototype.error = function(message) {
            };
        }
        return c;
    })();

    /*
     * $RCSfile: LoggerFactory.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var LoggerFactory = {
        level: 2,
        getLogger: function(name, pattern) {
            return new Logger((name || "NULL"), (pattern || "%d [%name.%level] - %msg"), this.level);
        }
    };

    /*
     * $RCSfile: DomUtil.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var DomUtil = {};

    DomUtil.addEventListener = function(target, type, handler) {
        if(target.attachEvent) {
            target.attachEvent("on" + type, handler);
        }
        else if(target.addEventListener) {
            target.addEventListener(type, handler, false);
        }
        else {
            target["on" + type] = handler;
        }
    };

    DomUtil.removeEventListener = function(target, type, handler) {
        if(target.detachEvent) {
            target.detachEvent("on" + type, handler);
        }
        else if(target.removeEventListener) {
            target.removeEventListener(type, handler, false);
        }
        else {
            target["on" + type] = null;
        }
    };

    DomUtil.createElement = function(name, params) {
        var e = null;

        if(window.ActiveXObject) {
            var attrs = Stringify.attributes(params);

            if(attrs.length > 0) {
                e = document.createElement("<" + name + " " + attrs + ">");
            }
            else {
                e = document.createElement("<" + name + ">");
            }
        }
        else {
            e = document.createElement(name);

            if(params != null) {
                for(var i in params) {
                    if(params[i] != null) {
                        e[i] = params[i];
                    }
                }
            }
        }
        return e;
    };

    /*
     * $RCSfile: Sequence.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Sequence = function(start, pattern) {
        this.value = start;
        this.pattern = pattern;

        function next() {
            var id = (this.value++).toString();

            if(this.pattern != null) {
                return this.pattern.replace("${sequence}", id);
            }
            return id;
        }
    };

    Sequence.prototype.next = function() {
        var id = (this.value++).toString();

        if(this.pattern != null) {
            return this.pattern.replace("${sequence}", id);
        }
        return id;
    };

    var ID = new Sequence(1, null);

    ID.next = function(pattern) {
        if(pattern != null) {
            return pattern.replace("${id}", (this.value++).toString());
        }
        else {
            return (this.value++).toString();
        }
    };

    /*
     * $RCSfile: Html.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Html = {
        entities: {
            "<":  "&lt;",
            ">":  "&gt;",
            "&":  "&amp;",
            "\"": "&quot;",
            " ":  "&nbsp;",
            "\u00ae": "&reg;",
            "\u00a9": "&copy;"
        },
        replace: function(c){return Html.entities[c];},
        encode: function(source, crlf){
            if(source == null) {
                return "";
            }
            if(crlf == null) {
                crlf = "&#13;&#10;";
            }
            return source.toString().replace(new RegExp("[<>\"\\u00ae\\u00a9]", "g"), Html.replace).replace(new RegExp("\\r?\\n", "g"), crlf);
        }
    };

    /*
     * $RCSfile: Stringify.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Stringify = {
        trim: function(source){return (source != null ? source.toString().replace(/(^\s*)|(\s*$)/g, "") : "");},

        url: function(params) {
            var b = [];

            for(var i in params) {
                if(params[i] != null) {
                    b[b.length] = i + "=" + encodeURIComponent(params[i]);
                }
            }
            return b.join("&");
        },
        text: function(source) {
            if(source == null) {
                return "";
            }

            var entities = {
                "\\": "\\\\",
                "\'": "\\\'",
                "\"": "\\\"",
                "\r": "\\r",
                "\n": "\\n",
                "\t": "\\t",
                "\b": "\\b",
                "\f": "\\f"
            };

            var replace = function(c) {
                return entities[c];
            };
            return source.toString().replace(new RegExp("[\\\'\"\r\n\t\b\f]", "g"), replace);
        },
        json: function(params) {
            var b = [];

            for(var i in params) {
                if(params[i] != null) {
                    b[b.length] = i + ": \"" + this.text(params[i]) + "\"";
                }
            }
            return "{" + b.join(",") + "}";
        },
        attributes: function(params) {
            var b = [];
            if(params != null) {
                for(var i in params) {
                    if(params[i] != null) {
                        b[b.length] = i + "=\"" + Html.encode(params[i]) + "\"";
                    }
                }
            }
            return b.join(" ");
        }
    };

    /*
     * $RCSfile: Invokation.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Invokation = {
        safe: function() {
            try {
                this.unsafe.apply(this, arguments);
            }
            catch(e) {
            }
        },
        unsafe: function(context, object, methodName) {
            var method = object[methodName];

            if(method != null) {
                if(arguments != null && arguments.length > 0) {
                    var args = Array.prototype.slice.call(arguments, 3);
                    return method.apply(context, args);
                }
                else {
                    return method.apply(context);
                }
            }
            else {
                throw {"name": "NoSuchMethodError", "message": "method \"" + name + "\" not found !"};
            }
        }
    };

    /*
     * $RCSfile: Ajax.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Ajax = {
        getXmlHttpRequest: function() {
            var xmlHttpRequest = null;

            if(window.ActiveXObject != null) {
                xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            }
            else {
                xmlHttpRequest = new XMLHttpRequest();
            }
            return xmlHttpRequest;
        },
        request: function(request) {
            var xmlHttpRequest = ((request.transport != null && request.transport != undefined) ? request.transport : this.getXmlHttpRequest());

            if(xmlHttpRequest == null || xmlHttpRequest == undefined) {
                if(request.error != null) {
                    request.error(null);
                }
                return;
            }

            var url = request.url;
            var method = request.method;
            var headers = request.headers;
            xmlHttpRequest.open(method, request.url, (request.async == false ? false : true));

            xmlHttpRequest.onreadystatechange = function() {
                if(xmlHttpRequest.readyState == 4) {
                    if(request.callback != null) {
                        request.callback(xmlHttpRequest);
                        return;
                    }
                    if(xmlHttpRequest.status == 0 || xmlHttpRequest.status == 200) {
                        if(request.success != null) {
                            request.success(xmlHttpRequest);
                        }
                    }
                    else {
                        if(request.error != null) {
                            request.error(xmlHttpRequest);
                        }
                    }
                }
            };

            xmlHttpRequest.ontimeout = request.ontimeout;
            xmlHttpRequest.onprogress = request.onprogress;
            xmlHttpRequest.upload.onprogress = request.onuploadprogress;

            if(headers != null) {
                for(var i = 0; i < headers.length; i++) {
                    var header = headers[i];
                    xmlHttpRequest.setRequestHeader(header.name, header.value);
                }
            }
            xmlHttpRequest.send(request.data);
            return xmlHttpRequest;
        },
        append: function(formData, data) {
            if(formData == null || data == null) {
                return;
            }

            for(var i in data) {
                var value = data[i];
                var type = typeof(data[i]);

                if(type == "string" || type == "number" || type == "boolean") {
                    formData.append(i, value);
                }
                else if(className == "object" && value.length != null) {
                    for(var j = 0; j < value.length; j++) {
                        if(value[j] != null) {
                            formData.append(i, value[j]);
                        }
                    }
                }
                else {
                    formData.append(i, value);
                }
            }
        },
        getFormData: function(name, file) {
            var args = arguments;
            var formData = new FormData();
            formData.append(name, file, file.name);

            for(var i = 2; i < args.length; i++) {
                this.append(formData, args[i]);
            }
            return formData;
        },
        getBlob: function(file, start, end) {
            var blob = file;
            start = (start || 0);
            end = (end || 0);

            if(file.slice) {
                logger.debug("file.slice: " + start + "/" + end);
                blob = file.slice(start, end);
                blob.name = file.name;
                blob.contentType = file.type;
            }

            if(file.webkitSlice) {
                logger.debug("file.webkitSlice " + start + "/" + end);
                blob = file.webkitSlice(start, end);
                blob.name = file.name;
                blob.contentType = file.type;
            }

            if(file.mozSlice) {
                logger.debug("file.mozSlice " + start + "/" + end);
                blob = file.mozSlice(start, end);
                blob.name = file.name;
                blob.contentType = file.type;
            }
            return blob;
        },
        submit: function(file, settings) {
            var url = settings.url;
            var name = settings.name;
            var data = settings.data;
            var length = file.size;
            var partSize = (settings.partSize || 10240);
            var retryTimes = (settings.retryTimes || 3);
            var upload = function(offset, count) {
                var end = Math.min(offset + partSize, length);
                var range = "bytes " + offset + "-" + end + "/" + length;
                var formData = Ajax.getFormData(name, Ajax.getBlob(file, offset, end), data, {"offset": offset, "start": offset, "end": end, "length": length});
                logger.debug("upload[" + count + "] - name: " + name + ", range: " + range);

                var options = {
                    "url": url,
                    "method": "post",
                    "headers": [{"name": "Content-Range", "value": range}],
                    "data": formData,
                    "transport": settings.transport,
                    "onuploadprogress": function(e) {
                        var loaded = Math.min(offset + (e.loaded || e.position), length);
                        var total = (e.total || e.totalSize);
                        logger.debug("invoke settings.progress(" + loaded + ", " + length + ");");
                        settings.progress(loaded, length);
                    },
                    "error": function(xhr) {
                        /**
                         * ready & abort
                         */
                        if(xhr.readyState == 0) {
                            settings.error(xhr, "AbortException");
                            return;
                        }

                        if(count < retryTimes) {
                            logger.warn("upload failed, retry " + (count + 1));
                            upload(offset, count + 1);
                        }
                        else {
                            logger.debug("invoke settings.error(UploadException 1): connect failed !");
                            settings.error(xhr, "UploadException");
                        }
                    },
                    "success": function(xhr) {
                        logger.debug("xhr.readyState: " + xhr.readyState);
                        logger.debug("settings.dataType: " + settings.dataType + ", responseText: " + xhr.responseText);

                        if(xhr.status == 0) {
                            settings.error(xhr, "AbortException");
                            return;
                        }

                        var result = Ajax.getResponse(xhr.responseText, "json");

                        /**
                         * success - (result.status == 0 || result.status == 200)
                         */
                        if(result.status == 0) {
                            result.status = 200;
                        }

                        /**
                         * 
                         */
                        if(result.start == null || result.start == undefined) {
                            result.start = end;
                        }

                        if(result == null || result.status != 200) {
                            logger.debug("invoke settings.error(BadResponseException 2) - result: " + result);

                            if(count < retryTimes) {
                                logger.warn("upload failed, retry " + (count + 1));
                                upload(offset, count + 1);
                            }
                            else {
                                settings.error(xhr, "BadResponseException");
                            }
                            return;
                        }

                        logger.debug("XMLHttpResponse.response: " + JSON.stringify(result));
                        var start = parseInt(result.start);

                        if(isNaN(start)) {
                            logger.debug("invoke settings.error (BadResponseException 3) - result.start: " + result.start);
                            settings.error(xhr, "BadResponseException");
                            return;
                        }

                        if(start >= length) {
                            settings.success(xhr);
                        }
                        else {
                            upload(end, 0);
                        }
                    }
                };
                Ajax.request(options);
            };
            upload(0, 0);
        },
        getResponse: function(content, dataType) {
            try {
                if(dataType == "text") {
                    return content;
                }

                if(dataType == "script") {
                    var e = document.createElement("script");
                    e.type = "text/javascript";
                    e.text = content;
                    document.body.appendChild(e);
                    return content;
                }

                if(dataType == "json") {
                    return eval("(" + content + ")");
                }
                else {
                    /**
                     * text, html, etc..
                     */
                }
            }
            catch(e) {
            }
            return content;
        },
        getByteSize: function(size) {
            if(size < 1024) {
                return size.toFixed(1) + "B";
            }
            if(size < 1048576) {
                return (size / 1024).toFixed(1) + "K";
            }
            return (size / 1024 / 1024).toFixed(1) + "M";
        },
        getTimes: function(second) {
            var b = [];
            var h = Math.floor(second / 3600);
            var m = Math.floor(second % 3600 / 60);
            var s = Math.floor(second % 60);

            if(h > 0) {
                b[b.length] = (h < 10 ? "0" + h : h);
                b[b.length] = ":";
            }

            b[b.length] = (m < 10 ? "0" + m : m);
            b[b.length] = ":";
            b[b.length] = (s < 10 ? "0" + s : s);
            return b.join("");
        }
    };

    /*
     * $RCSfile: FileItem.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FileItem = function(id, file) {
        this.id = id;
        this.file = file;
        this.startTime = 0;
        this.lastLoaded = 0;
        this.lastTimeMillis = 0;
    };

    FileItem.prototype = {
        getItem: function() {
            var extension = "";
            var k = this.file.name.lastIndexOf(".");

            if(k > -1) {
                extension = this.file.name.substring(k + 1).toLowerCase();
            }
            return {id: this.id, name: this.file.name, extension: extension, size: this.file.size};
        },
        getSpeed: function(loaded) {
            var timeMillis = new Date().getTime();

            if(this.lastTimeMillis <= 0) {
                this.lastTimeMillis = timeMillis;
            }

            var seconds = (timeMillis - this.lastTimeMillis) / 1000;
            var speed = (seconds > 0 ? Math.floor((loaded - this.lastLoaded) / seconds) : (loaded - this.lastLoaded));

            this.lastLoaded = loaded;
            this.lastTimeMillis = timeMillis;
            return speed;
        },
        getSeconds: function() {
            return Math.floor((new Date().getTime() - this.startTime) / 1000);
        },
        abort: function() {
            if(this.httpRequest != null) {
                try {
                    this.httpRequest
                    this.httpRequest.abort();
                }
                catch(e) {
                }
                this.httpRequest = null;
            }
        }
    };

    /*
     * $RCSfile: FileList.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FileList = function() {
        this.files = [];
    };

    FileList.from = function(files) {
        var fileList = new FileList();

        for(var i = 0, length = files.length; i < length; i++) {
            fileList.add(files[i]);
        }
        return fileList;
    };

    FileList.getNextId = function() {
        return FileList.sequence.next();
    };

    FileList.getSequence = function() {
        var pattern = "_form_file_" + this.getRandomString(5) + "_${sequence}";
        return new Sequence(1, pattern);
    };

    FileList.getRandomString = function(size) {
        var buffer = [];
        var chars = "0123456789abcdefghijklmnopqrstuvwxyz";

        for(var i = 0; i < 5; i++) {
            var index = Math.floor(Math.random() * chars.length);
            buffer.push(chars.charAt(index));
        }
        return buffer.join("");
    };
    FileList.sequence = FileList.getSequence();

    FileList.prototype = {
        add: function(file) {
            var id = FileList.getNextId();
            this.files.push(new FileItem(id, file));
        },
        addAll: function(fileList) {
            var list = fileList.files;

            for(var i = 0, length = list.length; i < length; i++) {
                this.files.push((list[i]));
            }
        },
        getFileItem: function(id) {
            for(var i = 0; i < this.files.length; i++) {
                if(id == this.files[i].id) {
                    return this.files[i];
                }
            }
            return null;
        },
        list: function() {
            var list = new Array();

            for(var i = 0; i < this.files.length; i++) {
                var fileItem = this.files[i];
                list.push(fileItem.getItem());
            }
            return list;
        },
        remove: function(id) {
            for(var i = 0; i < this.files.length; i++) {
                if(id == this.files[i].id) {
                    this.files[i].abort();
                    this.files.splice(i, 1);
                    return id;
                }
            }
            return null;
        },
        clear: function() {
            this.files.length = 0;
        }
    };

    var UnsupportOperatorError = new Error("UnsupportOperator", "unsupport operator error!");

    /*
     * $RCSfile: FileUpload.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FileUpload = Class.create(null, /* public abstract */ function(id) {
        this.id = id;
    });

    FileUpload.prototype = {
        /**
         * @return the flash&input element
         */
        getElement: function() {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        ready: function() {
            this.logger.debug("FileUpload[" + this.id + "] intance ready!");
        },

        /**
         * @interface
         */
        dispatch: function(method, args) {
            this.logger.debug("FormUpload.dispatch - method: " + method + ", args: " + args);

            try {
                return this[method].apply(this, args);
            }
            catch(e) {
                this.logger("FormUpload.dispatch(\"" + method + "\") error: " + e.name + ": " + e.message);
            }
        },

        /**
         * @event("select")
         */
        select: function(files) {
            for(var i = 0; i < files.length; i++) {
                this.logger.debug("FormUpload.select - file: " + Stringify.json(files[i]));
            }
            return Invokation.safe(this, this.settings, "select", files);
        },

        /**
         * @event("complete")
         */
        complete: function(file) {
            this.logger.debug("FormUpload.complete: - files: " + Stringify.json(file));
            return Invokation.safe(this, this.settings, "complete", file);
        },

        /**
         * @event("progress")
         */
        progress: function(file, loaded, total, speed, seconds) {
            this.logger.debug("FormUpload.progress - fileId: " + file.id + ", " + loaded + "/" + total + " - " + Math.floor(loaded / total * 100) + "%, " + speed + ", " + seconds);
            return Invokation.safe(this, this.settings, "progress", file, loaded, total, speed, seconds);
        },

        /**
         * @event("success")
         */
        success: function(file, data) {
            this.logger.debug("FormUpload.success - file: " + Stringify.json(file) + ", data: " + data);
            var result = Ajax.getResponse(data, this.settings.dataType);
            return Invokation.safe(this, this.settings, "success", file, result);
        },

        /**
         * @event("cancel")
         */
        cancel: function(file) {
            this.logger.debug("FormUpload.cancel - file: " + Stringify.json(file));
            return Invokation.safe(this, this.settings, "cancel", file);
        },

        /**
         * @event("error")
         */
        error: function(file, data) {
            this.logger.debug("FormUpload.error - file: " + Stringify.json(file) + ", data: " + data);
            return Invokation.safe(this, this.settings, "error", file, data);
        },

        /**
         * @interface
         */
        load: function(fileId) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        push: function(files) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        upload: function(fileId, url, name, data) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        abort: function(fileId) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        remove: function(fileId) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        getFileList: function() {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        getDataURL: function(fileId) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        getBase64Data: function(fileId) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        getResponse: function(content, dataType) {
            return Ajax.getResponse(content, dataType);
        },

        /**
         * @interface
         */
        getVersion: function() {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        getSystemProperty: function(name) {
            throw UnsupportOperatorError;
        },

        /**
         * @interface
         */
        destory: function() {
            throw UnsupportOperatorError;
        }
    };

    /*
     * $RCSfile: FormUpload.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FormUpload = Class.create(FileUpload, function(id, inputId, settings, logger) /* extends FileUpload */{
        this.id = id;
        this.inputId = inputId;
        this.settings = settings;
        this.fileList = new FileList();
        this.logger = logger;
    });

    Class.extend(FormUpload.prototype, {
        /**
         * @interface
         * @Override
         */
        getElement: function() {
            return document.getElementById(this.inputId);
        },

        /**
         * @interface
         * @Override
         */
        load: function(fileId) {
            this.logger.debug("FormUpload.load: " + fileId);

            this.logger.debug("this.fileList: " + JSON.stringify(this.fileList));
            var fileItem = this.fileList.getFileItem(fileId);

            if(fileItem == null) {
                return {"name": "FileNotFoundError", "message": "file not found !"};
            }

            if(fileItem.data != null) {
                this.complete(fileItem.getItem());
                return null;
            }

            var self = this;
            var file = fileItem.file;
            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function(e){
                fileItem.data = reader.result;
                self.complete(fileItem.getItem());
            };
            return null;
        },

        /**
         * @interface
         * @Override
         */
        push: function(files) {
            var fileList = FileList.from(files);
            this.fileList.addAll(fileList);
            return fileList.list();
        },

        /**
         * @interface
         * @Override
         */
        upload: function(fileId, url, name, data) {
            logger.debug("upload: fileId: " + fileId + ", url: " + url + ", name: " + name);

            try {
                var timeMillis = new Date().getTime();
                var fileItem = this.fileList.getFileItem(fileId);

                if(fileItem == null) {
                    return {"name": "FileNotFoundError", "message": "file not found !"};
                }

                if(fileItem.status == 1) {
                    throw new Error("UploadError", "UploadError");
                }

                if(fileItem.httpRequest == null) {
                    fileItem.httpRequest = Ajax.getXmlHttpRequest();
                }

                var self = this;
                var settings = {
                    "url": url,
                    "name": name,
                    "data": data,
                    "partSize": (self.settings.partSize || 10240),
                    "transport": fileItem.httpRequest,
                    "progress": function(loaded, length) {
                        var speed = Ajax.getByteSize(fileItem.getSpeed(loaded));
                        self.progress(fileItem.getItem(), loaded, length, speed, fileItem.getSeconds());
                    },
                    "success": function(xhr) {
                        self.logger.debug("FormUpload.success - fileId: " + fileId + ", xhr: " + xhr);
                        fileItem.status = 0;
                        self.success(fileItem.getItem(), xhr.responseText);
                    },
                    "error": function(xhr) {
                        fileItem.status = 0;
                        self.error(xhr, fileItem.getItem());
                    }
                };
                fileItem.status = 1;
                fileItem.lastLoaded = 0;
                fileItem.startTime = timeMillis;
                fileItem.lastTimeMillis = timeMillis;
                Ajax.submit(fileItem.file, settings);
            }
            catch(e) {
                logger.error(e);
                throw e;
            }
            return null;
        },

        /**
         * @interface
         * @Override
         */
        abort: function(fileId) {
            var fileItem = this.fileList.getFileItem(fileId);

            if(fileItem == null) {
                logger.error("abort error: " + fileId + " not exists!");
                return false;
            }

            var httpRequest = fileItem.httpRequest;
            if(httpRequest != null) {
                try {
                    httpRequest.abort();
                    return true;
                }
                catch (e) {
                    logger.error("abort error: " + fileId);
                }
            }
            return false;
        },

        /**
         * @interface
         * @Override
         */
        remove: function(fileId) {
            this.logger.debug("fileUpload.remove: " + fileId);
            return this.fileList.remove(fileId);
        },

        /**
         * @interface
         * @Override
         */
        getFileList: function() {
            this.logger.debug("getFileList ...");
            return this.fileList.list();
        },

        /**
         * @interface
         * @Override
         */
        getDataURL: function(fileId) {
            this.logger.debug("getDataURL - fileId: " + fileId);

            try {
                var fileItem = this.fileList.getFileItem(fileId);

                if(fileItem == null) {
                    return {"name": "FileNotFoundError", "message": "file not found !"};
                }
                return fileItem.data;
            }
            catch(e) {
                this.logger.error(e);
            }
            return null;
        },

        /**
         * @interface
         * @Override
         */
        getBase64Data: function(fileId) {
            this.logger.debug("getBase64Data " + fileId);
            return null;
        },

        /**
         * @interface
         * @Override
         */
        getVersion: function() {
            return "1.0.0.0";
        },

        /**
         * @interface
         * @Override
         */
        getSystemProperty: function(name) {
            return null;
        },

        /**
         * @interface
         * @Override
         */
        destory: function() {
            var e = document.getElementById(this.inputId);

            if(e != null) {
                var parent = e.parentNode;
                parent.removeChild(e);
                parent.parentNode.removeChild(parent);
            }
            FileUploadFatory.remove(this.id);
            return true;
        },
    });

    /*
     * $RCSfile: FormUploadContext.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FormUploadContext = {};

    FormUploadContext.getFileInput = function(id, name, accept, multiple) {
        var params = {
            "id": id,
            "name": name,
            "type": "file",
            "accept": (accept || "*"),
            "multiple": multiple
        };

        var div = DomUtil.createElement("div");
        var input = DomUtil.createElement("input", params);
        div.style.display = "none";
        div.appendChild(input);
        document.body.appendChild(div);
        return input;
    };

    FormUploadContext.create = function(settings) {
        var e = null;

        if(typeof(settings.id) == "string") {
            e = document.getElementById(settings.id);
        }
        else {
            e = settings.id;
        }

        var id = ID.next();
        var inputId = "_skin_file_input_" + id;
        var instanceId = "_skin_file_upload_" + id;
        var uploadLogger = LoggerFactory.getLogger("_skin_upload_logger_" + id);
        var instance = new FormUpload(instanceId, inputId, settings, uploadLogger);
        var input = this.getFileInput(inputId, inputId, settings.accept, settings.multiple);
        var onchange = function(event) {
            var e = (event || window.event);
            var input = e.target;
            var files = input.files;
    
            if(files == null || files.length < 1) {
                return;
            }

            var fileList = FileList.from(input.files);

            if(instance.fileList == null) {
                instance.fileList = new FileList();
            }
            input.value = "";
            instance.fileList.addAll(fileList);
            instance.select(fileList.list());
        };

        DomUtil.addEventListener(input, "change", onchange);

        if(e != null) {
            DomUtil.addEventListener(e, "click", function() {
                input.click();
            });
        }
        return FileUploadFactory.setInstance(instanceId, instance);
    };

    /*
     * $RCSfile: FlashUpload.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FlashUpload = Class.create(FileUpload, function(id, flashId, settings, logger) {
        this.id = id;
        this.flashId = flashId;
        this.settings = settings;
        this.logger = logger;
    });

    Class.extend(FlashUpload.prototype, {
        /**
         * @interface
         * @Override
         */
        getElement: function() {
            return FlashUtil.getInstance(this.flashId);
        },

        /**
         * @interface
         * @Override
         */
        load: function(fileId) {
            this.logger.debug("FlashUpload.load: " + fileId);
            return FlashUtil.invoke(this.flashId, "load", fileId);
        },

        /**
         * @interface
         * @Override
         */
        upload: function(fileId, url, name, data) {
            this.logger.debug("upload: " + fileId + ", name: " + name);
            FlashUtil.invoke(this.flashId, "upload", fileId, url, name, data);
        },

        /**
         * @interface
         * @Override
         */
        abort: function(fileId) {
            this.logger.debug("abort: " + fileId);
            FlashUtil.invoke(this.flashId, "abort", fileId);
        },

        /**
         * @interface
         * @Override
         */
        remove: function(fileId) {
            this.logger.debug("FlashUpload.remove: " + fileId);
            return FlashUtil.invoke(this.flashId, "removeFile", fileId);
        },

        /**
         * @interface
         * @Override
         */
        getFileList: function() {
            this.logger.debug("getFileList ...");
            return FlashUtil.invoke(this.flashId, "getFileList");
        },

        /**
         * @interface
         * @Override
         */
        getDataURL: function(fileId) {
            this.logger.debug("getDataURL: " + fileId);
            return FlashUtil.invoke(this.flashId, "getDataURL", fileId);
        },

        /**
         * @interface
         * @Override
         */
        getBase64Data: function(fileId) {
            this.logger.debug("getBase64Data " + fileId);
            return FlashUtil.invoke(this.flashId, "getBase64Data", fileId);
        },

        /**
         * @interface
         * @Override
         */
        getVersion: function() {
            return FlashUtil.invoke(this.flashId, "getVersion");
        },

        /**
         * @interface
         * @Override
         */
        getSystemProperty: function(name) {
            return FlashUtil.invoke(this.flashId, "getSystemProperty", "version");
        },

        /**
         * @interface
         * @Override
         */
        bug: function() {
            return FlashUtil.invoke(this.flashId, "hello", "\"hello\"");
        },

        /**
         * @interface
         * @Override
         */
        destory: function() {
            var e = document.getElementById(this.flashId);

            if(e != null) {
                var parent = e.parentNode;
                parent.removeChild(e);
                parent.parentNode.removeChild(parent);
            }

            var loggerId = this.id.replace("_skin_flash_upload_", "_skin_flash_logger_");
            window[loggerId] = null;
            delete window[loggerId];
            FileUploadFatory.remove(this.id);
            return true;
        }
    });

    /*
     * $RCSfile: FlashFactory.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FlashFactory = {
        build: function(settings) {
            var b = [];
            var isIE = (typeof(window.ActiveXObject) != "undefined");
            var classId = (settings.classId || "clsid:d27cdb6e-ae6d-11cf-96b8-444553540000");
            var width = (settings.width || 0);
            var height = (settings.height || 0);
            var params = (settings.params || {});

            var attrs = {
                "id": settings.id,
                "type": "application/x-shockwave-flash",
                "data": settings.movie,
                "width": settings.width,
                "height": settings.height,
                "align": settings.align
            };

            if(isIE) {
                attrs = {
                    "id": settings.id,
                    "classId": classId,
                    "width": settings.width,
                    "height": settings.height,
                    "align": settings.align
                }
            }

            b[b.length] = "<object " + Stringify.attributes(attrs) + ">";
            b[b.length] = "<param name=\"movie\" value=\"" + Html.encode(settings.movie) + "\"/>";

            for(var i in params) {
                b[b.length] = "    <param name=\"" + i + "\" value=\"" + Html.encode(params[i]) + "\"/>";
            }
            b[b.length] = (isIE ? "<a href=\"http://www.adobe.com/go/getflash\">Get Adobe Flash Player</a>" : "");
            b[b.length] = "</object>";
            return b.join("");
        },
        create: function(settings) {
            var container = document.getElementById(settings.id);

            if(container != null) {
                var div = document.createElement("div");
                div.innerHTML = this.build(settings);
                container.parentNode.insertBefore(div, container);
                container.parentNode.removeChild(container);
            }
        }
    };

    /*
     * $RCSfile: FlashUtil.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FlashUtil = {
        getInstance: function(id) {
            var e = document.getElementById(id);

            if(e != null) {
                return e;
            }

            var list = document.getElementsByName(id);

            if(list != null && list.length > 0) {
                for(var i = 0; i < list.length; i++) {
                    if(list[i].nodeType == 1 && list[i].nodeName.toLowerCase() == "embed") {
                        return list[i];
                    }
                }
            }
            return null;
        },
        invoke: function(id, methodName) {
            logger.debug("FlashUtil.invoke - id: " + id + ", name: " + methodName);
            var flash = this.getInstance(id);

            if(flash == null) {
                throw new Error("FlashNotFoundError", "flash object not found !");
            }

            var method = flash[methodName];

            if(method == null) {
                throw new Error("NoSuchMethodError", "method \"" + name + "\" not found !");
            }

            if(arguments != null && arguments.length > 0) {
                var args = Array.prototype.slice.call(arguments, 2);
                return method.apply(flash, args);
            }
            else {
                return method();
            }
        },
        delay: function() {
            var args = arguments;
            var handler = this.invoke;
            setTimeout(function() {
                handler.apply(FlashUtil, args);
            }, 100);
        }
    };

    /*
     * $RCSfile: FlashUploadContext.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FlashUploadContext = {};

    FlashUploadContext.getAccept = function(accept) {
        var b = [];
        var a = (accept || "*").split(",");

        for(var i = 0; i < a.length; i++) {
            b[b.length] = "*." + a[i];
        }
        return b.join("; ");
    };

    FlashUploadContext.create = function(settings) {
        var e = null;

        if(typeof(settings.id) == "string") {
            e = document.getElementById(settings.id);
        }
        else {
            e = settings.id;
        }

        var top = e.offsetTop;
        var left = e.offsetLeft;
        var width = e.offsetWidth;
        var height = e.offsetHeight;
        var id = ID.next();
        var flashId = "_skin_flash_" + id;
        var loggerId = "_skin_flash_logger_" + id;
        var instanceId = "_skin_flash_upload_" + id;
        var uploadLogger = LoggerFactory.getLogger("_skin_upload_logger_" + id);
        var flashLogger = window[loggerId] = LoggerFactory.getLogger(loggerId);
        var flashVars = {
            "instanceId": instanceId,
            "domain": window.location.hostname,
            "title": (settings.title || "请选择文件"),
            "accept": FlashUploadContext.getAccept(settings.accept),
            "multiple": (settings.multiple || "false"),
            "logger": loggerId
        };
        var html = FlashFactory.build({
            "id": flashId,
            "movie": (settings.swfURL || "/flash/uploader.swf"),
            "width": width,
            "height": height,
            "params": {
                "wmode": "transparent",
                "scale": "noscale",
                "allowNetworking": "all",
                "allowScriptAccess": "sameDomain",
                "flashVars": Stringify.url(flashVars)
            }
        });

        logger.debug("instanceId: " + instanceId + ", flashId: " + flashId + ", flash.logger: " + loggerId);
        logger.debug("flash.position: {top: " + top + ", left: " + left + ", width: " + width + ", height: " + height + "}");
        logger.debug(html);

        var css = [
            "margin: 0px;",
            "padding: 0px;",
            "top: 0px;",
            "left: 0px;",
            "width: " + width + "px;",
            "height: " + height + "px;",
            "text-align: left;",
            "position: absolute;",
            "display: block;",
            "z-index: 1000;"
        ];
        var div = document.createElement("div");
        div.style.cssText = css.join(" ");
        div.innerHTML = html;
        e.style.position = "relative";
        e.appendChild(div);

        var instance = new FlashUpload(instanceId, flashId, settings, uploadLogger);
        return FileUploadFactory.setInstance(instanceId, instance);
    };

    var exports = {
        "Html": Html,
        "Ajax": Ajax,
        "Sequence": Sequence,
        "Stringify": Stringify,
        "Invokation": Invokation,
        "LoggerFactory": LoggerFactory
    };

    /*
     * $RCSfile: FlashUploadContext.js,v $$
     * $Revision: 1.1 $
     * $Date: 2015-11-22 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FileUploadFactory = {
        context: [],
        getById: function(id) {
            return this.context[id];
        },
        setInstance: function(id, instance) {
            return (this.context[id] = instance);
        },
        create: function(settings, name) {
            if(name == "h5") {
                return FormUploadContext.create(settings);
            }
            else if(name == "flash") {
                return FlashUploadContext.create(settings);
            }

            if(typeof(FormData) == "undefined") {
                return FlashUploadContext.create(settings);
            }
            else {
                return FormUploadContext.create(settings);
            }
        },
        remove: function(id) {
            var instance = this.getById(id);

            if(instance != null) {
                this.context[id] = null;
                delete this.context[id];
            }
        },
        dispatch: function(instanceId, methodName, args) {
            var instance = this.getById(instanceId);

            if(instance == null) {
                logger.warn(instanceId + " not found !");
                return;
            }
            instance.dispatch(methodName, args);
        },
        export: function(name, context) {
            var object = exports[name];

            if(object == null) {
                return object;
            }
            return eval(name);
        }
    };

    var logger = scope.logger = LoggerFactory.getLogger("STDOUT", null);
    FileUploadFactory.stdlogger = logger;
    scope.FileUploadFactory = FileUploadFactory;
})(window);