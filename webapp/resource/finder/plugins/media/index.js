if(typeof(localStorage) == "undefined") {
    localStorage = {};
}

window.AudioContext = (window.AudioContext || window.webkitAudioContext || window.mozAudioContext || window.msAudioContext);
window.requestAnimationFrame = (window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.msRequestAnimationFrame);
window.cancelAnimationFrame = (window.cancelAnimationFrame || window.webkitCancelAnimationFrame || window.mozCancelAnimationFrame || window.msCancelAnimationFrame);

var PlayList = function() {
    this.track = 0;
    this.items = [];
};

PlayList.prototype.add = function(item) {
    this.items[this.items.length] = item;
};

PlayList.prototype.get = function(track) {
    if(track >= 0 && track < this.items.length) {
        return this.items[track];
    }
    else {
        return null;
    }
};

PlayList.prototype.prev = function() {
    this.track--;

    if(this.track < 0) {
        this.track = this.item.length - 1;
    }
    return this.track;
};

PlayList.prototype.next = function() {
    this.track++;

    if(this.track >= this.items.length) {
        this.track = 0;
    }
    return this.track;
};

PlayList.prototype.shuffle = function() {
    this.track = (new Date().getTime() % this.items.length);
    return this.track;
};

PlayList.prototype.setTrack = function(track) {
    this.track = track;
};

PlayList.prototype.getTrack = function(track) {
    return this.track;
};

PlayList.prototype.remove = function(track) {
    if(this.items.length > 0) {
        var items = this.items;

        for(var i = track; i < items.length - 1; i++) {
            items[i] = items[i + 1];
        }
        items.length = (items.length - 1);
    }
};

PlayList.prototype.each = function(handler) {
    for(var i = 0; i < this.items.length; i++) {
        var flag = handler(this.items[i]);

        if(flag == false) {
            break;
        }
    }
};

PlayList.prototype.size = function(handler) {
    return this.items.length;
};

var AudioPlayer = com.skin.framework.Class.create(Dialog, function() {
});

AudioPlayer.prototype.create = function() {
    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    var audio = this.getAudio();
    var audioContext = this.getAudioContext();
    var volume = (localStorage.volume || 0.5);
    var mode = (localStorage.mode || 1);
    var ul = jQuery(container).find("div.play-list ul");
    parent.attr("class", "media-dialog");
    parent.css("width", "380px");
    parent.css("height", "540px");

    parent.find("div.title span.min").click(function() {
        self.hide();
        TaskManager.add(self);
    });

    parent.find("div.title span.close").click(function() {
        self.pause();
        self.close();
        TaskManager.remove(self);
    });

    parent.find(".rewind").click(function() {
        self.prev();
    });

    parent.find(".playback").click(function() {
        if(jQuery(this).hasClass("playing")) {
            self.pause();
        }
        else {
            self.play();
        }
    });

    parent.find(".fastforward").click(function() {
        self.next();
    });

    parent.find(".repeat, .shuffle").click(function() {
        var src = jQuery(this);
        var mode = parseInt(src.attr("mode"));
        self.setMode(mode);
    });

    parent.find("div.play-list ul").dblclick(function(event) {
        var src = (event.target || event.srcElement);
        var li = jQuery(src).closest("li");
        var track = li.attr("track");

        jQuery(src).closest("ul").find("li").removeClass("playing");
        li.addClass("playing");
        self.play(parseInt(track));
    });

    parent.find(".mute").click(function(){
        var src = jQuery(this);

        if(src.hasClass("enable")){
            self.setVolume(src.data("volume"));
            src.removeClass("enable");
        }
        else {
            src.data("volume", audio.volume).addClass("enable");
            self.setVolume(0);
        }
    });

    parent.find(".progress .slider").click(function(event) {
        var src = jQuery(this);
        var x = event.offsetX;
        var width = src.width();
        var percent = (x / width);
        var currentTime = Math.floor(audio.duration * percent);

        self.setProgress(currentTime);
        audio.currentTime = currentTime;
    });

    parent.find(".volume .slider").click(function() {
        var src = jQuery(this);
        var x = event.offsetX;
        var width = src.width();
        self.setVolume((x / width));
    });

    this.addShortcut("ESC", function() {
        self.pause();
        self.close();
    });

    this.addShortcut("ENTER | BACKSPACE", function() {
        self.pause();
    });

    var onload = function() {
        var c = jQuery(self.getContainer());
        var endValue = (this.seekable && this.seekable.length) ? this.seekable.end(0) : 0;
        c.find(".progress .loaded").css("width", (100 / (this.duration || 1) * endValue) + "%");
    };

    var source = audioContext.createMediaElementSource(audio);
    this.analyser = audioContext.createAnalyser();

    source.connect(this.analyser);
    source.connect(audioContext.destination);

    audio.addEventListener("canplay", function() {
        var volume = (localStorage.volume || 0.5);
        self.setVolume(volume);
    }, false);

    audio.addEventListener("progress", onload, false);
    audio.addEventListener("durationchange", onload, false);
    audio.addEventListener("ended", function() {
        if(self.mode == 1) {
            self.next();
        }
        else if(self.mode == 2) {
            self.shuffle();
        }
        else {
            self.replay();
        }
    }, false);

    this.setMode(mode);
    this.setVolume(volume);
    Dragable.registe(parent.find("div.title").get(0), container);
};

