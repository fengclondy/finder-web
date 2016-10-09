(function() {
    Finder.getPlayList = function() {
        if(window != window.parent) {
            return new window.parent.PlayList();
        }
        else {
            return new window.PlayList();
        }
    };

    Finder.getAudioPlayer = function() {
        if(this.audioPlayer == null) {
            if(window != window.parent) {
                if(window.parent.audioPlayer != null) {
                    this.audioPlayer = window.parent.audioPlayer;
                }
                else {
                    this.audioPlayer = new window.parent.AudioPlayer({"container": "finder-audiodialog", "title": "AudioPlayer"});
                    window.parent.audioPlayer = this.audioPlayer;
                }
            }
            else {
                this.audioPlayer = new AudioPlayer({"container": "finder-audiodialog"});
            }
        }
        return this.audioPlayer;
    };

    Finder.getVideoPlayer = function() {
        if(this.videoPlayer == null) {
            if(window != window.parent) {
                if(window.parent.videoPlayer != null) {
                    this.videoPlayer = window.parent.videoPlayer;
                }
                else {
                    this.videoPlayer = new window.parent.VideoPlayer({"container": "finder-videodialog", "title": "VideoPlayer"});
                    window.parent.videoPlayer = this.videoPlayer;
                }
            }
            else {
                this.videoPlayer = new VideoPlayer({"container": "finder-videodialog"});
            }
        }
        return this.videoPlayer;
    };

    FileType.registe("mp3", function(file, options) {
        var index = 0;
        var workspace = Finder.getWorkspace();
        var path = Finder.getPath();
        var fileList = Finder.getFileList();
        var prefix = Finder.getContextPath() + "/finder/download.html?workspace=" + encodeURIComponent(workspace);
        var cover = Finder.getContextPath() + "/resource/finder/images/hua.jpg";
        var playList = Finder.getPlayList();

        for(var i = 0; i < fileList.length; i++) {
            var fileName = fileList[i].fileName;
            var fileType = FileType.getType(fileName);

            if(FileType.contains("mp3", fileType)) {
                if(fileName == file.fileName) {
                    index = playList.size();
                }
                playList.add({"title": fileName, "url": prefix + "&path=" + encodeURIComponent(path + "/" + fileName), "cover": cover});
            }
        }

        var audioPlayer = Finder.getAudioPlayer();
        audioPlayer.setPlayList(playList);
        audioPlayer.play(index);
        audioPlayer.open();
        return true;
    });

    var openWindow = function(url, name, width, height, features){
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

    FileType.registe("mp4, mov, rmvb, mkv", function(file, options) {
        var index = 0;
        var workspace = Finder.getWorkspace();
        var path = Finder.getPath();
        var fileList = Finder.getFileList();
        var prefix = Finder.getContextPath() + "/finder/download.html?workspace=" + encodeURIComponent(workspace);
        var cover = Finder.getContextPath() + "/resource/finder/images/hua.jpg";
        var playList = Finder.getPlayList();

        for(var i = 0; i < fileList.length; i++) {
            var fileName = fileList[i].fileName;
            var fileType = FileType.getType(fileName);

            if(FileType.contains("mp4, mov", fileType)) {
                if(fileName == file.fileName) {
                    index = playList.size();
                }
                playList.add({"title": fileName, "url": prefix + "&path=" + encodeURIComponent(path + "/" + fileName), "cover": cover});
            }
        }

        openWindow(Finder.getContextPath() + "/finder/play.html?workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path + "/" + file.fileName), "_blank", 800, 600);
        return;
        var videoPlayer = Finder.getVideoPlayer();
        videoPlayer.setPlayList(playList);
        videoPlayer.play(index);
        videoPlayer.open();
        return true;
    });
})();

(function() {
    var win = null;
    var doc = null;
    var plugin = Finder.getPlugin();
    var home = plugin.getHome();

    if(window != window.parent) {
        win = window.parent;
        doc = window.parent.document;
    }
    else {
        win = window;
        doc = window.document;
    }

    if(win.PluginManager == null) {
        win.PluginManager = {};
    }

    if(win.PluginManager.media == null) {
        win.PluginManager.media = "media";
        ResourceLoader.css(doc, home + "/media/css/media.css");
        ResourceLoader.script(doc, home + "/media/index.js");
    }
})();
