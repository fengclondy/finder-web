var Cron = {};

Cron.getRange = function(expression, unit) {
    var a = expression.split("-");

    if(a.length > 1) {
        var i = parseInt(a[0]);
        var j = parseInt(a[1]);
        return "从" + i + unit + "到" + j + unit;
    }

    a = expression.split("/");

    if(a.length > 1) {
        var i = parseInt(a[0]);
        var j = parseInt(a[1]);
        return "从" + i + unit + "开始每" + j + unit;
    }
    return a[0] + unit;
};

Cron.parse = function(expression) {
    var b = [];
    var a = expression.split(" ").reverse();

    if(a.length >= 7) {
        var c = ["年", "周", "月", "日", "时", "分", "秒"];

        for(var i = 0; i < a.length; i++) {
            if(a[i] == "*") {
                b[b.length] = "每" + c[i];
            }
            else if(a[i] != "?") {
                b[b.length] = this.getRange(a[i], c[i]);
            }
        }
    }
    return b.join(" ");
};
