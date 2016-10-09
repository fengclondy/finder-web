(function() {
    var Ajax = {};

    Ajax.getXmlHttpRequest = function() {
        var xmlHttpRequest = null;

        if(window.ActiveXObject != null) {
            xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            xmlHttpRequest = new XMLHttpRequest();
        }
        return xmlHttpRequest;
    };

    Ajax.request = function(options) {
        var xmlHttpRequest = ((options.transport != null && options.transport != undefined) ? options.transport : this.getXmlHttpRequest());

        if(xmlHttpRequest == null || xmlHttpRequest == undefined) {
            if(options.error != null) {
                options.error(null);
            }
            return;
        }

        var url = options.url;
        var method = options.method;
        var headers = options.headers;
        xmlHttpRequest.open(method, options.url, (options.async == false ? false : true));

        xmlHttpRequest.onreadystatechange = function() {
            if(xmlHttpRequest.readyState == 4) {
                if(options.callback != null) {
                    options.callback(xmlHttpRequest);
                }
                else {
                    if(xmlHttpRequest.status == 0 || xmlHttpRequest.status == 200) {
                        if(options.success != null) {
                            options.success(xmlHttpRequest);
                        }
                    }
                    else if(xmlHttpRequest.status == 404 || xmlHttpRequest.status == 500) {
                        if(options.error != null) {
                            options.error(xmlHttpRequest);
                        }
                    }
                    else {
                        if(options.error != null) {
                            options.error(xmlHttpRequest);
                        }
                    }
                }
            }
        };

        xmlHttpRequest.ontimeout = options.ontimeout;
        xmlHttpRequest.onprogress = options.onprogress;
        xmlHttpRequest.upload.onprogress = options.onuploadprogress;

        if(headers != null) {
            for (var i = 0; i < headers.length; i++) {
                var header = headers[i];
                xmlHttpRequest.setRequestHeader(header.name, header.value);
            }
        }
        xmlHttpRequest.send(options.data);
        return xmlHttpRequest;
    };

    var ByteUtil = {
        getTotalSize: function(files) {
            var totalBytes = 0;
            for(var i = 0; i < files.length; i++) {
                totalBytes = (totalBytes + files[i].size);
            }
            return totalBytes;
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


    /**
     * MultipartUpload
     * @author weixian
     */
    var MultipartUpload = function() {
        this.partSize = 10 * 1024 * 1024;
        this.retryTimes = 3;
        this.status = "done";
        this.xmlHttpRequest = null;
    };

    MultipartUpload.prototype.getNextId = function() {
        if(this.sequence == null || this.sequence == undefined) {
            this.sequence = 1;
        }
        return "_seq_" + (this.sequence++);
    };

    MultipartUpload.prototype.getXmlHttpRequest = function() {
        if(this.xmlHttpRequest == null) {
            this.xmlHttpRequest = Ajax.getXmlHttpRequest();
        }
        return this.xmlHttpRequest;
    };

    MultipartUpload.prototype.addEventListener = function(target, type, handler) {
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

    MultipartUpload.prototype.removeEventListener = function(target, type, handler) {
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

    MultipartUpload.prototype.append = function(formData, data) {
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
                for (var j = 0; j < value.length; j++) {
                    if(value[j] != null) {
                        formData.append(i, value[j]);
                    }
                }
            } else {
                formData.append(i, value);
            }
        }
    };

    MultipartUpload.prototype.getBlob = function(file, start, end) {
        var blob = file;
        start = (start || 0);
        end = (end || 0);

        if(file.slice) {
            blob = file.slice(start, end);
            blob.name = file.name;
            blob.contentType = file.type;
        }

        if(file.webkitSlice) {
            blob = file.webkitSlice(start, end);
            blob.name = file.name;
            blob.contentType = file.type;
        }

        if(file.mozSlice) {
            blob = file.mozSlice(start, end);
            blob.name = file.name;
            blob.contentType = file.type;
        }
        return blob;
    };

    MultipartUpload.prototype.getFormData = function(name, file) {
        var args = arguments;
        var formData = new FormData();
        formData.append(name, file, file.name);

        for(var i = 2; i < args.length; i++) {
            this.append(formData, args[i]);
        }
        return formData;
    };

    MultipartUpload.prototype.upload = function(settings) {
        var accept = (settings.accept || "*");
        var id = "_ifile" + this.getNextId();
        var input = null;

        if(window.ActiveXObject) {
            input = document.createElement("<input id=\"" + id + "\" name=\"" + id + "\" type=\"file\" accept=\"" + accept + "\"/>");
        }
        else {
            input = document.createElement("input");
            input.name = id;
            input.type = "file";
            input.accept = accept;
        }
        document.body.appendChild(input);

        var self = this;
        var destory = function() {
            try {
                input.parentNode.removeChild(input);
            } catch (e) {}
        };

        var onchange = function() {
            var value = input.value;

            if(value == null || value.length < 1) {
                destory();
                return;
            }

            if(self.select != null) {
                self.select(settings, input.files);
            }
        };
        this.addEventListener(input, "change", onchange);
        input.click();
    };

    MultipartUpload.prototype.submit = function(settings) {
        if(this.status == "load") {
            throw {"name": "UploadException", "message": "501"};
        }

        var self = this;
        var url = settings.url;
        var name = settings.name;
        var file = settings.file;
        var data = settings.data;
        var length = file.size;
        var partSize = (settings.partSize || (10 * 1024 * 1024));
        var transport = this.getXmlHttpRequest();
        this.status = "load";

        var upload = function(offset, count) {
            if(offset >= length) {
                return;
            }

            var end = Math.min(offset + partSize, length);
            var range = "bytes " + offset + "-" + end + "/" + length;
            var blob = self.getBlob(file, offset, end);

            var formData = self.getFormData(name, blob, data, {
                "offset": offset,
                "length": (end - offset),
                "fileSize": length,
                "partSize": partSize
            });

            var options = {};
            options.url = url;
            options.method = "post";
            options.headers = [{"name": "Content-Range", "value": range}];
            options.data = formData;
            options.transport = transport;
            options.onuploadprogress = function(e) {
                var loaded = (e.loaded || e.position);
                var total = (e.total || e.totalSize);
                self.progress(file, offset + loaded, length);
            };

            options.success = function(xhr) {
                /**
                 * abort
                 */
                if(xhr.status == 0) {
                    self.status = "done";
                    self.cancel(xhr, "AbortException");
                    return;
                }

                if(xhr.status != 200) {
                    if(count < self.retryTimes) {
                        upload(offset, count + 1);
                    }
                    else {
                        self.status = "done";
                        self.error(xhr, "UploadException");
                    }
                    return;
                }

                if(end >= length) {
                    self.status = "done";
                    self.progress(file, length, length);
                    self.success(file, self.getResponse(xhr));
                }
                else {
                    upload(end, 1);
                }
            };

            options.error = function(xhr) {
                /**
                 * ready & abort
                 */
                if(xhr.readyState == 0) {
                    self.status = "done";
                    self.error(xhr, "AbortException");
                    return;
                }

                if(count < self.retryTimes) {
                    upload(offset, count + 1);
                }
                else {
                    self.status = "done";
                    self.error(xhr, "UploadException");
                }
            };
            self.xmlHttpRequest = Ajax.request(options);
        };
        upload(0, 1);
    };

    MultipartUpload.prototype.getResponse = function(xmlHttpRequest) {
        try {
            return eval("(" + xmlHttpRequest.responseText + ")");
        }
        catch (e) {
        }
        return null;
    };

    MultipartUpload.prototype.contains = function(source, searchment) {
        if(source == null || searchment == null) {
            return false;
        }

        var a = source.split(",");

        for(var i = 0; i < a.length; i++) {
            if(this.trim(a[i]) == searchment) {
                return true;
            }
        }
        return false;
    };

    MultipartUpload.prototype.getExtension = function(name) {
        if(name == null) {
            return "";
        }

        var k = name.lastIndexOf(".");

        if(k > -1) {
            return name.substring(k).toLowerCase();
        }
        else {
            return "";
        }
    };

    MultipartUpload.prototype.trim = function(s) {
        return (s != null ? s.toString().replace(/(^\s*)|(\s*$)/g, "") : "");
    };

    MultipartUpload.prototype.progress = function(loaded, total) {
    };

    MultipartUpload.prototype.error = function(xmlHttpRequest) {
    };

    MultipartUpload.prototype.success = function(xmlHttpRequest) {
    };

    MultipartUpload.prototype.cancle = function(xmlHttpRequest) {
    };

    MultipartUpload.prototype.abort = function() {
        if(this.xmlHttpRequest != null) {
            this.xmlHttpRequest.abort();
            this.xmlHttpRequest = null;
        }
        this.status = "done";
    };
    window.MultipartUpload = MultipartUpload;
    window.ByteUtil = ByteUtil;
})();