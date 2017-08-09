/*
 * $RCSfile: JSONParser.java,v $
 * $Revision: 1.1 $
 * $Date: 2014-03-25 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: JSONParser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JSONParser {
    private StringBuilder stringBuilder = new StringBuilder(1024);

    /**
     * 一个极简的JSON解析器
     * 返回的数据类型只有七种: Long, Double, String, Boolean, List, Map, null
     * @param source
     * @return Object
     */
    public Object parse(String source) {
        StringStream stream = new StringStream(source);
        Object result = parse(stream);
        this.skipComment(stream);
        return result;
    }

    /**
     * @param stream
     * @return Object
     */
    public Object parse(StringStream stream) {
        this.skipComment(stream);

        if(stream.eof()) {
            return null;
        }

        int i = stream.peek();

        if(i == '{') {
            stream.read();
            this.skipComment(stream);
            Map<String, Object> map = new LinkedHashMap<String, Object>();

            if(stream.next('}')) {
                return map;
            }

            while(true) {
                String key = this.getKey(stream);
                this.skipComment(stream);

                if(!stream.next(':')) {
                    throw this.error("syntax error, expect ':', key: [" + key + "] but found '" + (char)stream.read() + "'", stream);
                }
                Object value = this.parse(stream);
                map.put(key, value);

                this.skipComment(stream);
                i = stream.read();

                if(i == ',') {
                    this.skipComment(stream);
                    continue;
                }

                if(i == '}') {
                    break;
                }
                throw this.error("syntax error, expect ',' or '}', but found '" + i + "'", stream);
            }
            return map;
        }
        else if(i == '[') {
            stream.read();
            this.skipComment(stream);
            List<Object> list = new ArrayList<Object>();

            if(stream.next(']')) {
                return list;
            }

            while(true) {
                Object value = this.parse(stream);
                list.add(value);

                this.skipComment(stream);
                i = stream.read();

                if(i == ',') {
                    continue;
                }

                if(i == ']') {
                    break;
                }
                throw this.error("syntax error, expect ',' or ']', but found " + (char)i, stream);
            }
            return list;
        }
        else {
            switch(i) {
                case '\'':
                case '"': {
                    return this.getString(stream);
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                case '.':
                case '+': {
                    return this.getNumber(stream);
                }
                case 't': {
                    return this.getTrue(stream);
                }
                case 'f': {
                    return this.getFalse(stream);
                }
                case 'n': {
                    return this.getNull(stream);
                }
                case 'u': {
                    return this.getUndefined(stream);
                }
                default: {
                    throw this.error("syntax error, unknown literal: " + stream.getRemain(10), stream);
                }
            }
        }
    }

    /**
     * @param stream
     */
    public void skipComment(StringStream stream) {
        int i = 0;
        stream.skipWhitespace();

        while(!stream.eof()) {
            i = stream.read();

            if(i == '/' && stream.next('*')) {
                while(!stream.eof()) {
                    i = stream.read();

                    if(i == '*' && stream.next('/')) {
                        break;
                    }
                }
                stream.skipWhitespace();
            }
            else if(i == '/' && stream.next('/')) {
                stream.find('\n');
                stream.skipWhitespace();
            }
            else if(i > ' ') {
                stream.back();
                break;
            }
        }
    }

    /**
     * @param stream
     * @return String
     */
    public String getKey(StringStream stream) {
        int i = 0;
        int quote = stream.read();

        if(quote != '\'' && quote != '"') {
            quote = ' ';
            stream.back();
        }

        int length = 0;
        int offset = stream.getPosition();

        while((i = stream.read()) != StringStream.EOF) {
            if(i == quote) {
                break;
            }

            if(quote == 32) {
                if(i <= 32 || i == ',' || i == ':' || i == '}' || i == ']') {
                    stream.back();
                    break;
                }
            }
            length++;
        }

        if(length < 1) {
            throw this.error("syntax error, expect key, but key is empty !", stream);
        }
        return stream.getString(offset, length);
    }

    /**
     * @param stream
     * @return String
     */
    public String getString(StringStream stream) {
        int i = 0;
        int quote = stream.read();
        StringBuilder buffer = this.stringBuilder;
        buffer.setLength(0);

        while((i = stream.read()) != StringStream.EOF) {
            if(i == quote) {
                break;
            }

            if(i == 0x0A) {
                throw this.error("syntax error, expect '" + (char)quote + "', but found " + (char)i, stream);
            }

            if(i == '\\') {
                i = stream.read();

                switch (i) {
                    case '\'': {
                        buffer.append("'");
                        break;
                    }
                    case '"': {
                        buffer.append('"');
                        break;
                    }
                    case 'r': {
                        buffer.append('\r');
                        break;
                    }
                    case 'n': {
                        buffer.append('\n');
                        break;
                    }
                    case 't': {
                        buffer.append('\t');
                        break;
                    }
                    case 'b': {
                        buffer.append('\b');
                        break;
                    }
                    case 'f': {
                        buffer.append('\f');
                        break;
                    }
                    case '\\': {
                        buffer.append('\\');
                        break;
                    }
                    default : {
                        buffer.append('\\');
                        buffer.append((char)i);
                        break;
                    }
                }
            }
            else {
                buffer.append((char)i);
            }
        }

        if(i != quote) {
            throw this.error("syntax error, unclosed string", stream);
        }
        return buffer.toString();
    }

    /**
     * @param stream
     * @return Number
     */
    public Number getNumber(StringStream stream) {
        int i = 0;
        int offset = stream.getPosition();

        while((i = stream.read()) != StringStream.EOF) {
            if((i >= '0' && i <= '9') || i == '-' || i == '.' || i == '+') {
                continue;
            }
            else {
                stream.back();
                break;
            }
        }

        int length = stream.getPosition() - offset;
        return this.getNumber(stream.getString(offset, length));
    }

    /**
     * @param stream
     * @return Boolean
     */
    public Boolean getTrue(StringStream stream) {
        if(stream.match("true")) {
            stream.skip(4);
            return Boolean.TRUE;
        }
        throw this.error("syntax error, unknown literal: " + stream.getRemain(10), stream);
    }

    /**
     * @param stream
     * @return Boolean
     */
    public Boolean getFalse(StringStream stream) {
        if(stream.match("false")) {
            stream.skip(5);
            return Boolean.FALSE;
        }
        throw this.error("syntax error, unknown literal: " + stream.getRemain(10), stream);
    }

    /**
     * @param stream
     * @return Object
     */
    public Object getNull(StringStream stream) {
        if(stream.match("null")) {
            stream.skip(4);
            return null;
        }
        throw this.error("syntax error, unknown literal: " + stream.getRemain(10), stream);
    }

    /**
     * @param stream
     * @return Object
     */
    public Object getUndefined(StringStream stream) {
        if(stream.match("undefined")) {
            stream.skip(9);
            return null;
        }
        throw this.error("syntax error, unknown literal: " + stream.getRemain(10), stream);
    }

    /**
     * @param value
     * @return Number
     */
    public Number getNumber(String value) {
        if(value.startsWith("0x")) {
            return Long.valueOf(value, 16);
        }

        int dataType = this.getDataType(value);

        switch(dataType) {
            case 2: {
                return Long.valueOf(value);
            }
            case 3: {
                return Double.valueOf(value);
            }
            case 4: {
                return Double.valueOf(value);
            }
            case 5: {
                return Long.valueOf(value);
            }
            default: {
                return null;
            }
        }
    }

    /**
     * 0 - String
     * 1 - Boolean
     * 2 - Integer
     * 3 - Float
     * 4 - Double
     * 5 - Long
     * @param content
     * @return int
     */
    public int getDataType(String content) {
        String source = content.trim();

        if(source.length() < 1) {
            return 0;
        }

        if(source.equals("true") || source.equals("false")) {
            return 1;
        }

        char c;
        int d = 0;
        int type = 2;
        int length = source.length();

        for(int i = 0; i < length; i++) {
            c = source.charAt(i);

            if(i == 0 && (c == '+' || c == '-')) {
                continue;
            }

            if(c == '.') {
                if(d == 0) {
                    d = 4;
                    continue;
                }
                return 0;
            }

            if(c < 48 || c > 57) {
                if(i == length - 1) {
                    if(c == 'f' || c == 'F') {
                        return 3;
                    }
                    else if(c == 'd' || c == 'D') {
                        return 4;
                    }
                    else if(c == 'l' || c == 'L') {
                        return (d == 0 ? 5 : 0);
                    }
                    else {
                        return 0;
                    }
                }

                if(i == length - 2 && (c == 'e' || c == 'E') && Character.isDigit(source.charAt(length - 1))) {
                    return 4;
                }
                return 0;
            }
        }
        return (d == 0 ? type : d);
    }

    /**
     * @param error
     * @param stream
     * @return RuntimeException
     */
    public RuntimeException error(String error, StringStream stream) {
        return new RuntimeException(error + " at #" + stream.getPosition() + ": " + stream.getRemain(30));
    }
}