AudioPlayer.prototype.getContent = function() {
    var buffer = [];
    buffer[buffer.length] = "<div class=\"title\">";
    buffer[buffer.length] = "  <h4>Finder - Media Player</h4>";
    buffer[buffer.length] = "  <span class=\"close\" dragable=\"false\"></span>";
    buffer[buffer.length] = "  <span class=\"min\" dragable=\"false\"></span>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"audio-player\">";
    buffer[buffer.length] = "  <div class=\"tag\">";
    buffer[buffer.length] = "    <div style=\"float: left; width: 240px; height: 60px; font-weight: bold;\">";
    buffer[buffer.length] = "      <div style=\"clear: both; padding: 10px 4px 4px 4px; overflow: hidden;\">";
    buffer[buffer.length] = "        <span class=\"lcd n0\"></span><span class=\"lcd n8\"></span><span class=\"lcd colon\"></span></span><span class=\"lcd n8\"></span><span class=\"lcd n8\"></span>";
    buffer[buffer.length] = "      </div>";
    buffer[buffer.length] = "      <div class=\"caption\" style=\"padding: 10px 4px 4px 4px; width: 220px; white-space:nowrap; text-overflow: ellipsis; font-weight: bold; overflow: hidden;\"></div>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "    <div style=\"float: left; height: 60px; text-align: right;\"><canvas width=\"120\" height=\"60\"></canvas></div>";
    buffer[buffer.length] = "  </div>";
    buffer[buffer.length] = "  <div class=\"ctrl\">";
    buffer[buffer.length] = "    <div class=\"control\">";
    buffer[buffer.length] = "      <div class=\"left\">";
    buffer[buffer.length] = "        <div class=\"rewind icon\"></div>";
    buffer[buffer.length] = "        <div class=\"playback icon\"></div>";
    buffer[buffer.length] = "        <div class=\"fastforward icon\"></div>";
    buffer[buffer.length] = "  </div>";
    buffer[buffer.length] = "<div class=\"right volume\">";
    buffer[buffer.length] = "<div class=\"left mute icon\"></div>";
    buffer[buffer.length] = "<div class=\"slider left\">";
    buffer[buffer.length] = "<div class=\"pace\"></div>";
    buffer[buffer.length] = "<a class=\"dot\" href=\"#\" style=\"left: 0%;\"></a>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"progress\">";
    buffer[buffer.length] = "<div class=\"slider\">";
    buffer[buffer.length] = "<div class=\"loaded\"></div>";
    buffer[buffer.length] = "<div class=\"pace\"></div>";
    buffer[buffer.length] = "<a class=\"dot\" href=\"#\" style=\"left: 0%;\"></a>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"timer left\">00:00</div>";
    buffer[buffer.length] = "<div class=\"right\">";
    buffer[buffer.length] = "<div class=\"repeat icon\" mode=\"1\" title=\"列表循环\"></div>";
    buffer[buffer.length] = "<div class=\"shuffle icon\" mode=\"2\" title=\"随机播放\"></div>";
    buffer[buffer.length] = "<div class=\"repeat once icon\" mode=\"3\" title=\"单曲循环\"></div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"play-list\"><ul></ul></div>";
    buffer[buffer.length] = "</div>";
    return buffer.join("");
};

AudioPlayer.prototype.setActiveStyle = function(active) {
    var c = this.getContainer();

    if(c != null) {
        if(active == true) {
            c.style.zIndex = DialogManager.getZIndex();
        }
        else {
            c.style.zIndex = this.zIndex;
        }
    }
};

