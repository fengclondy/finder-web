if(typeof(window.Finder) == "undefined") {
    window.Finder = {};
}

Finder.getFinderConfig = function() {
    var config = null;

    if(typeof(window.localStorage) == "undefined") {
        config = {};
    }
    else {
        try {
            config = JSON.parse(window.localStorage.finder);
        }
        catch(e) {
        }
    }
    return (config != null ? config : {});
};

Finder.setFinderConfig = function(config) {
    if(typeof(window.localStorage) != "undefined") {
        window.localStorage.finder = JSON.stringify(config);
    }
    else {
        alert("您的浏览器不支持本地存储，请升级浏览器之后再试！");
    }
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
