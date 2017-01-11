var Base64 = {};

Base64.ENCODE_TABLE = [
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
    "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g",
    "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
    "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2",
    "3", "4", "5", "6", "7", "8", "9", "+", "/"
];

Base64.DECODE_TABLE = [
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
];

Base64.encode = function(text) {
    var c1 = 0;
    var c2 = 0;
    var c3 = 0;

    var data = this.getBytes(text);
    var modulus = data.length % 3;
    var length = data.length - modulus;
    var buffer = [];

    for(var i = 0, j = 0; i < length; i += 3, j += 4) {
        c1 = data[i + 0] & 0xff;
        c2 = data[i + 1] & 0xff;
        c3 = data[i + 2] & 0xff;

        buffer[j + 0] = this.ENCODE_TABLE[((c1 >>> 2) & 0x3f)];
        buffer[j + 1] = this.ENCODE_TABLE[((c1 << 4) | (c2 >>> 4)) & 0x3f];
        buffer[j + 2] = this.ENCODE_TABLE[((c2 << 2) | (c3 >>> 6)) & 0x3f];
        buffer[j + 3] = this.ENCODE_TABLE[((c3 & 0x3f))];
    }

    var b4 = 0;
    var b5 = 0;

    if(modulus == 1) {
        b4 = data[data.length - 1] & 0xff;
        c1 = (b4 >>> 2) & 0x3f;
        c2 = (b4 << 4) & 0x3f;

        buffer[buffer.length] = this.ENCODE_TABLE[c1];
        buffer[buffer.length] = this.ENCODE_TABLE[c2];
        buffer[buffer.length] = "==";
    }
    else if(modulus == 2) {
        b4 = data[data.length - 2] & 0xff;
        b5 = data[data.length - 1] & 0xff;
        c1 = ((b4 >>> 2) & 0x3f);
        c2 = ((b4 << 4) | (b5 >>> 4)) & 0x3f;
        c3 = ((b5 << 2) & 0x3f);

        buffer[buffer.length] = this.ENCODE_TABLE[c1];
        buffer[buffer.length] = this.ENCODE_TABLE[c2];
        buffer[buffer.length] = this.ENCODE_TABLE[c3];
        buffer[buffer.length] = "=";
    }

    return buffer.join("");
};

Base64.decode = function(data) {
    var c1 = 0;
    var c2 = 0;
    var c3 = 0;
    var c4 = 0;
    var buffer = [];

    for(var i = 0, length = data.length; i < length;)  {
        do  {
             c1 = this.DECODE_TABLE[data.charCodeAt(i++) & 0xff];
        }
        while(i < length && c1 == -1);

        if(c1 == -1) {
            break;
        }

        do  {
            c2 = this.DECODE_TABLE[data.charCodeAt(i++) & 0xff];
        }
        while(i < length && c2 == -1);

        if(c2 == -1) {
            break;
        }

        buffer[buffer.length] = (c1 << 2) | ((c2 & 0x30) >> 4);

        do {
            c3 = data.charCodeAt(i++) & 0xff;

            if(c3 == 61) {
                return this.getString(buffer);
            }
            c3 = this.DECODE_TABLE[c3];
        } 
        while(i < length && c3 == -1);

        if(c3 == -1) {
            break;
        }

        buffer[buffer.length] = ((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2);

        do {
            c4 = data.charCodeAt(i++) & 0xff;

            if(c4 == 61) {
                return this.getString(buffer);
            }
            c4 = this.DECODE_TABLE[c4];
        } 
        while(i < length && c4 == -1);

        if(c4 == -1) {
            break;
        }
        buffer[buffer.length] = ((c3 & 0x03) << 6) | c4;
    }
    return this.getString(buffer);
};

Base64.getBytes = function(data) {
    var c;
    var buffer = [];

    for(var i = 0, length = data.length; i < length; i++) {
        c = data.charCodeAt(i);

        if((c >= 0x0001) && (c <= 0x007F)) {
            buffer[buffer.length] = c;
        }
        else if(c > 0x07FF) {
            buffer[buffer.length] = (0xE0 | ((c >> 12) & 0x0F));
            buffer[buffer.length] = (0x80 | ((c >> 6) & 0x3F));
            buffer[buffer.length] = (0x80 | ((c >> 0) & 0x3F));
        }
        else {
            buffer[buffer.length] = (0xC0 | ((c >> 6) & 0x1F));
            buffer[buffer.length] = (0x80 | ((c >> 0) & 0x3F));
        }
    }
    return buffer;
};

Base64.getString = function(data) {
    var c;
    var c1, c2;
    var buffer = [];

    for(var i = 0, length = data.length; i < length;) {
        c = data[i++];

        switch(c >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7: {
                buffer[buffer.length] = String.fromCharCode(data[i - 1]);
                break;
            }
            case 12:
            case 13: {
                c1 = data[i++];
                buffer[buffer.length] = String.fromCharCode(((c & 0x1F) << 6) | (c1 & 0x3F));
                break;
            }
            case 14: {
                c1 = data[i++];
                c2 = data[i++];
                buffer[buffer.length] = String.fromCharCode(((c & 0x0F) << 12) | ((c1 & 0x3F) << 6) | ((c2 & 0x3F) << 0));
                break;
            }
        }
    }
    return buffer.join("");
};
