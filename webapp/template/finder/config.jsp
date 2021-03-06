<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Preferences</title>
<link rel="stylesheet" type="text/css" href="${requestURI}?action=res&path=/finder/css/form.css"/>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/charset.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/colorpicker.js"></script>
<script type="text/javascript" src="${requestURI}?action=res&path=/finder/config.js"></script>
<script type="text/javascript">
<!--
jQuery(function() {
    var theme = Finder.getConfig("global.theme", "RDark");
    var charset = Finder.getConfig("global.charset", "utf-8");
    var fontFamily = Finder.getConfig("less.fontFamily", "Lucida Console");
    var fontColor = Finder.getConfig("less.fontColor", "#009900");
    var backgroundColor = Finder.getConfig("less.backgroundColor", "#000000");
    Charset.setup(jQuery("select[name=charset]").get(0), charset);

    jQuery("select[name=theme]").val(theme);
    jQuery("select[name=fontFamily]").val(fontFamily);
    jQuery("input[name=fontColor]").val(fontColor);
    jQuery("input[name=backgroundColor]").val(backgroundColor);

    jQuery("input[name=fontColorPicker]").css("backgroundColor", fontColor);
    jQuery("input[name=backgroundColorPicker]").css("backgroundColor", backgroundColor);
    jQuery("#fontExample").css("fontFamily", fontFamily);
});

jQuery(function() {
    jQuery("input.color-picker").click(function(event) {
        var position = jQuery(this).position();
        var x = position.left;
        var y = position.top + jQuery(this).height();
        var self = jQuery(this);

        ColorDialog.open(x, y, function(color) {
            self.css("backgroundColor", color);
            self.parent().find("input.color-text").val(color);
            ColorDialog.close();
        });
    });

    jQuery("select[name=fontFamily]").change(function() {
        jQuery("#fontExample").css("fontFamily", this.value);
    });

    jQuery("#default-btn").click(function() {
        jQuery("select[name=theme]").val("RDark");
        jQuery("select[name=charset]").val("utf-8");
        jQuery("select[name=fontFamily]").val("Lucida Console");
        jQuery("input[name=fontColor]").val("#999999");
        jQuery("input[name=backgroundColor]").val("#000000");
    });

    jQuery("#ensure-btn").click(function() {
        var config = {"global": {}, "less": {}};
        var global = config.global;
        var lessConfig = config.less;

        global.theme = jQuery("select[name=theme]").val();
        global.charset = jQuery("select[name=charset]").val();

        lessConfig.fontFamily = jQuery("select[name=fontFamily]").val();
        lessConfig.fontColor = jQuery("input[name=fontColor]").val();
        lessConfig.backgroundColor = jQuery("input[name=backgroundColor]").val();
        Finder.setFinderConfig(config);
        window.close();
    });

    jQuery("#cancel-btn").click(function() {
        window.close();
    });
});
//-->
</script>
</head>
<body contextPath="${contextPath}" workspace="${workspace}">
<div>
    <div class="finder-nav">
        <div class="menu-head"><h4>系统选项</h4></div>
        <div id="" class="menu-body" style="margin-top: 10px; white-space: nowrap; overflow: auto;">
            <ul class="menu">
                <li class="active"><a href="javascript:void(0)">全局设置</a></li>
            </ul>
        </div>
    </div>
    <div style="margin-left: 220px;">
        <div id="finder-panel" class="form">
            <div class="menu-panel"><h4>全局设置</h4></div>
            <div class="form-row">
                <div class="form-label">主题：</div>
                <div class="form-c300">
                    <div class="form-field">
                        <select name="theme">
                            <option value="Django">Django</option>
                            <option value="Eclipse">Eclipse</option>
                            <option value="Emacs">Emacs</option>
                            <option value="FadeToGrey">FadeToGrey</option>
                            <option value="MDUltra">MDUltra</option>
                            <option value="Midnight">Midnight</option>
                            <option value="RDark">RDark</option>
                        </select>
                    </div>
                </div>
                <div class="form-m300">
                    <div class="form-comment">主题。</div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label">字符集：</div>
                <div class="form-c300">
                    <div class="form-field"><select name="charset"></select></div>
                </div>
                <div class="form-m300">
                    <div class="form-comment">字符集。</div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label">字体：</div>
                <div class="form-c300">
                    <div class="form-field">
                        <select name="fontFamily">
                            <option value="Andale Mono">Andale Mono</option>
                            <option value="Anonymous">Anonymous</option>
                            <option value="Consolas">Consolas</option>
                            <option value="Courier New">Courier New</option>
                            <option value="Dejavu">Dejavu</option>
                            <option value="DroidMono">DroidMono</option>
                            <option value="EnvyR">EnvyR</option>
                            <option value="FiraCode">FiraCode</option>
                            <option value="Hasklig">Hasklig</option>
                            <option value="InputMono">InputMono</option>
                            <option value="liberation">liberation</option>
                            <option value="Lucida Console">Lucida Console</option>
                            <option value="LucidaMono">LucidaMono</option>
                            <option value="Monaco">Monaco</option>
                            <option value="Monoid">Monoid</option>
                            <option value="Profont">Profont</option>
                            <option value="SourceCode">SourceCode</option>
                            <option value="UbuntuMono">UbuntuMono</option>
                        </select>
                    </div>
                </div>
                <div class="form-m300">
                    <div id="fontExample">我是字体示例 abcd ABCD 123</div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label">字体颜色：</div>
                <div class="form-c300">
                    <div class="form-field">
                        <input name="fontColor" type="text" class="color-text" value=""/>
                        <input name="fontColorPicker" type="text" class="color-picker" readonly="true" value=""/>
                    </div>
                </div>
                <div class="form-m300">
                    <div class="form-comment">字体颜色。</div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-label">背景颜色：</div>
                <div class="form-c300">
                    <div class="form-field">
                        <input name="backgroundColor" type="text" class="color-text" value=""/>
                        <input name="backgroundColorPicker" type="text" class="color-picker" readonly="true" value=""/>
                    </div>
                </div>
                <div class="form-m300">
                    <div class="form-comment">背景颜色。</div>
                </div>
            </div>
            <div class="button">
                <button id="default-btn" class="button">恢复默认</button>
                <button id="ensure-btn" class="button ensure">确 定</button>
                <button id="cancel-btn" class="button cancel">取 消 </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
