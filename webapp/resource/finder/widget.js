(function() {
    var com = {skin: {framework: {}}};

    /*
     * $RCSfile: Class.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Class = com.skin.framework.Class = {};

    Class.getClassId = function() {
        if(this.id == null) {
            this.id = 0;
        }

        this.id++;
        return "class_" + this.id;
    };

    /**
     * create a class
     * @param parent
     * @param constructor
     * @return Object
     */
    Class.create = function(parent, constructor, fields){
        var clazz = null;

        if(parent != null) {
            if(constructor != null) {
                clazz = function(){/* Class.create */ parent.apply(this, arguments); constructor.apply(this, arguments);};
            }
            else {
                clazz = function(){/* Class.create */ parent.apply(this, arguments);};
            }

            for(var property in parent.prototype) {
                clazz.prototype[property] = parent.prototype[property];
            }

            clazz.prototype["toString"] = parent.prototype["toString"];
            clazz.$super = parent.prototype;
        }
        else {
            if(constructor != null) {
                clazz = function(){/* Class.create */ constructor.apply(this, arguments);};
            }
            else {
                clazz = function(){/* Class.create */};
            }
            clazz.$super = {};
        }

        clazz.parent = parent;
        clazz.classId = this.getClassId();
        clazz.prototype.constructor = clazz;

        if(fields != null && fields.length > 0) {
            this.let(clazz, fields);
        }
        return clazz;
    };

    /**
     * @param instance
     * @param prototype
     * @retur Object
     */
    Class.$super = /* private */ function(instance, prototype){
        var object = {};

        for(var i in prototype) {
            if(typeof(prototype[i]) == "function") {
                object[i] = function(){prototype[i].apply(instance, arguments);};
            }
        }
        return object;
    };

    /**
     * var myduck = Class.getInstance(Animal, function(){this.swim = function(){};});
     * myduck.swim();
     * @param parent
     * @param constructor
     * @return Object
     */
    Class.getInstance = function(parent, constructor){
        return new (Class.create(parent, constructor))();
    };

    /**
     * extend properties
     * @param child
     * @param parent
     * @return Object
     */
    Class.extend = function(child, parent){
        if(child == null) {
            child = {};
        }

        for(var property in parent) {
            child[property] = parent[property];
        }
        return child;
    };

    /**
     * @param f
     * @param fields
     * @return Object
     */
    Class.let = function(f, fields){
        var p = f.prototype;

        for(var i = 0, length = fields.length; i < length; i++) {
            var name = fields[i];
            var method = name.charAt(0).toUpperCase() + name.substring(1);
            p["set" + method] = new Function(name, "this." + name + " = " + name + ";");
            p["get" + method] = new Function("return this." + name + ";");
        }
    };

    /*
     * $RCSfile: ID.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var ID = {};

    ID.next = function() {
        if(this.seed == null) {
            this.seed = 0;
        }

        this.seed++;
        return this.seed;
    };

    /*
     * $RCSfile: StringUtil.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var StringUtil = {};
    StringUtil.trim = function(s) {return (s != null ? new String(s).replace(/(^\s*)|(\s*$)/g, "") : "");}
    StringUtil.startsWith = function(source, search) {
        if(source.length >= search.length) {
            return (source.substring(0, search.length) == search);
        }
        return false;
    };

    /**
     * @param source
     * @param search
     * @param replacement
     * @return String
     */
    StringUtil.replace = function(source, search, replacement) {
        if(source == null) {
            return "";
        }

        if(search == null) {
            return source;
        }

        var s = 0;
        var e = 0;
        var d = search.length;
        var content = source;
        var buffer = [];

        while(true) {
            while(true) {
                e = content.indexOf(search, s);

                if(e == -1) {
                    buffer[buffer.length] = content.substring(s);
                    break;
                }
                else {
                    buffer[buffer.length] = content.substring(s, e);
                    buffer[buffer.length] = replacement;
                    s = e + d;
                }
            }

            content = buffer.join("");
            e = content.indexOf(search, 0);

            if(e > -1) {
                s = 0;
                buffer.length = 0;
            }
            else {
                break;
            }
        }
        return content;
    };

    /*
     * $RCSfile: KeyCode.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var KeyCode = {
        F1:  112,
        F2:  113,
        F3:  114,
        F4:  115,
        F5:  116,
        F6:  117,
        F7:  118,
        F8:  119,
        F9:  120,
        F10: 121,
        F11: 122,
        F12: 123,
        END:  35,
        HOME: 36,
        INSERT: 45,
        DELETE: 46,
        BACKSPACE: 8,
        TAB: 9,
        ENTER: 13,
        SHIFT: 16,
        CTRL:  17,
        ALT:   18,
        CAPSLOCK: 20,
        ESC: 27,
        BLANKSPACE: 32,
        LEFT:  37,
        UP:    38,
        RIGHT: 39,
        DOWN:  40
    };

    KeyCode.isLetter = function(keyCode) {
        return (keyCode >= 65 && keyCode <= 90);
    };

    KeyCode.isLetter = function(keyCode) {
        return (keyCode >= 48 && keyCode <= 57);
    };

    var ListenerId = {};

    ListenerId.next = function() {
        if(this.id == null) {
            this.id = 1;
        }
        return this.id++;
    };

    /*
     * $RCSfile: Listener.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Listener = function(context) {
        this.context = context;
    };

    Listener.prototype.dispatch = function(name, event) {
        var handler = this[name];

        if(handler != null) {
            return handler.apply(this, [event]);
        }
        else {
            return true;
        }
    };

    Listener.prototype.paste = function(event) {
        return true;
    };

    Listener.prototype.keydown = function(event) {
        return true;
    };

    Listener.prototype.keyup = function(event) {
        return true;
    };

    Listener.prototype.mouseover = function(event) {
        return true;
    };

    Listener.prototype.mousedown = function(event) {
        return true;
    };

    Listener.prototype.mouseup = function(event) {
        return true;
    };

    Listener.prototype.dblclick = function(event) {
        return true;
    };

    Listener.prototype.click = function(event) {
        return true;
    };

    Listener.prototype.dblclick = function(event) {
        return true;
    };

    /*
     * $RCSfile: Shortcut.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Shortcut = Class.create(Listener, function() {
        this.listeners = [];
    });

    Shortcut.prototype.keydown = function(event) {
        var src = (event.srcElement || event.target);
        var nodeName = src.nodeName.toLowerCase();

        if(nodeName == "input" || nodeName == "textarea" || nodeName == "select") {
            return true;
        }

        var listeners = this.listeners;

        for(var i = 0; i < listeners.length; i++) {
            var listener = listeners[i];
            var shortcut = listener.shortcut;

            if(this.match(shortcut, event) == true) {
                return listener.handler.apply(this.context, [event]);
            }
        }
        return true;
    };

    Shortcut.prototype.match = function(shortcut, event) {
        var a = shortcut.split("+");
        var keyCode = (event.keyCode || event.which);

        if(a.length < 1) {
            return false;
        }

        for(var i = 0; i < a.length; i++) {
            var name = StringUtil.trim(a[i]);

            if(name.length < 1) {
                continue;
            }

            if(name == "CTRL") {
                if(event.ctrlKey == true) {
                    continue;
                }
                else {
                    return false;
                }
            }

            if(name == "SHIFT") {
                if(event.shiftKey == true) {
                    continue;
                }
                else {
                    return false;
                }
            }

            if(name == "ALT") {
                if(event.altKey == true) {
                    continue;
                }
                else {
                    return false;
                }
            }

            if(name.length > 1) {
                if(keyCode != KeyCode[name]) {
                    return false;
                }
            }
            else {
                if(keyCode != name.charCodeAt(0)) {
                    return false;
                }
            }
        }
        return true;
    };

    Shortcut.prototype.addListener = function(shortcut, handler) {
        var a = shortcut.split("|");

        for(var i = 0; i < a.length; i++) {
            var shortcut = StringUtil.trim(a[i]);

            if(shortcut.length > 0) {
                this.listeners[this.listeners.length] = {"shortcut": shortcut, "handler": handler};
            }
        }
    };

    /*
     * $RCSfile: ArrayList.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var ArrayList = function() {
        this.elements = [];
    };

    ArrayList.prototype.add = function(e) {
        this.elements[this.elements.length] = e;
    };

    ArrayList.prototype.contains = function(e) {
        var elements = this.elements;

        for(var i = 0; i < elements.length; i++) {
            if(elements[i] == e) {
                return true;
            }
        }
        return false;
    };

    ArrayList.prototype.remove = function(e) {
        var b = false;
        var list = [];
        var elements = this.elements;

        for(var i = 0; i < elements.length; i++) {
            if(elements[i] != e) {
                list[list.length] = elements[i];
            }
            else {
                b = true;
            }
        }
        this.elements = list;
        return b;
    };

    ArrayList.prototype.get = function(i) {
        if(i >= 0 && i < this.elements.length) {
            return this.elements[i];
        }
        else {
            return null;
        }
    };

    ArrayList.prototype.first = function(i) {
        return this.get(0);
    };

    ArrayList.prototype.last = function(i) {
        var size = this.size();
        return this.get(size - 1);
    };

    ArrayList.prototype.size = function(i) {
        return this.elements.length;
    };

    ArrayList.prototype.list = function() {
        var list = [];
        var elements = this.elements;

        for(var i = 0; i < elements.length; i++) {
            list[list.length] = elements[i];
        }
        return list;
    };

    ArrayList.prototype.each = function(handler) {
        var list = [];
        var elements = this.elements;

        for(var i = 0; i < elements.length; i++) {
            var flag = handler(elements[i]);

            if(flag == false) {
                break;
            }
        }
    };

    /*
     * $RCSfile: LinkedList.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var LinkedList = function() {
        this.head = null;
        this.tail = null;
    };

    LinkedList.prototype.add = function(e) {
        var node = new Node(null, null, e);

        if(this.head == null) {
            this.head = node;
            this.tail = node;
        }
        else {
            node.prev = this.tail;
            this.tail.next = node;
            this.tail = node;
        }
    };

    LinkedList.prototype.contains = function(e) {
        var node = this.head;

        while(node != null) {
            if(node.item == e) {
                return true;
            }
            else {
                node = node.next;
            }
        }
        return false;
    };

    LinkedList.prototype.remove = function(e) {
        var node = this.head;

        while(node != null) {
            if(node.item == e) {
                var prev = node.prev;
                var next = node.next;

                if(prev == null) {
                    this.head = next;

                    if(next != null) {
                        next.prev = null;
                    }
                }
                else {
                    prev.next = next;

                    if(next != null) {
                        next.prev = prev;
                    }
                }

                if(next == null) {
                    this.tail = prev;
                }
                else {
                }

                node.prev = null;
                node.next = null;

                /**
                 * don't break
                 */
                node = next;
            }
            else {
                node = node.next;
            }
        }
        return false;
    };

    LinkedList.prototype.get = function(i) {
        var index = 0;
        var element = null;

        this.each(function(e) {
            if(index == i) {
                element = e;
                return false;
            }
            index++;
        });
        return element;
    };

    LinkedList.prototype.first = function(i) {
        if(this.head != null) {
            return this.head.item;
        }
        else {
            return null;
        }
    };

    LinkedList.prototype.last = function(i) {
        if(this.tail != null) {
            return this.tail.item;
        }
        else {
            return null;
        }
    };

    LinkedList.prototype.size = function() {
        var length = 0;
        var node = this.head;

        while(node != null) {
            node = node.next;
            length++;
        }
        return length;
    };

    LinkedList.prototype.list = function() {
        var list = [];
        var node = this.head;

        while(node != null) {
            list[list.length] = node.item;
            node = node.next;
        }
    };

    LinkedList.prototype.each = function(handler) {
        var list = [];
        var node = this.head;

        while(node != null) {
            var flag = handler(node.item);

            if(flag == false) {
                break;
            }
            node = node.next;
        }
    };

    /*
     * $RCSfile: Node.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Node = function(prev, next, item) {
        this.prev = prev;
        this.next = next;
        this.item = item;
    };

    Node.prototype.toString = function() {
        var buffer = [];
        buffer[buffer.length] = this.item;

        if(this.next != null) {
            buffer[buffer.length] = " -> " + this.next.toString();
        }
        return buffer.join("");
    };

    /*
     * $RCSfile: BindUtil.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var BindUtil = {};

    BindUtil.bind = function(object, handler) {
        return function(){
            return handler.apply(object, arguments);
        }
    };

    BindUtil.bindAsEventListener = function(object, handler) {
        return function(event) {
            return handler.call(object, (event || window.event));
        }
    };

    /*
     * $RCSfile: EventUtil.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var EventUtil = {};
    EventUtil.addEventListener = function(target, type, handler) {
        if(target.addEventListener) {
            target.addEventListener(type, handler, false);
        }
        else if(target.attachEvent) {
            target.attachEvent("on" + type, handler);
        }
        else {
            target["on" + type] = handler;
        }
    };

    EventUtil.removeEventListener = function(target, type, handler) {
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

    EventUtil.stop = function(event, returnValue) {
        if(event != null) {
            if(event.stopPropagation) {
                event.stopPropagation();
            }
            else {
                event.cancelBubble = true;
            }

            if(event.preventDefault) {
                event.preventDefault();
            }

            if(returnValue == true) {
                event.cancel = false;
                event.returnValue = true;
            }
            else {
                event.cancel = true;
                event.returnValue = false;
            }
        }
        return false;
    };

    /*
     * $RCSfile: Dragable.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Dragable = function(source, target) {
        this.x = 0;
        this.y = 0;
        this.source = source;
        this.target = target;
        this.frame = document.getElementById("_dragable_frame");

        this.start = function(event){
            var src = (event.srcElement || event.target);
            var keyCode = (event.keyCode || event.which);

            if(keyCode != 1) {
                return true;
            }

            if(src.getAttribute("dragable") == "false") {
                return true;
            }

            this.y = event.clientY - this.target.offsetTop;
            this.x = event.clientX - this.target.offsetLeft;

            var x = this.target.offsetLeft;
            var y = this.target.offsetTop;
            var w = this.target.offsetWidth;
            var h = this.target.offsetHeight;

            this.frame.style.top = y + "px";
            this.frame.style.left = x + "px";
            this.frame.style.width = w + "px";
            this.frame.style.height = h + "px";
            this.frame.style.display = "block";
            this.frame.style.zIndex = parseInt(this.target.style.zIndex) + 10;

            EventUtil.addEventListener(document, "mouseup", this.stopHandler);
            EventUtil.addEventListener(document, "mousemove", this.moveHandler);
            return true;
        };

        this.move = function(event){
            var x = event.clientX - this.x;
            var y = event.clientY - this.y;
            this.frame.style.top = y + "px";
            this.frame.style.left = x + "px";
            this.frame.style.zIndex = parseInt(this.target.style.zIndex) + 10;
            return this.stopPropagation(event);
        };

        this.stop = function(event){
            var y = this.frame.offsetTop;
            var x = this.frame.offsetLeft;

            this.target.style.marginTop = "0px";
            this.target.style.marginLeft = "0px";
            this.target.style.top = y + "px";
            this.target.style.left = x + "px";

            this.frame.style.zIndex = -1;
            this.frame.style.display = "none";

            EventUtil.removeEventListener(document, "mouseup", this.stopHandler);
            EventUtil.removeEventListener(document, "mousemove", this.moveHandler);
            return this.stopPropagation(event);
        };

        this.stopPropagation = function(event){
            if(event.stopPropagation) {
                event.stopPropagation();
            }
            else {
                event.cancelBubble = true;
            }

            if(event.preventDefault) {
                event.preventDefault();
            }

            event.cancel = true;
            event.returnValue = false;
            return false;
        };

        this.stopHandler = BindUtil.bindAsEventListener(this, this.stop);
        this.moveHandler = BindUtil.bindAsEventListener(this, this.move);

        if(this.frame == null) {
            this.frame = document.createElement("div");
            this.frame.id = "_dragable_frame";

            if(document.all) {
                this.frame.innerHTML = "<div style=\"width: 100%; height: 100%; background-color: #0e89e6; filter: alpha(opacity=10); cursor: default;\"></div>";
            }
            else {
                this.frame.innerHTML = "<div style=\"width: 100%; height: 100%; background-color: #0e89e6; opacity: 0.1; cursor: default;\"></div>";
            }
            document.body.appendChild(this.frame);
        }

        if(this.target != null) {
            this.target.style.position = "absolute";
            var y = this.target.offsetTop;
            var x = this.target.offsetLeft;
            var w = this.target.offsetWidth - 0;
            var h = this.target.offsetHeight - 0;

            var cssText = "position: absolute; display: none;"
                + " width: " + w + "px; height: " + h + "px; top:" + y + "px; left:" + x + "px;"
                + " border: 1px dashed #3399FF; background-color: transparent; cursor: default; z-index: -1";
            this.frame.style.cssText = cssText;
        }

        if(this.source != null) {
            EventUtil.addEventListener(this.source, "mousedown", BindUtil.bindAsEventListener(this, this.start));
        }
    };

    Dragable.registe = function(source, target){
        var e1 = null;
        var e2 = null;

        if(typeof(source) == "string") {
            e1 = document.getElementById(source);
        }
        else {
            e1 = source;
        }

        if(typeof(target) == "string") {
            e2 = document.getElementById(target);
        }
        else {
            e2 = target;
        }

        if(e1 != null && e2 != null) {
            new Dragable(e1, e2);
        }
    };

    var TaskManager = {};
    TaskManager.id = "_task_bar";
    TaskManager.tasks = new ArrayList();

    TaskManager.getById = function(id) {
       var dialog = null;

       this.tasks.each(function(e) {
            if(e.getId() == id) {
                dialog = e;
                return false;
            }
            else {
                return true;
            }
       });
       return dialog;
    };

    TaskManager.add = function(dialog) {
        var dialogId = dialog.getId();

        if(this.getById(dialogId) == null) {
            this.tasks.remove(dialog);
            this.tasks.add(dialog);
            this.show();
        }
    };

    TaskManager.remove = function(dialog) {
        var task = this.getById(dialog.getId());
        this.tasks.remove(task);
        jQuery("#" + this.id + " ul li[dialogId=" + dialog.getId() + "]").remove();
    };

    TaskManager.show = function() {
        var c = document.getElementById(this.id);

        if(c != null) {
            var buffer = [];
            var tasks = this.tasks;
            buffer[buffer.length] = "<ul class=\"widget-task-bar\">";

            for(var i = 0, size = tasks.size(); i < size; i++) {
                var dialog = tasks.get(i);

                if(dialog.parent == null) {
                    buffer[buffer.length] = "<li dialogId=\"" + dialog.getId() + "\" title=\"" + dialog.title + "\"onclick=\"TaskManager.toggle(this)\">";

                    if(dialog.icon != null) {
                        buffer[buffer.length] = "<img style=\"margin-top: -2px; margin-right: 4px;\" src=\"" + dialog.icon + "\">";
                    }
                    buffer[buffer.length] = dialog.title + "</li>";
                }
            }
            buffer[buffer.length] = "</ul>";
            c.innerHTML = buffer.join("");
        }
    };

    TaskManager.setTitle = function(dialog) {
        var dialogId = dialog.getId();

        if(dialog.icon != null) {
            jQuery("#" + this.id + " ul li[dialogId=" + dialogId + "]").html("<img style=\"margin-top: -2px; margin-right: 4px;\" src=\"" + dialog.icon + "\">" + dialog.title);
        }
        else {
            jQuery("#" + this.id + " ul li[dialogId=" + dialogId + "]").html(dialog.title);
        }
    };

    TaskManager.toggle = function(src) {
        var dialog = this.getById(src.getAttribute("dialogId"));

        if(dialog != null) {
            dialog.toggle();
        }
    };

    TaskManager.getContainer = function() {
        return document.getElementById(this.id);
    };

    /*
     * $RCSfile: DialogManager.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var DialogManager = {};
    DialogManager.zIndex = 1000;
    DialogManager.active = null;
    DialogManager.dialogs = new ArrayList();

    DialogManager.setActive = function(dialog, b) {
        if(b == true) {
            this.push(dialog);
        }
        else {
            this.pop(dialog);
        }
    };

    DialogManager.add = function(dialog) {
        if(dialog.parent == null) {
            var last = this.dialogs.last();

            if(last != dialog) {
                this.dialogs.remove(dialog);
                this.dialogs.add(dialog);
            }
            DialogManager.show();
        }
    };

    DialogManager.push = function(dialog) {
        var last = this.dialogs.last();

        if(last != dialog) {
            this.dialogs.remove(dialog);
            this.dialogs.add(dialog);

            this.dialogs.each(function(e) {
                if(e != dialog) {
                    e.setActiveStyle(false);
                }
            });
            dialog.setActiveStyle(true);
        }
        dialog.focus();
    };

    DialogManager.pop = function(dialog) {
        this.dialogs.remove(dialog);
        dialog.setActiveStyle(false);

        var dialog = this.getActive();

        if(dialog != null) {
            this.push(dialog);
        }
        return true;
    };

    DialogManager.remove = function(dialog) {
        return this.dialogs.remove(dialog);
    };

    DialogManager.getActive = function() {
        return this.dialogs.last();
    };

    DialogManager.getZIndex = function() {
        this.zIndex = this.zIndex + 5;
        return this.zIndex;
    };

    DialogManager.dispatch = function(name, event) {
        var dialog = this.getActive();

        if(dialog != null) {
            return dialog.getListener().dispatch(name, event);
        }
        else {
            return true;
        }
    };

    /*
     * $RCSfile: Mask.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Mask = {"id": "_dialog_mask"};
    Mask.queue = [];
    Mask.show = function(id, zIndex) {
        var e = document.getElementById(this.id);

        var cssText = [
            "display: none;",
            "top: 0px; left: 0px;",
            "width: 200px; height: 200px;",
            "position: absolute;",
            "background: #000000;",
            "opacity: 0.5; filter: alpha(opacity=50);"
        ].join(" ");

        if(e != null) {
            e.style.cssText = cssText;
        }
        else {
            e = document.createElement("div");
            e.id = this.id;
            e.className = "dialog-mask";
            e.style.cssText = cssText;
            e.setAttribute("contextmenu", "false");
            document.body.appendChild(e);
        }

        if(e.childNodes.length < 1) {
            var html = [
                "<iframe frameborder=\"0\" scrolling=\"no\"",
                " style=\"top: 0px; left: 0px;",
                " width: 200px; height: 200px;",
                " border: 0px solid #5183dc; background-color: transparent; z-index: 9991; overflow: hidden;\"",
                " src=\"about:blank\"></iframe>"].join("");
            e.innerHTML = html;

            var frame = e.childNodes[0].contentWindow.document;
            frame.open("text/html");
            frame.write("<html><head><title>mask</title></head><body style=\"background-color: #000000;\"></body></html>");
            frame.close();
        }

        e.style.zIndex = zIndex;
        e.style.display = "block";
        this.resize();
        this.block(id);
    };

    Mask.block = function(id) {
        var length = this.queue.length;
        for(var i = 0; i < length; i++) {
            if(this.queue[i] == id) {
                return true;
            }
        }

        for(var i = 0; i < length; i++) {
            if(this.queue[i] == "") {
                this.queue[i] = id;
                return true;
            }
        }
        this.queue[length] = id;
        return true;
    };

    Mask.resize = function() {
        var e = document.getElementById(this.id);

        if(e != null) {
            var width = document.documentElement.scrollWidth;
            var height = document.documentElement.scrollHeight;

            if(document.documentElement.scrollWidth < document.documentElement.clientWidth) {
                width = document.documentElement.clientWidth;
            }

            if(document.documentElement.scrollHeight < document.documentElement.clientHeight) {
                height = document.documentElement.clientHeight;
            }

            e.style.width = width + "px";
            e.style.height = height + "px";

            if(e.childNodes.length > 0) {
                var frame = e.childNodes[0];
                frame.style.width = width;
                frame.style.width = height;
            }
        }
    };

    Mask.close = function(id) {
        var e = document.getElementById(this.id);

        for(var i = 0; i < this.queue.length; i++) {
            if(this.queue[i] == id) {
                this.queue[i] = "";
            }
        }

        if(this.queue.join("").length < 1) {
            this.queue.length = 0;

            if(e != null) {
                e.style.display = "none";
            }
            return false;
        }
        return true;
    };

    /**
     * $RCSfile: Dialog.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var Dialog = com.skin.framework.Class.create(null, function(args){
        var options = (args || {});
        this.id = null;
        this.title = options.title;
        this.block = options.block;
        this.parent = options.parent;
        this.childs = new ArrayList();
        this.zIndex = DialogManager.getZIndex();

        if(options.container == null) {
            this.id = "_widget_component_" + ID.next();
        }
        else {
            if(typeof(options.container) == "string") {
                this.id = options.container;
            }
            else {
                if(options.container.id != null) {
                    this.id = options.container.id;
                }
                else {
                    this.id = "_widget_component_" + ID.next();
                    options.container.id = this.id;
                }
            }
        }

        if(options.parent != null) {
            options.parent.childs.add(this);
        }

        var self = this;
        var container = this.getContainer();

        if(container != null) {
            /**
             * 当单击一个窗口的时候需要将该窗口变为活动窗口
             * 拖拽效果注册的是mousedown事件, 所以此处只能注册为mousedown事件
             * 但是如果窗口出现了嵌套，将导致事件无法被传递到子窗口，所以此处不允许嵌套
             */
            if(container == document.body || container == window || container == document) {
                throw {"name": "Error", "message": "窗口容器不允许嵌套！"};
            }

            /**
             * 以下两个事件用来保证当一个窗口被单击的时候将该窗口设置为活动窗口
             */
            EventUtil.addEventListener(container, "mousedown", function(event) {
                DialogManager.push(self);
            });

            EventUtil.addEventListener(container, "contextmenu", function(event) {
                DialogManager.push(self);
            });
        }

        var content = this.getContent();

        if(content != null) {
            this.setContent(content);
        }
        this.create();
    });

    Dialog.prototype.getId = function(title) {
        return this.id;
    };

    Dialog.prototype.create = function(content) {
    };

    Dialog.prototype.setTitle = function(title) {
        this.title = title;
        DialogManager.show();
    };

    Dialog.prototype.open = function() {
        if(this.block == true) {
            Mask.show(this.id);
        }
        return this.show();
    };

    Dialog.prototype.close = function() {
        this.setVisiable(false, false);

        if(this.parent != null) {
            this.parent.childs.remove(this);
        }

        this.childs.each(function(e) {
            e.close();
        });
    };

    Dialog.prototype.exit = function() {
        this.destroy();
    };

    Dialog.prototype.destroy = function() {
        DialogManager.pop(this);
        var c = this.getContainer();

        if(c != null) {
            c.parentNode.removeChild(c);
        }

        if(this.parent != null) {
            this.parent.childs.remove(this);
        }

        this.childs.each(function(e) {
            e.destroy();
        });
    };

    Dialog.prototype.setContent = function(content) {
        var c = this.getContainer();

        if(c != null) {
            if(typeof(content) == "string") {
                c.innerHTML = content;
            }
            else {
                c.appendChild(content);
            }
        }
    };

    Dialog.prototype.getContent = function() {
        return null;
    };

    Dialog.prototype.setVisiable = function(visiable, active) {
        var c = this.getContainer();

        if(c != null) {
            if(visiable == true) {
                if(active == true) {
                    DialogManager.setActive(this, true);
                }

                if(this.block == true) {
                    Mask.show(this.id, this.zIndex - 1);
                }
                c.style.display = "block";
            }
            else {
                DialogManager.setActive(this, false);

                if(this.block == true) {
                    Mask.close(this.id);
                }
                c.style.display = "none";
            }
        }
    };

    /**
     * @param active
     */
    Dialog.prototype.focus = function(active) {
        var c = this.getContainer();

        if(c != null) {
            try {
                c.focus();
            }
            catch(e) {
            }
        }
    };

    /**
     * @param active
     */
    Dialog.prototype.setActive = function(active) {
        DialogManager.setActive(this, active);
    };

    Dialog.prototype.setActiveStyle = function(active) {
        var c = this.getContainer();

        if(c != null) {
            if(active == true) {
                c.style.borderColor = "#3f6cad";
                c.style.zIndex = DialogManager.getZIndex();
            }
            else {
                c.style.borderColor = "#cccccc";
                c.style.zIndex = this.zIndex;
            }
        }
    };

    Dialog.prototype.show = function() {
        var c = this.getContainer();

        if(c != null) {
            c.style.display = "block";
            var width = this.getWidth(c);
            var height = this.getHeight(c);
            var top = document.documentElement.scrollTop + document.body.scrollTop;
            var left = document.documentElement.scrollLeft + document.body.scrollLeft;
            var y = top + parseInt((document.documentElement.clientHeight - height) / 2);
            var x = left + parseInt((document.documentElement.clientWidth - width) / 2);

            c.style.top = (y > 0 ? y : 0) + "px";
            c.style.left = (x > 0 ? x : 0) + "px";
            this.setVisiable(true, true);
        }
        return this;
    };

    Dialog.prototype.hide = function() {
        var c = this.getContainer();

        if(c != null) {
            c.style.display = "none";
        }
        return this;
    };

    Dialog.prototype.toggle = function() {
        var c = this.getContainer();

        if(c != null) {
            if(c.style.display != "none") {
                c.style.display = "none";
            }
            else {
                c.style.display = "block";
            }
        }
        return this;
    };

    /**
     * 快捷键管理器
     * @param listener
     */
    Dialog.prototype.setListener = function(listener) {
        this.listener = listener;
    };

    /**
     * 快捷键管理器
     * @return Shortcut
     */
    Dialog.prototype.getListener = function() {
        if(this.listener == null) {
            this.listener = new Shortcut(this);
        }
        return this.listener;
    };

    Dialog.prototype.getShortcut = function(key, handler) {
        return this.getListener();
    };

    Dialog.prototype.addShortcut = function(key, handler) {
        if(typeof(handler) == "string") {
            var command = handler;

            this.getListener().addListener(key, function(event) {
                this.close();
                this.execute(command);
                return false;
            });
        }
        else {
            this.getListener().addListener(key, handler);
        }
    };

    Dialog.prototype.getContainer = function() {
        var c = document.getElementById(this.id);

        if(c == null) {
            c = document.createElement("div");
            c.id = this.id;
            c.className = "dialog";
            c.setAttribute("contextmenu", false);
            document.body.appendChild(c);
        }
        return c;
    };

    Dialog.prototype.getZIndex = function() {
        return this.zIndex;
    };

    Dialog.prototype.getStyle = function(e, name) {
        if(e.style[name]) {
            return e.style[name];
        }
        else if(document.defaultView != null && document.defaultView.getComputedStyle != null) {
            var computedStyle = document.defaultView.getComputedStyle(e, null);

            if(computedStyle != null) {
                var property = name.replace(/([A-Z])/g, "-$1").toLowerCase();
                return computedStyle.getPropertyValue(property);
            }
        }
        else if(e.currentStyle != null) {
            return e.currentStyle[name];
        }
        return null;
    };

    Dialog.prototype.getWidth = function(e) {
        var result = this.getStyle(e, "width");

        if(result != null) {
            result = parseInt(result.replace("px", ""));
        }
        return isNaN(result) ? 0 : result;
    };

    Dialog.prototype.getHeight = function(e) {
        var result = this.getStyle(e, "height");

        if(result != null) {
            result = parseInt(result.replace("px", ""));
        }
        return isNaN(result) ? 0 : result;
    };

    /*
     * $RCSfile: MessageDialog.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var MessageDialog = Class.create(Dialog, function() {
        this.block = true;
    });

    MessageDialog.prototype.create = function() {
        var self = this;
        var buffer = [];
        buffer[buffer.length] = "<div class=\"title\">";
        buffer[buffer.length] = "    <h4 class=\"alert\">提示</h4>";
        buffer[buffer.length] = "    <span class=\"close\" dragable=\"false\"></span>";
        buffer[buffer.length] = "</div>";
        buffer[buffer.length] = "<div class=\"body20\">";
        buffer[buffer.length] = "    <div class=\"text\"></div>";
        buffer[buffer.length] = "</div>";
        buffer[buffer.length] = "<div class=\"button right\">";
        buffer[buffer.length] = "    <button type=\"button\" class=\"button ensure\" href=\"javascript:void(0)\">确 定</button>";
        buffer[buffer.length] = "</div>";
        this.setContent(buffer.join(""));

        var container = jQuery(this.getContainer());
        container.find("div.text").html("");
        container.find("div.button button.ensure").unbind();
        container.find("div.title span.close").unbind();
        container.find("div.button button.ensure").click(function(){
            self.close();
            self.destroy();

            if(self.ensure != null) {
                self.ensure();
            }
            return false;
        });

        container.find("div.title span.close").click(function(){
            self.close();
            self.destroy();

            if(self.ensure != null) {
                self.ensure();
            }
            return false;
        });

        this.addShortcut("ENTER", function() {
            self.close();
            self.destroy();

            if(self.ensure != null) {
                self.ensure(true);
            }
            return false;
        });

        this.addShortcut("ESC", function() {
            self.close();
            self.destroy();

            if(self.ensure != null) {
                self.ensure();
            }
            return false;
        });
        Dragable.registe(container.find("div.title").get(0), container.get(0));
    };

    MessageDialog.prototype.open = function(message) {
        var container = jQuery(this.getContainer());
        container.find("div.text").html(message);
        return this.show();
    };

    /*
     * $RCSfile: ConfirmDialog.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var ConfirmDialog = Class.create(Dialog, function() {
        this.block = true;
    });

    ConfirmDialog.prototype.create = function() {
        var self = this;
        var buffer = [];
        buffer[buffer.length] = "<div class=\"title\">";
        buffer[buffer.length] = "    <h4 class=\"alert\">确认</h4>";
        buffer[buffer.length] = "    <span class=\"close\" dragable=\"false\"></span>";
        buffer[buffer.length] = "</div>";
        buffer[buffer.length] = "<div class=\"body20\">";
        buffer[buffer.length] = "    <div class=\"text\"></div>";
        buffer[buffer.length] = "</div>";
        buffer[buffer.length] = "<div class=\"button right\">";
        buffer[buffer.length] = "    <button type=\"button\" class=\"button ensure\" href=\"javascript:void(0)\">确 定</button>";
        buffer[buffer.length] = "    <button type=\"button\" class=\"button cancel\" href=\"javascript:void(0)\">取 消</button>";
        buffer[buffer.length] = "</div>";
        this.setContent(buffer.join(""));

        var container = jQuery(this.getContainer());
        container.find("div.text").html("");
        container.find("div.button button.ensure").unbind();
        container.find("div.button button.cancel").unbind();
        container.find("div.title span.close").unbind();
        container.find("div.button button.ensure").click(function() {
            self.close();
            self.destroy();

            if(self.ensure != null) {
                self.ensure(true);
            }
            return false;
        });

        container.find("div.button button.cancel").click(function(event) {
            self.close();
            self.destroy();

            if(self.cancel != null) {
                self.cancel(false);
            }
            return false;
        });

        container.find("div.title span.close").click(function() {
            self.close();
            self.destroy();

            if(self.cancel != null) {
                self.cancel(false);
            }
            return false;
        });

        this.addShortcut("ENTER", function() {
            self.close();
            self.destroy();

            if(self.ensure != null) {
                self.ensure(true);
            }
            return false;
        });

        this.addShortcut("ESC", function() {
            self.close();
            self.destroy();

            if(self.cancel != null) {
                self.cancel();
            }
            return false;
        });
        Dragable.registe(container.find("div.title").get(0), container.get(0));
    };

    ConfirmDialog.prototype.open = function(message) {
        var container = jQuery(this.getContainer());
        container.find("div.text").html(message);
        return this.show();
    };

    /*
     * $RCSfile: ContextMenu.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var ContextMenu = Class.create(Dialog, null);

    ContextMenu.prototype.create = function() {
        var self = this;
        var container = this.getContainer();

        this.addShortcut("UP", function(event) {
            self.scroll(false);
            return false;
        });

        this.addShortcut("DOWN", function(event) {
            self.scroll(true);
            return false;
        });

        this.addShortcut("ENTER", function(event) {
            var item = self.getSelected();

            self.close();
            self.execute(item.getAttribute("command"));
            return false;
        });

        jQuery(container).find("ul li.item").mouseover(function() {
            if(jQuery(this).hasClass("disabled")) {
                return false;
            }
            this.className = "item selected";
            return false;
        });

        jQuery(container).find("ul li.item").mouseout(function() {
            if(jQuery(this).hasClass("disabled")) {
                return false;
            }
            this.className = "item";
            return false;
        });

        jQuery(container).find("ul li.item").click(function() {
            if(jQuery(this).hasClass("disabled")) {
                return false;
            }

            self.close();
            self.execute(this.getAttribute("command"));
            return false;
        });
    };

    ContextMenu.prototype.setActiveStyle = function(active) {
        var c = this.getContainer();

        if(c != null) {
            if(active == true) {
                c.style.zIndex = DialogManager.getZIndex();
            }
            else {
                c.style.display = "none";
                DialogManager.remove(this);
            }
        }
    };

    ContextMenu.prototype.setEnabled = function(command, enabled) {
        var container = this.getContainer();

        if(enabled != false) {
            jQuery(container).find("ul li.item[command=" + command + "]").attr("class", "item");
        }
        else {
            jQuery(container).find("ul li.item[command=" + command + "]").attr("class", "item disabled");
        }
    };

    ContextMenu.prototype.getItems = function(enabled) {
        var items = [];
        var container = this.getContainer();

        if(container != null) {
            jQuery(container).find("ul li.item").each(function(e) {
                if(enabled == true) {
                    if(jQuery(this).hasClass("disabled") == false) {
                        items[items.length] = this;
                    }
                }
                else {
                    items[items.length] = this;
                }
            });
        }
        return items;
    };

    ContextMenu.prototype.getSelected = function() {
        var list = this.getItems(true);

        for(var i = 0; i < list.length; i++) {
            var item = list[i];

            if(jQuery(item).hasClass("selected")) {
                return item;
            }
        }

        if(list.length > 0) {
            return list[list.length - 1];
        }
        else {
            return null;
        }
    };

    ContextMenu.prototype.scroll = function(forward) {
        var container = this.getContainer();
        var parent = jQuery(container);
        var selected = this.getSelected();

        if(selected == null) {
            return;
        }

        selected.className = "item";
        var list = this.getItems();

        for(var i = 0; i < list.length; i++) {
            var item = list[i];

            if(item != selected) {
                continue;
            }

            if(forward) {
                if(i + 1 < list.length) {
                    list[i + 1].className = "item selected";
                }
                else {
                    list[0].className = "item selected";
                }
                break;
            }
            else {
                if(i > 0) {
                    list[i - 1].className = "item selected";
                }
                else {
                    list[list.length - 1].className = "item selected";
                }
                break;
            }
        }
    };

    ContextMenu.prototype.show = function() {
        var container = this.getContainer();

        if(container != null) {
            var top = document.body.scrollTop + event.clientY;
            var left = document.body.scrollLeft + event.clientX;
            container.style.display = "block";

            if(top + container.offsetHeight > (document.body.scrollTop + document.documentElement.clientHeight)) {
                top = (top - container.offsetHeight);
            }

            if(top < document.body.scrollTop) {
                top = document.body.scrollTop;
            }

            if(left + container.offsetWidth > (document.body.scrollLeft + document.documentElement.clientWidth)) {
                left = (left - container.offsetWidth);
            }

            if(left < document.body.scrollLeft) {
                left = document.body.scrollLeft;
            }
            container.style.top = top + "px";
            container.style.left = left + "px";
            this.setVisiable(true, true);
        }
        return this;
    };

    ContextMenu.prototype.execute = function(command, event) {
        var handler = this.context[command];

        if(handler != null) {
            handler.apply(this.context, [event]);
        }
    };

    ContextMenu.prototype.setup = function(items) {
        var self = this;
        var context = {};
        var html = this.build(items);
        var container = this.getContainer();

        for(var i = 0; i < items.length; i++) {
            var item = items[i];

            if(item != "|") {
                var name = item.name;
                context[name] = item.action;
            }
        }
        this.setContent(html);
        this.context = context;

        for(var i = 0; i < items.length; i++) {
            var item = items[i];

            if(item != "|" && item.key != null) {
                this.addShortcut(item.key, item.name);
            }
        }

        jQuery(container).find("ul li.item").mouseover(function() {
            if(jQuery(this).hasClass("disabled")) {
                return false;
            }
            this.className = "item selected";
            return false;
        });

        jQuery(container).find("ul li.item").mouseout(function() {
            if(jQuery(this).hasClass("disabled")) {
                return false;
            }
            this.className = "item";
            return false;
        });

        jQuery(container).find("ul li.item").click(function() {
            if(jQuery(this).hasClass("disabled")) {
                return false;
            }

            self.close();
            self.execute(this.getAttribute("command"));
            return false;
        });
    };

    ContextMenu.prototype.build = function(items) {
        var buffer = [];
        buffer[buffer.length] = "<ul class=\"menu\">";

        for(var i = 0; i < items.length; i++) {
            var item = items[i];

            if(item != "|") {
                buffer[buffer.length] = "<li class=\"item\" command=\"" + item.name + "\">";

                if(item.icon != null) {
                    buffer[buffer.length] = "    <span class=\"icon\"><img src=\"" + item.icon + "\"/></span>";
                }
                else {
                    buffer[buffer.length] = "    <span class=\"icon\"></span>";
                }
                buffer[buffer.length] = "    <a class=\"command\" href=\"javascript:void(0)\">" + item.text + "</a>";
                buffer[buffer.length] = "</li>";
            }
            else {
                buffer[buffer.length] = "<li class=\"separator\"></li>";
            }
        }
        buffer[buffer.length] = "</ul>";
        return buffer.join("");
    };

    /*
     * $RCSfile: DialogUtil.js,v $$
     * $Revision: 1.1 $
     * $Date: 2012-10-18 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var DialogUtil = {};

    DialogUtil.alert = function(message, callback) {
        var messageDialog = new MessageDialog();
        messageDialog.ensure = callback;
        messageDialog.open(message);
    };

    DialogUtil.confirm = function(message, callback) {
        var confirmDialog = new ConfirmDialog();
        confirmDialog.ensure = callback;
        confirmDialog.cancel = callback;
        confirmDialog.open(message);
    };

    /**
     * export
     */
    window.com = com;
    window.KeyCode = KeyCode;
    window.EventUtil = EventUtil;
    window.Dialog = Dialog;
    window.DialogManager = DialogManager;
    window.TaskManager = TaskManager;
    window.MessageDialog = MessageDialog;
    window.ConfirmDialog = ConfirmDialog;
    window.ContextMenu = ContextMenu;
    window.Dragable = Dragable;
    window.DialogUtil = DialogUtil;
})();