AudioPlayer.prototype.setPlayList = function(playList) {
    var track = 0;
    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    var ul = jQuery(container).find("div.play-list ul").html("");

    this.playList = playList;
    this.playList.each(function(item) {
        ul.append("<li track=\"" + track + "\" title=\"" + item.title + "\"><span class=\"title\">" + item.title + "</span><span class=\"remove\">删除</span></li>");
        track++;
    });

    parent.find("div.play-list ul li span.remove").click(function() {
        var li = jQuery(this).closest("li");
        self.remove(li.attr("track"));
    });
};

AudioPlayer.prototype.play = function(track) {
    var self = this;
    var audio = this.getAudio();
    var container = this.getContainer();

    if(this.timer == null) {
        clearInterval(this.timer);
    }

    this.timer = setInterval(function() {
        var audio = self.getAudio();
        self.setProgress(audio.currentTime);
    }, 500);

    if(track == null || track == undefined) {
        jQuery(container).find(".playback").addClass("playing");
        this.visualize();
        audio.play();
        return;
    }

    var item = this.playList.get(track);

    if(item == null) {
        return;
    }

    this.playList.setTrack(track);
    jQuery(container).find(".cover").html("<img src=\"" +item.cover + "\" alt=\"" + (item.album || "") + "\">");
    jQuery(container).find(".tag div.caption").html(item.title);
    jQuery(container).find(".playback").addClass("playing");
    jQuery(container).find(".play-list li").removeClass("playing").eq(track).addClass("playing");

    this.title = item.title;
    this.icon = "/resource/finder/plugins/media/images/audio.gif";
    TaskManager.setTitle(this);

    this.visualize();
    audio.volume = jQuery(container).find(".mute").hasClass("enable") ? 0 : 1;
    audio.src = item.url;
    audio.play();
};

AudioPlayer.prototype.replay = function() {
    var track = this.playList.getTrack();
    this.play(track);
};

AudioPlayer.prototype.prev = function() {
    var track = this.playList.prev();
    this.play(track);
};

AudioPlayer.prototype.next = function() {
    console.log("next - this.mode: " + this.mode + " - track: " + this.playList.getTrack());
    var track = this.playList.next();
    this.play(track);
};

AudioPlayer.prototype.shuffle = function() {
    console.log("shuffle - this.mode: " + this.mode + " - track: " + this.playList.getTrack());
    var track = this.playList.shuffle();
    this.play(track);
};

AudioPlayer.prototype.pause = function() {
    var audio = this.getAudio();
    var container = this.getContainer();

    if(this.timer == null) {
        clearInterval(this.timer);
        this.timer = null;
    }

    this.status = 0;
    jQuery(container).find(".playback").removeClass("playing");
    audio.pause();
};

AudioPlayer.prototype.remove = function(track) {
    var index = parseInt(track);

    if(isNaN(index)) {
        return;
    }

    var current = this.playList.getTrack();
    var parent = jQuery(this.getContainer());
    parent.find("div.play-list ul li[track=" + track + "]").remove();
    this.playList.remove(index);

    var i = 0;
    parent.find("div.play-list ul li").each(function() {
        this.setAttribute("track", i);
        i++;
    });

    if(current > 0) {
        this.playList.setTrack(current - 1);
    }
};

AudioPlayer.prototype.setMode = function(mode) {
    var parent = jQuery(this.getContainer());
    this.mode = localStorage.mode = mode;
    parent.find(".repeat, .shuffle").removeClass("enable");
    parent.find(".repeat, .shuffle").filter("[mode=" + mode + "]").addClass("enable");
};

AudioPlayer.prototype.getSeconds = function(value) {
    var minutes = Math.floor(value / 60);
    var seconds = Math.floor(value % 60);

    if(minutes < 10) {
        minutes = "0" + minutes;
    }

    if(seconds < 10) {
        seconds = "0" + seconds;
    }
    return minutes + ":" + seconds;
};

AudioPlayer.prototype.setProgress = function(value) {
    var audio = this.getAudio();
    var container = this.getContainer();
    var time = this.getSeconds(value);
    var totalTime = this.getSeconds((audio.duration || 0));
    var ratio = value / audio.duration * 100;

    var i = 0;
    jQuery(container).find("span.lcd").each(function() {
        if(i < 5) {
            if(i != 2) {
                jQuery(this).attr("class", "lcd n" + time.charAt(i));
            }
            i++;
        }
    });

    jQuery(container).find(".timer").html(time + "/" + totalTime);
    jQuery(container).find(".progress .pace").css("width", ratio + "%");
    jQuery(container).find(".progress .slider a").css("left", ratio + "%");
};

