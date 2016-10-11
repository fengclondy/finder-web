var ColorDialog = {};

ColorDialog.open = function(x, y, callback) {
    var e = this.getContainer();
    e.style.position = "absolute";
    e.style.top = y + "px";
    e.style.left = x + "px";
    e.style.zIndex = 1000;
    e.style.display = "block";

    if(callback != null) {
        this.click = function(color) {
            callback(color);
        };
    }
    else {
        this.click = null;
    }
};

ColorDialog.close = function() {
    var e = this.getContainer();
    e.style.display = "none";
};

ColorDialog.getContainer = function() {
    var id = "_color_dialog_";
    var e = document.getElementById(id);

    if(e == null) {
        e = this.create(id);
    }
    return e;
}

ColorDialog.create = function(id) {
    var self = this;
    var e = document.createElement("div");
    e.id = id;
    e.innerHTML = this.getHtml();
    e.style.cssText = "clear: both; float: left; width: 210px; height: 173px; background-color: #333333;";
    document.body.appendChild(e);

    ColorDialog.addEventListener(e, "mouseover", function(e) {
        var event = (e || window.event);
        var src = (event.target || event.srcElement);
        var color = src.getAttribute("color");

        if(color != null) {
            document.getElementById("_color_txt").innerHTML = color;
            document.getElementById("_color_bar").style.backgroundColor = color;
        }
    });

    ColorDialog.addEventListener(e, "click", function(e) {
        var event = (e || window.event);
        var src = (event.target || event.srcElement);
        var color = src.getAttribute("color");

        if(color != null) {
            if(self.click != null) {
                self.click(color);
            }
        }
    });
    return e;
};

ColorDialog.getHtml = function() {
    var colors = [
        "000000", "000000", "000033", "000066", "000099", "0000cc", "0000ff", "003300", "003333", "003366", "003399", "0033cc", "0033ff", "006600", "006633", "006666", "006699", "0066cc", "0066ff",
        "333333", "009900", "009933", "009966", "009999", "0099cc", "0099ff", "00cc00", "00cc33", "00cc66", "00cc99", "00cccc", "00ccff", "00ff00", "00ff33", "00ff66", "00ff99", "00ffcc", "00ffff",
        "666666", "330000", "330033", "330066", "330099", "3300cc", "3300ff", "333300", "333333", "333366", "333399", "3333cc", "3333ff", "336600", "336633", "336666", "336699", "3366cc", "3366ff",
        "999999", "339900", "339933", "339966", "339999", "3399cc", "3399ff", "33cc00", "33cc33", "33cc66", "33cc99", "33cccc", "33ccff", "33ff00", "33ff33", "33ff66", "33ff99", "33ffcc", "33ffff",
        "cccccc", "660000", "660033", "660066", "660099", "6600cc", "6600ff", "663300", "663333", "663366", "663399", "6633cc", "6633ff", "666600", "666633", "666666", "666699", "6666cc", "6666ff",
        "ffffff", "669900", "669933", "669966", "669999", "6699cc", "6699ff", "66cc00", "66cc33", "66cc66", "66cc99", "66cccc", "66ccff", "66ff00", "66ff33", "66ff66", "66ff99", "66ffcc", "66ffff",
        "ff0000", "990000", "990033", "990066", "990099", "9900cc", "9900ff", "993300", "993333", "993366", "993399", "9933cc", "9933ff", "996600", "996633", "996666", "996699", "9966cc", "9966ff",
        "00ff00", "999900", "999933", "999966", "999999", "9999cc", "9999ff", "99cc00", "99cc33", "99cc66", "99cc99", "99cccc", "99ccff", "99ff00", "99ff33", "99ff66", "99ff99", "99ffcc", "99ffff",
        "0000ff", "cc0000", "cc0033", "cc0066", "cc0099", "cc00cc", "cc00ff", "cc3300", "cc3333", "cc3366", "cc3399", "cc33cc", "cc33ff", "cc6600", "cc6633", "cc6666", "cc6699", "cc66cc", "cc66ff",
        "ffff00", "cc9900", "cc9933", "cc9966", "cc9999", "cc99cc", "cc99ff", "cccc00", "cccc33", "cccc66", "cccc99", "cccccc", "ccccff", "ccff00", "ccff33", "ccff66", "ccff99", "ccffcc", "ccffff",
        "00ffff", "ff0000", "ff0033", "ff0066", "ff0099", "ff00cc", "ff00ff", "ff3300", "ff3333", "ff3366", "ff3399", "ff33cc", "ff33ff", "ff6600", "ff6633", "ff6666", "ff6699", "ff66cc", "ff66ff",
        "ff00ff", "ff9900", "ff9933", "ff9966", "ff9999", "ff99cc", "ff99ff", "ffcc00", "ffcc33", "ffcc66", "ffcc99", "ffcccc", "ffccff", "ffff00", "ffff33", "ffff66", "ffff99", "ffffcc", "ffffff"
    ];

    var b = [];
    var img = "data:image/gif;base64,R0lGODlhCgAKAJEAAAAAAP///8zMzP///yH5BAEAAAMALAAAAAAKAAoAAAIRlD2Zhzos3GMSykqd1VltzxQAOw==";
    var css = "float: left; margin: 1px 0px 0px 1px; width: 10px; height: 10px; background-color: #${color}; font-size: 0px; cursor: pointer;";

    b[b.length] = "<div style=\"padding: 0px; width: 210px;height: 15px; background-color: #ffffff; background-image: url(" + img + "); background-repeat: repeat-x; overflow: hidden;}\">";
    b[b.length] = "<em id=\"_color_bar\" style=\"float: left; display: block; margin: 0px; font-size: 0px; width: 140px; height: 13px; background: #ddf0df;\"></em>";
    b[b.length] = "<em id=\"_color_txt\" style=\"float: left; display: block; margin: 0px; font-size: 12px; width: 58px; height: 13px; overflow: hidden; text-align: center; font-style: normal; background: #ddf0df;\"></em>";
    b[b.length] = "<em style=\"float: left; margin: 1px 0px 0px 1px; width: 10px; height: 10px; background-color: #ffffff; background-image: url(" + img + ")\" color=\"transparent\"></em>";
    b[b.length] = "</div>";

    for(var i = 0; i < colors.length; i++) {
        b[b.length] = "<em color=\"#" + colors[i] +"\" style=\"" + css.replace("${color}", colors[i]) + "\"></em>";
    }

    b[b.length] = "<div style=\"clear: both; margin: 0px 1px 0px 1px; padding-left: 4px; border-top: 1px solid #333333; background-color: #ff9933; line-height: 24px; font-size: 6px; color: #ffffff;\">skin color-picker v1.0.0</div>";
    return b.join("");
};

ColorDialog.addEventListener = function(target, type, handler) {
    if(target == null) {
        return;
    }

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

