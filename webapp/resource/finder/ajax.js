/**
 * Skin JavaScript Library v1.0.0
 * Copyright (c) 2010 xuesong.net
 * 
 * mailto: xuesong.net@163.com
 * Date: 2010-04-28 10:24:21
 * Revision: 1012
 */
var Ajax = {};
Ajax.getXmlHttpRequest = function() {
    return SimpleXmlHttpRequestPool.getInstance();
};

Ajax.createXmlHttpRequest = function() {
    return SimpleXmlHttpRequestPool.createXmlHttpRequest();
};

Ajax.createXmlDocument = function(root, async) {
    var xmlDocument = null;

    if(typeof(ActiveXObject) != "undefined" && typeof(ActiveXObject) != "null") {
        var names = ["MSXML2.DOMDocument.3.0", "Msxml2.DOMDocument", "Microsoft.XMLDOM"];

        for(var i = 0; i < names.length; i++) {
            try {
                xmlDocument = new ActiveXObject(names[i]);
                break;
            }
            catch(e) {
            }
        }

        xmlDocument.appendChild(xmlDocument.createElement(root));
    }
    else if(typeof(document) != "undefined" && document.implementation != null && document.implementation.createDocument != null) {
        xmlDocument = document.implementation.createDocument("", root, null);
    }

    if(xmlDocument != null) {
        xmlDocument.async = (async == false ? false : true);
        xmlDocument.preserveWhiteSpace = true;
        return xmlDocument;
    }

    return xmlDocument;
};

Ajax.serialize = function(json) {
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

Ajax.request = function(request) {
    var xmlHttpRequest = ((request.transport != null && request.transport != undefined) ? request.transport : this.getXmlHttpRequest());

    if(xmlHttpRequest != null && xmlHttpRequest != undefined) {
        var method = request.method;
        var encoding = request.charset;
        xmlHttpRequest.open(method, request.url, (request.async != null ? request.async : true));

        if(method != null && method.toLowerCase() == "post" && encoding != null) {
            xmlHttpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=" + encoding);
        }
        else {
            xmlHttpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        }

        xmlHttpRequest.onreadystatechange = function() {
            if(xmlHttpRequest.readyState == 4) {
                if(request.callback != null) {
                    request.callback(xmlHttpRequest);
                }
                else {
                    if(xmlHttpRequest.status == 0 || xmlHttpRequest.status == 200) {
                        if(request.success != null) {
                            request.success(xmlHttpRequest);
                        }
                    }
                    else if(xmlHttpRequest.status == 404 || xmlHttpRequest.status == 500) {
                        if(request.error != null) {
                            request.error(xmlHttpRequest);
                        }
                    }
                    else {
                        if(request.error != null) {
                            request.error(xmlHttpRequest);
                        }
                    }
                }
            }
        };

        var parameters = Ajax.serialize(request.data);
        xmlHttpRequest.setRequestHeader("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
        xmlHttpRequest.send(parameters);
    }
    else {
        if(request.error != null) {
            request.error(xmlHttpRequest);
        }
    }
};

Ajax.getResponse = function(content, dataType) {
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
};

Ajax.getErrorMessage = function(xmlHttpRequest) {
    if(xmlHttpRequest != null) {
        var status = xmlHttpRequest.status;
        var message = null;

        if(status < 0 || status > 1000) {
            message = (status + ": \u7f51\u7edc\u9519\u8bef, \u65e0\u6cd5\u8fde\u63a5\u5230\u670d\u52a1\u5668 !");
        }
        else if(status == 404) {
            message = (status + ": \u670d\u52a1\u5668\u65e0\u5e94\u7b54 !");
        }
        else if(status == 500) {
            message = (status + ": \u670d\u52a1\u5668\u5185\u90e8\u9519\u8bef !");
        }
        else {
            message = (status + ": \u670d\u52a1\u5668\u5185\u90e8\u9519\u8bef !");
        }

        return message;
    }

    return "xmlHttpRequest undefine";
};

Ajax.loadXml = function(source) {
    var xmlDocument = null;

    if(typeof(ActiveXObject) != "undefined" && typeof(ActiveXObject) != "null") {
        xmlDocument = Ajax.createXmlDocument(false);
        xmlDocument.loadXML(source);
    }
    else {
        xmlDocument = (new DOMParser()).parseFromString(source, "text/xml");
    }

    return xmlDocument;
};

var SimpleXmlHttpRequestPool = {

    instances: [],

    size: function() {
        return this.instances.length;
    },

    getActives: function() {
        var c = 0;
        var a = this.instances;
        var length = a.length;

        for(var i = 0; i < length; i ++) {
            if(a[i].readyState == 0 || a[i].readyState == 4) {
                c++;
            }
        }

        return length - c;
    },

    getInstance1: function() {
        this.getInstance = this.getInstance2;

        var a = this.instances;
        var length = a.length;

        for(var i = 0; i < length; i++) {
            if(a[i].readyState == 0 || a[i].readyState == 4) {
                return a[i];
            }
        }

        var e = this.createXmlHttpRequest();

        if(a.length < 1024) {
            a[a.length] = e;
        }

        return e;
    },

    getInstance2: function() {
        this.getInstance = this.getInstance3;

        var a = this.instances;
        var length = a.length;

        for(var i = length - 1; i > -1; i--) {
            if(a[i].readyState == 0 || a[i].readyState == 4) {
                return a[i];
            }
        }

        var e = this.createXmlHttpRequest();

        if(a.length < 1024) {
            a[a.length] = e;
        }

        return e;
    },

    getInstance3: function() {
        this.getInstance = this.getInstance1;

        var a = this.instances;
        var length = a.length;

        for(var i = Math.floor(length / 2); i > -1; i--) {
            if(a[i].readyState == 0 || a[i].readyState == 4) {
                return a[i];
            }
        }

        for(var i = Math.floor(length / 2) + 1; i < length; i++) {
            if(a[i].readyState == 0 || a[i].readyState == 4) {
                return a[i];
            }
        }

        var e = this.createXmlHttpRequest();

        if(a.length < 1024) {
            a[a.length] = e;
        }

        return e;
    },

    getInstance: function(){
        return this.getInstance1();
    },

    createXmlHttpRequest: function() {
        var xmlHttpRequest = null;

        if(window.ActiveXObject != null) {
            xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        }
        else {
            xmlHttpRequest = new XMLHttpRequest();
        }

        return xmlHttpRequest;
    }
};