AudioPlayer.prototype.setVolume = function(value) {
    var audio = this.getAudio();
    var container = this.getContainer();
    var parent = jQuery(container);

    parent.find(".volume .pace").css("width", value * 100 + "%");
    parent.find(".volume .slider a").css("left", value * 100 + "%");
    audio.volume = localStorage.volume = value;
};

AudioPlayer.prototype.visualize = function() {
    this.status = 1;

    if(this.animationId !== null) {
        cancelAnimationFrame(this.animationId);
    }
    this.drawSpectrum(this.analyser);
};

AudioPlayer.prototype.drawSpectrum = function(analyser) {
    var self = this;
    var canvas = this.getCanvas();
    var canvasWidth = canvas.width;
    var canvasHeight = canvas.height;

    var gap = 2;
    var meterWidth = 3;
    var capHeight = 2;
    var capStyle = "#ffffff";
    var meterNum = Math.floor(canvasWidth / (meterWidth + gap));
    var capYPositionArray = [];
    var ctx = canvas.getContext("2d");
    var gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(1,   "#00ff00");
    gradient.addColorStop(0.5, "#ffff00");
    gradient.addColorStop(0,   "#00ff00");

    console.log("canvasWidth: " + canvasWidth + ", canvasHeight: " + canvasHeight + ", meterNum: " + meterNum);

    var draw = function() {
        var buffer = new Uint8Array(analyser.frequencyBinCount);
        analyser.getByteFrequencyData(buffer);

        if(self.status === 0) {
            /**
             * 直接返回保持当前状态
             */
            // cancelAnimationFrame(self.animationId);
            // return;

            /*
            for(var i = buffer.length - 1; i >= 0; i--) {
                buffer[i] = 0;
            };
            */

            var allCapsReachBottom = true;

            for(var i = capYPositionArray.length - 1; i >= 0; i--) {
                allCapsReachBottom = self.allCapsReachBottom && (capYPositionArray[i] == 0);
            };

            if(allCapsReachBottom) {
                cancelAnimationFrame(self.animationId);
                return;
            };
        };

        /**
         * 只取前面的80%数据, 后面的20%数据基本没什么变化
         */
        var step = Math.floor(buffer.length * 0.6 / meterNum);
        ctx.clearRect(0, 0, canvasWidth, canvasHeight);

        for(var i = 0; i < meterNum; i++) {
            var y = 0;
            var x = i * (meterWidth + gap);
            var value = buffer[i * step];

            if(capYPositionArray.length <= i) {
                capYPositionArray.push(value);
            }

            ctx.fillStyle = capStyle;

            if(value < capYPositionArray[i]) {
                capYPositionArray[i] = capYPositionArray[i] - 1;
                y = 255 - capYPositionArray[i];
                y = Math.floor(y / 255 * canvasHeight) - capHeight;
                ctx.fillRect(x, y, meterWidth, capHeight);
            }
            else {
                y = 255 - value;
                y = Math.floor(y / 255 * canvasHeight) - capHeight;
                ctx.fillRect(x, y, meterWidth, capHeight);
                capYPositionArray[i] = value;
            }

            y = 255 - value + capHeight;
            y = Math.floor(y / 255 * canvasHeight);

            ctx.fillStyle = gradient;
            ctx.fillRect(x, y, meterWidth, value);
        }
        self.animationId = window.requestAnimationFrame(draw);
    }
    this.animationId = requestAnimationFrame(draw);
};

AudioPlayer.prototype.getAudio = function() {
    if(this.audio == null) {
        this.audio = new Audio();
    }
    return this.audio;
};

AudioPlayer.prototype.getCanvas = function() {
    return jQuery("#" + this.id + " canvas").get(0);
};

AudioPlayer.prototype.getAudioContext = function() {
    if(this.audioContext == null) {
        this.audioContext = new AudioContext();
    }
    return this.audioContext;
};

var VideoPlayer = com.skin.framework.Class.create(Dialog, function() {
});

