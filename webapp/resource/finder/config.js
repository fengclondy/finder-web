if(typeof(window.Finder) == "undefined") {
    window.Finder = {};
}

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
    var json = JSON.stringify(value);

    if(typeof(window.localStorage) != "undefined") {
        window.localStorage[name] = json;
    }
    else {
        Finder.setCookie({"name": name, "value": value, "expires": 7 * 24 * 60 * 60});
    }
};

Finder.getFinderConfig = function() {
    var config = null;

    try {
        config = JSON.parse(Finder.getLocalVariable("finder"));
    }
    catch(e) {
    }
    return (config != null ? config : {});
};

Finder.setFinderConfig = function(config) {
    Finder.setLocalVariable("finder", config);
};

Finder.setConfig = function(name, value) {
    var a = name.split(".");
    var config = this.getFinderConfig();
    var current = config;

    for(var i = 0; i < a.length - 1; i++) {
        var key = a[i];

        if(current[key] == null || typeof(current[key]) != "object") {
            current[key] = {};
        }
        else {
            console.log(JSON.stringify(current[key]));
        }
        current = current[key];
    }
    current[a[a.length - 1]] = value;
    this.setFinderConfig(config);
};

Finder.getConfig = function(name, defaultValue) {
    var a = name.split(".");
    var current = this.getFinderConfig();

    for(var i = 0; i < a.length; i++) {
        current = current[a[i]];

        if(current == null) {
            break;
        }
    }

    if(current == null || current.length < 1) {
        return defaultValue;
    }
    else {
        return current;
    }
};
