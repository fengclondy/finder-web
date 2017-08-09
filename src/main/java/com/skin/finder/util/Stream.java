/*
 * $RCSfile: Stream.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-11-08 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * <p>Title: Stream</p>
 * <p>Description: 非线程安全的文本流</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Stream implements Closeable {
    private int index;
    private int count;
    private int line;
    private int column;
    private char[] buffer;
    private Reader reader;
    private StringBuilder stringBuffer;

    /**
     * 
     */
    public static final int EOF = -1;

    /**
     * @param file
     * @param charset
     * @param bufferSize
     * @return Stream
     * @throws IOException
     */
    public static Stream getStream(File file, String charset, int bufferSize) throws IOException {
        return getStream(file.toURI().toURL(), charset, bufferSize);
    }

    /**
     * @param url
     * @param charset
     * @param bufferSize
     * @return Stream
     * @throws IOException
     */
    public static Stream getStream(URL url, String charset, int bufferSize) throws IOException {
        return getStream(url.openStream(), charset, bufferSize);
    }

    /**
     * @param inputStream
     * @param charset
     * @param bufferSize
     * @return Stream
     * @throws IOException
     */
    public static Stream getStream(InputStream inputStream, String charset, int bufferSize) throws IOException {
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        return new Stream(reader, bufferSize);
    }

    /**
     * @param reader
     */
    public Stream(Reader reader) {
        this(reader, 8192);
    }

    /**
     * @param reader
     * @param bufferSize
     */
    public Stream(Reader reader, int bufferSize) {
        this.index = 0;
        this.count = 0;
        this.reader = reader;
        this.buffer = new char[bufferSize];
        this.line = 1;
        this.column = 1;
    }

    /**
     * @return int
     * @throws IOException
     */
    public int read() throws IOException {
        if(this.count < 0) {
            return EOF;
        }

        if(this.index < this.count) {
            int i = this.buffer[this.index++];

            if(i == '\n') {
                this.line++;
                this.column = 1;
            }
            else {
                this.column++;
            }
            return i;
        }

        this.fill();

        if(this.index < this.count) {
            int i = this.buffer[this.index++];

            if(i == '\n') {
                this.line++;
                this.column = 1;
            }
            else {
                this.column++;
            }
            return i;
        }
        else {
            return EOF;
        }
    }

    /**
     * @param cbuf
     * @param offset
     * @param length
     * @return int
     * @throws IOException
     */
    public int read(char[] cbuf, int offset, int length) throws IOException {
        if(this.count - this.index <= length) {
            System.arraycopy(this.buffer, this.index, cbuf, offset, length);
            this.index += length;

            char c;
            for(int i = offset; i < offset + length; i++) {
                c = cbuf[i];

                if(c == '\n') {
                    this.line++;
                    this.column = 1;
                }
                else {
                    this.column++;
                }
            }
            return length;
        }
        return EOF;
    }

    /**
     * @return String
     * @throws IOException
     */
    public String readLine() throws IOException {
        int c = this.read();

        if(c == EOF) {
            return null;
        }

        if(c == 0x0A) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();

        if(c != '\r') {
            buffer.append((char)c);
        }

        while((c = this.read()) != EOF) {
            if(c == 0x0A) {
                break;
            }

            if(c != '\r') {
                buffer.append((char)c);
            }
        }
        return buffer.toString();
    }

    /**
     * @return int
     * @throws IOException
     */
    public int peek() throws IOException {
        /**
         * 绝大多数情况下只需要一次检查就够了
         * peek(int offset)会检查参数并计算新位置
         */
        if(this.index < this.count) {
            return this.buffer[this.index];
        }
        return this.peek(0);
    }

    /**
     * 获取当前位置以后的数据, offset >= 0
     * offset = 0时就是当前位置的数据
     * 该方法只是预读, 不会改变数据指针
     * 并且该方法会缓存预读的数据
     * @param offset
     * @return int
     * @throws IOException
     */
    public int peek(int offset) throws IOException {
        if(this.count < 0) {
            return EOF;
        }

        if(offset < 0) {
            throw new java.lang.IllegalArgumentException("offset must be >= 0");
        }

        /**
         * 实际请求位置
         */
        int i = this.index + offset;

        if(i < this.count) {
            return this.buffer[i];
        }

        /**
         * 需要读取新数据
         * 先将旧的数据移动到buffer的起始位置
         */
        if(this.index > 0) {
            if(this.count - this.index > 0) {
                System.arraycopy(this.buffer, this.index, this.buffer, 0, this.count - this.index);
            }
            this.count = this.count - this.index;
            this.index = 0;
        }

        /**
         * 如果请求位置超出了buffer容量
         */
        if(offset > this.buffer.length) {
            /**
             * 扩容, 确保容量超过请求位置
             * 新的容量大小是当前bufferSize的倍数
             */
            int bufferSize = (offset / this.buffer.length + 1) * this.buffer.length;
            char[] cbuf = new char[bufferSize];
            System.arraycopy(this.buffer, 0, cbuf, 0, this.count);
            this.buffer = cbuf;
        }

        /**
         * 读取新数据
         */
        int length = this.reader.read(this.buffer, this.count, this.buffer.length - this.count);

        if(length > 0) {
            this.count = this.count + length;
        }

        if(offset < this.count) {
            return this.buffer[offset];
        }
        return EOF;
    }

    /**
     * @param c
     * @return boolean
     * @throws IOException
     */
    public boolean next(char c) throws IOException {
        if(this.peek(0) == c) {
            this.read();
            return true;
        }
        return false;
    }

    /**
     * @param text
     * @return boolean
     * @throws IOException
     */
    public boolean match(String text) throws IOException {
        /**
         * 依次匹配
         */
        for(int i = 0, length = text.length(); i < length; i++) {
            if(text.charAt(i) != this.peek(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param text
     * @param ignoreCase
     * @return boolean
     * @throws IOException
     */
    public boolean match(String text, boolean ignoreCase) throws IOException {
        if(!ignoreCase) {
            return this.match(text);
        }

        int b;

        /**
         * 依次匹配
         */
        for(int i = 0, length = text.length(); i < length; i++) {
            b = this.peek(i);

            if(b == EOF) {
                return false;
            }

            if(Character.toLowerCase(text.charAt(i)) != Character.toLowerCase((char)b)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param test
     * @param ignoreCase
     * @return String
     * @throws IOException
     */
    public boolean tryread(String test, boolean ignoreCase) throws IOException {
        if(this.match(test, ignoreCase)) {
            this.skip(test.length());
            return true;
        }
        return false;
    }

    /**
     * @param c
     * @return String
     * @throws IOException
     */
    public String readUntil(char c) throws IOException {
        int i = 0;
        StringBuilder buffer = this.getStringBuffer();

        while((i = this.read()) != EOF) {
            if(i == c) {
                return buffer.toString();
            }
            else {
                buffer.append((char)i);
            }
        }
        return null;
    }

    /**
     * @param s
     * @return String
     * @throws IOException
     */
    public String readUntil(String s) throws IOException {
        int i;
        StringBuilder buffer = this.getStringBuffer();

        while(!this.match(s)) {
            i = this.read();

            if(i == EOF) {
                return null;
            }
            buffer.append((char)i);
        }
        this.skip(s.length());
        return buffer.toString();
    }

    /**
     * @param length
     * @throws IOException
     */
    public void skip(int length) throws IOException {
        for(int i = 0; i < length; i++) {
            this.read();
        }
    }

    /**
     * @param c
     * @return String
     * @throws IOException
     */
    public boolean skipUntil(char c) throws IOException {
        int i = 0;

        while((i = this.read()) != EOF) {
            if(i == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * skip line
     * @throws IOException
     */
    public void skipLine() throws IOException {
        int i;

        while((i = this.read()) != EOF) {
            if(i == '\n') {
                break;
            }
        }
    }

    /**
     * skip crlf
     * @throws IOException
     */
    public void skipCRLF() throws IOException {
        int i;

        while((i = this.peek()) != EOF) {
            if(i == '\r' || i == '\n') {
                this.read();
            }
            else {
                break;
            }
        }
    }

    /**
     * @throws IOException
     */
    public void skipWhitespace() throws IOException {
        int i;

        while((i = this.peek()) != EOF) {
            if(i <= ' ') {
                this.read();
            }
            else {
                break;
            }
        }
    }

    /**
     * @return int
     * @throws IOException
     */
    private int fill() throws IOException {
        this.index = 0;
        this.count = this.reader.read(this.buffer, 0, this.buffer.length);
        return this.count;
    }

    /**
     * @return boolean
     * @throws IOException
     */
    public boolean eof() throws IOException {
        return (this.peek(0) == EOF);
    }

    /**
     * @return int
     */
    public int getLine() {
        return this.line;
    }

    /**
     * @return int
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * @return int
     */
    public int getBufferSize() {
        return this.buffer.length;
    }

    /**
     * @return StringBuffer
     */
    private StringBuilder getStringBuffer() {
        if(this.stringBuffer == null) {
            this.stringBuffer = new StringBuilder();
        }
        else {
            this.stringBuffer.setLength(0);
        }
        return this.stringBuffer;
    }

    /**
     * @return String
     */
    public String getBufferedString() {
        if(this.count >= 0 && this.count >= this.index) {
            return new String(this.buffer, this.index, this.count - this.index);
        }
        else {
            return null;
        }
    }

    /**
     * @param length
     * @return String
     */
    public String getBufferedString(int length) {
        if(this.count >= 0 && this.count >= this.index) {
            if(this.count - this.index > length) {
                return new String(this.buffer, this.index, length);
            }
            else {
                return new String(this.buffer, this.index, this.count - this.index);
            }
        }
        else {
            return null;
        }
    }

    /**
     * close the stream
     */
    @Override
    public void close() {
        if(this.reader != null) {
            try {
                this.reader.close();
            }
            catch (IOException e) {
            }
        }
    }
}