VideoPlayer.prototype.create = function() {
    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    var video = this.getVideo();
    var volume = (localStorage.volume || 0.5);
    var mode = (localStorage.mode || 1);
    var ul = jQuery(container).find("div.play-list ul");

    parent.attr("class", "media-dialog");
    parent.css("width", "533px");

    parent.find("div.title span.min").click(function() {
        self.hide();
        TaskManager.add(self);
    });

    parent.find("div.title span.close").click(function() {
        self.pause();
        self.close();
        TaskManager.remove(self);
    });

    parent.find(".rewind").click(function() {
        self.prev();
    });

    parent.find(".playback").click(function() {
        if(jQuery(this).hasClass("playing")) {
            self.pause();
        }
        else {
            self.play();
        }
    });

    parent.find(".fastforward").click(function() {
        self.next();
    });

    parent.find(".repeat, .shuffle").click(function() {
        var src = jQuery(this);
        var mode = parseInt(src.attr("mode"));
        self.setMode(mode);
    });

    parent.find(".fullscreen").click(function() {
        self.fullscreen();
    });

    parent.find("div.play-list ul").dblclick(function(event) {
        var src = (event.target || event.srcElement);
        var li = jQuery(src).closest("li");
        var track = li.attr("track");

        jQuery(src).closest("ul").find("li").removeClass("playing");
        li.addClass("playing");
        self.play(parseInt(track));
    });

    parent.find(".mute").click(function(){
        var src = jQuery(this);

        if(src.hasClass("enable")){
            self.setVolume(src.data("volume"));
            src.removeClass("enable");
        }
        else {
            src.data("volume", video.volume).addClass("enable");
            self.setVolume(0);
        }
    });

    parent.find(".progress .slider").click(function(event) {
        var src = jQuery(this);
        var x = event.offsetX;
        var width = src.width();
        var percent = (x / width);
        var currentTime = Math.floor(video.duration * percent);

        if(isNaN(currentTime)) {
            return;
        }

        self.setProgress(currentTime);
        video.currentTime = currentTime;
    });

    parent.find(".volume .slider").click(function() {
        var src = jQuery(this);
        var x = event.offsetX;
        var width = src.width();
        self.setVolume((x / width));
    });

    this.addShortcut("ESC", function() {
        self.pause();
        self.close();
    });

    this.addShortcut("ALT + ENTER", function() {
        self.fullscreen();
    });

    this.addShortcut("ENTER | BLANKSPACE", function() {
        if(video.paused) {
            self.play();
        }
        else {
            self.pause();
        }
    });

    this.addShortcut("LEFT", function() {
        var currentTime = video.currentTime;

        if(currentTime > 5) {
            video.currentTime = currentTime - 5;
        }
        else {
            video.currentTime = 0;
        }
    });

    this.addShortcut("RIGHT", function() {
        var currentTime = video.currentTime;
        video.currentTime = currentTime + 5;
    });

    var timer = null;
    var onload = function() {
        var c = jQuery(self.getContainer());
        var endValue = (this.seekable && this.seekable.length) ? this.seekable.end(0) : 0;
        c.find(".progress .loaded").css("width", (100 / (this.duration || 1) * endValue) + "%");
    };

    video.addEventListener("mousemove", function() {
        var src = this;
        src.style.cursor = "auto";

        if(timer != null) {
            clearTimeout(timer);
        }

        timer = setTimeout(function() {
            src.style.cursor = "none";
        }, 3000);
    }, false);

    video.addEventListener("dblclick", function() {
        self.fullscreen();
    }, false);

    video.addEventListener("canplay", function() {
        var volume = (localStorage.volume || 0.5);
        self.setVolume(volume);
    }, false);

    video.addEventListener("progress", onload, false);
    video.addEventListener("durationchange", onload, false);
    video.addEventListener("playing", function() {
        self.waiting(0);
    }, false);

    video.addEventListener("waiting", function() {
        self.waiting(1);
    }, false);

    video.addEventListener("ended", function() {
        if(self.mode == 1) {
            self.next();
        }
        else if(self.mode == 2) {
            self.shuffle();
        }
        else {
            self.replay();
        }
    }, false);

    video.addEventListener("error", function() {
        if(self.mode == 1) {
            self.next();
        }
        else if(self.mode == 2) {
            self.shuffle();
        }
        else {
            self.replay();
        }
    }, false);

    this.setMode(mode);
    this.setVolume(volume);
    Dragable.registe(parent.find("div.title").get(0), container);
};

