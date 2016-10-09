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
    var volume = (localStorage.volume || 0.5);
    var mode = (localStorage.mode || 1);
    var ul = jQuery(container).find("div.play-list ul");

    parent.css("border", "1px solid #000000");
    parent.find("div.title").css("backgroundColor", "#333333");
    parent.find("div.title").css("borderBottom", "1px dashed #666666");
    parent.find("div.title h4").css("color", "#999999");

    parent.find("div.title span.close").click(function() {
        self.close();
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
    var container = this.getContainer();
    var parent = jQuery(container);
    var ul = jQuery(container).find("div.play-list ul").html("");

    this.playList = playList;
    this.playList.each(function(item) {
        ul.append("<li track=\"" + track + "\">" + item.title + "</li>");
        track++;
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
        audio.play();
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
    var track = this.playList.next();
    this.play(track);
};

AudioPlayer.prototype.shuffle = function() {
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

    jQuery(container).find(".playback").removeClass("playing");
    audio.pause();
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

AudioPlayer.prototype.getContainer = function() {
    return document.getElementById(this.id);
};

AudioPlayer.prototype.getAudio = function() {
    return jQuery("#" + this.id + " audio").get(0);
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

    parent.css("border", "1px solid #000000");
    parent.find("div.title").css("backgroundColor", "#333333");
    parent.find("div.title").css("borderBottom", "1px dashed #666666");
    parent.find("div.title h4").css("color", "#999999");

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
        console.log("fullscreen");
        self.fullscreen();
    });

    var onload = function() {
        var c = jQuery(self.getContainer());
        var endValue = (this.seekable && this.seekable.length) ? this.seekable.end(0) : 0;
        c.find(".progress .loaded").css("width", (100 / (this.duration || 1) * endValue) + "%");
    };

    video.addEventListener("progress", onload, false);
    video.addEventListener("durationchange", onload, false);
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
    Dragable.registe(parent.find("video").get(0), container);
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

VideoPlayer.prototype.fullscreen = function() {
    Fullscreen.toggle(this.getVideo());
};

VideoPlayer.prototype.getContainer = function() {
    return document.getElementById(this.id);
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