VideoPlayer.prototype.getContent = function() {
    var buffer = [];
    buffer[buffer.length] = "<div class=\"title\">";
    buffer[buffer.length] = "  <h4>Finder - Media Player</h4>";
    buffer[buffer.length] = "  <span class=\"close\" dragable=\"false\"></span>";
    buffer[buffer.length] = "  <span class=\"min\" dragable=\"false\"></span>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"video-player\">";
    buffer[buffer.length] = "  <div style=\"position: relative;\">";
    buffer[buffer.length] = "    <video width=\"533\" height=\"300\" webkit-playsinline=\"true\">您的浏览器不支持视频播放。</video>";
    buffer[buffer.length] = "    <div class=\"loading\"></div>";
    buffer[buffer.length] = "  </div>";
    buffer[buffer.length] = "<div style=\"padding: 4px 8px 0px 8px;\">";
    buffer[buffer.length] = "<div class=\"ctrl\">";
    buffer[buffer.length] = "<div class=\"tag\" style=\"display: none;\">";
    buffer[buffer.length] = "<strong>Title</strong>";
    buffer[buffer.length] = "<span class=\"artist\">Artist</span>";
    buffer[buffer.length] = "<span class=\"album\">Album</span>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"control\">";
    buffer[buffer.length] = "<div class=\"left\">";
    buffer[buffer.length] = "<div class=\"rewind icon\"></div>";
    buffer[buffer.length] = "<div class=\"playback icon\"></div>";
    buffer[buffer.length] = "<div class=\"fastforward icon\"></div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"right volume\">";
    buffer[buffer.length] = "<div class=\"left mute icon\"></div>";
    buffer[buffer.length] = "<div class=\"slider left\">";
    buffer[buffer.length] = "<div class=\"pace\"></div>";
    buffer[buffer.length] = "<a class=\"dot\" href=\"#\" style=\"left: 0%;\"></a>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"progress\">";
    buffer[buffer.length] = "<div class=\"slider\">";
    buffer[buffer.length] = "<div class=\"loaded\"></div>";
    buffer[buffer.length] = "<div class=\"pace\"></div>";
    buffer[buffer.length] = "<a class=\"dot\" href=\"#\" style=\"left: 0%;\"></a>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"timer left\">00:00</div>";
    buffer[buffer.length] = "<div class=\"right\">";
    buffer[buffer.length] = "<div class=\"repeat icon\" mode=\"1\" title=\"列表循环\"></div>";
    buffer[buffer.length] = "<div class=\"shuffle icon\" mode=\"2\" title=\"随机播放\"></div>";
    buffer[buffer.length] = "<div class=\"repeat once icon\" mode=\"3\" title=\"单曲循环\"></div>";
    buffer[buffer.length] = "<div class=\"fullscreen icon\" title=\"全屏\"></div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"play-list\" style=\"display: none;\"><ul></ul></div>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</div>";
    return buffer.join("");
};

VideoPlayer.prototype.setActiveStyle = function(active) {
    var c = this.getContainer();

    if(c != null) {
        if(active == true) {
            c.style.zIndex = DialogManager.getZIndex();
        }
        else {
            c.style.zIndex = this.zIndex;
        }
    }
};

VideoPlayer.prototype.setPlayList = function(playList) {
    var track = 0;
    var container = this.getContainer();
    var parent = jQuery(container);
    var ul = jQuery(container).find("div.play-list ul").html("");

    this.playList = playList;
    this.playList.each(function(item) {
        ul.append("<li track=\"" + track + "\">" + item.title + "</li>");
        track++;
    });
};

VideoPlayer.prototype.play = function(track) {
    var self = this;
    var video = this.getVideo();
    var container = this.getContainer();

    if(this.timer == null) {
        clearInterval(this.timer);
    }

    this.timer = setInterval(function() {
        var video = self.getVideo();
        self.setProgress(video.currentTime);
    }, 500);

    if(track == null || track == undefined) {
        jQuery(container).find(".playback").addClass("playing");
        video.play();
        return;
    }

    var item = this.playList.get(track);

    if(item == null) {
        return;
    }

    this.playList.setTrack(track);
    jQuery(container).find(".cover").html("<img src=\"" +item.cover + "\" alt=\"" + (item.album || "") + "\">");
    jQuery(container).find(".tag").html("<strong>" + item.title + "</strong>");
    jQuery(container).find(".playback").addClass("playing");
    jQuery(container).find(".play-list li").removeClass("playing").eq(track).addClass("playing");

    this.title = item.title;
    this.icon = "/resource/finder/plugins/media/images/video.gif";
    TaskManager.setTitle(this);

    video.volume = jQuery(container).find(".mute").hasClass("enable") ? 0 : 1;
    video.src = item.url;
    video.play();
};

VideoPlayer.prototype.replay = function() {
    var track = this.playList.getTrack();
    this.play(track);
};

VideoPlayer.prototype.prev = function() {
    var track = this.playList.prev();
    this.play(track);
};

VideoPlayer.prototype.next = function() {
    var track = this.playList.next();
    this.play(track);
};

VideoPlayer.prototype.shuffle = function() {
    var track = this.playList.shuffle();
    this.play(track);
};

VideoPlayer.prototype.pause = function() {
    var video = this.getVideo();
    var container = this.getContainer();

    if(this.timer == null) {
        clearInterval(this.timer);
        this.timer = null;
    }

    jQuery(container).find(".playback").removeClass("playing");
    video.pause();
};

VideoPlayer.prototype.setMode = function(mode) {
    var parent = jQuery(this.getContainer());
    this.mode = localStorage.mode = mode;
    parent.find(".repeat, .shuffle").removeClass("enable");
    parent.find(".repeat, .shuffle").filter("[mode=" + mode + "]").addClass("enable");
};

VideoPlayer.prototype.getSeconds = function(value) {
    var minutes = Math.floor(value / 60);
    var seconds = Math.floor(value % 60);

    if(minutes < 10) {
        minutes = "0" + minutes;
    }

    if(seconds < 10) {
        seconds = "0" + seconds;
    }
    return minutes + ":" + seconds;
};

VideoPlayer.prototype.setProgress = function(value) {
    var video = this.getVideo();
    var container = this.getContainer();
    var time = this.getSeconds(value);
    var totalTime = this.getSeconds((video.duration || 0));
    var ratio = value / video.duration * 100;

    jQuery(container).find(".timer").html(time + "/" + totalTime);
    jQuery(container).find(".progress .pace").css("width", ratio + "%");
    jQuery(container).find(".progress .slider a").css("left", ratio + "%");
};

VideoPlayer.prototype.setVolume = function(value) {
    var video = this.getVideo();
    var container = this.getContainer();
    var parent = jQuery(container);

    parent.find(".volume .pace").css("width", value * 100 + "%");
    parent.find(".volume .slider a").css("left", value * 100 + "%");
    video.volume = localStorage.volume = value;
};

VideoPlayer.prototype.waiting = function(loading) {
    var container = this.getContainer();
    var parent = jQuery(container);

    if(loading == 1) {
        parent.find(".loading").show();
    }
    else {
        parent.find(".loading").hide();
    }
};

VideoPlayer.prototype.fullscreen = function() {
    Fullscreen.toggle(this.getVideo());
};

VideoPlayer.prototype.getVideo = function() {
    return jQuery("#" + this.id + " video").get(0);
};

var Fullscreen = {};

Fullscreen.toggle = function(element) {
    var fullscreen = element.getAttribute("fullscreen");

    if(fullscreen != "true") {
        this.open(element);
    }
    else {
        this.exit(element);
    }
};

Fullscreen.open = function(element) {
    if(element.requestFullscreen) {
        element.requestFullscreen();
    }
    else if(element.mozRequestFullScreen) {
        element.mozRequestFullScreen();
    }
    else if(element.msRequestFullscreen){
        element.msRequestFullscreen();  
    }
    else if(element.oRequestFullscreen){
        element.oRequestFullscreen();
    }
    else if(element.webkitRequestFullscreen) {
        element.webkitRequestFullScreen();
    }
    else {
        element.style.width = "100%";
        element.style.height = "100%";
        element.style.overflow = "hidden";
    }
    element.setAttribute("fullscreen", "true");
};

Fullscreen.exit = function(element) {
    if(document.exitFullscreen) {
        document.exitFullscreen();
    }
    else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    }
    else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
    }
    else if(document.oRequestFullscreen){
        document.oCancelFullScreen();
    }
    else if (document.webkitExitFullscreen){
        document.webkitExitFullscreen();
    }
    else {
        element.style.width = "auto";
        element.style.height = "auto";
        element.style.overflow = "auto";
    }
    element.setAttribute("fullscreen", "false");
};
