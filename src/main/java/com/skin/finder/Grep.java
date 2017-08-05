/*
 * $RCSfile: Grep.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

/**
 * <p>Title: Grep</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Grep {
    /**
     * grep
     */
    private Grep() {
    }

    /**
     * @param raf
     * @param keyword
     * @param regexp
     * @param position
     * @param rows
     * @param charset
     * @return FileRange
     * @throws IOException
     */
    public static FileRange find(RandomAccessFile raf, String keyword, boolean regexp, long position, int rows, String charset) throws IOException {
        long start = position;
        long length = raf.length();

        if((start + 1) >= length) {
            FileRange range = new FileRange();
            range.setStart(length);
            range.setEnd(length);
            range.setCount(0L);
            range.setLength(length);
            range.setRows(0);
            range.setCharset(charset);
            return range;
        }

        if(start < 0) {
            start = 0;
        }

        byte LF = 0x0A;
        int readBytes = 0;
        int bufferSize = (int)(Math.min(length - start, 8L * 1024L));
        byte[] buffer = new byte[bufferSize];
        raf.seek(start);

        /**
         * 该方法总是从position处先查找第一个换行符的位置, 然后才开始读取数据, 并从第一个换行符以后的位置开始计算有效行数
         * 客户端允许从进度条点击一个文件位置, 此时传过来的position可能是文件的任意位置
         * 所以此处根据position查找出当前位置所在行的结束位置, 也就是说总是从请求位置的下一行开始获取内容.
         * 一般情况下, 服务端返回的end位置总是位于换行符的位置, 下次请求时传过来的start是上次返回的end位置
         * end总是处于换行符的位置的好处是刚好兼容此处的处理, 否则就需要实现两套逻辑.
         */
        if(start > 0) {
            boolean flag = false;

            while((readBytes = raf.read(buffer, 0, bufferSize)) > 0) {
                for(int i = 0; i < readBytes; i++) {
                    if(buffer[i] == LF) {
                        start = start + i + 1;
                        flag = true;
                        break;
                    }
                }

                if(flag) {
                    break;
                }
                else {
                    start += readBytes;
                }
            }

            if(flag) {
                raf.seek(start);
            }
            else {
                FileRange range = new FileRange();
                range.setStart(length - 1);
                range.setEnd(length - 1);
                range.setLength(length);
                range.setRows(0);
                range.setCharset(charset);
                return range;
            }
        }

        int count = 0;
        byte[] bytes = null;
        boolean match = false;
        ByteArrayOutputStream tmp = new ByteArrayOutputStream(); 
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Pattern pattern = (regexp ? Pattern.compile(keyword) : null);

        while((bytes = readLine(raf, buffer, tmp)) != null) {
            String line = new String(bytes, 0, bytes.length, charset);

            if(pattern != null) {
                match = pattern.matcher(line).find();
            }
            else {
                match = (line.indexOf(keyword) > -1);
            }

            if(match) {
                bos.write(bytes, 0, bytes.length);
                count++;
            }

            /**
             * 重置缓存
             */
            tmp.reset();

            if(count >= rows) {
                break;
            }
        }

        byte[] result = bos.toByteArray();
        FileRange range = new FileRange();
        range.setStart(start);
        range.setEnd(raf.getFilePointer());
        range.setCount(result.length);
        range.setLength(length);
        range.setRows(count);
        range.setBuffer(result);
        range.setCharset(charset);
        return range;
    }

    /**
     * 这是一个私有方法，为了提升性能要求第一个参数必须是BufferedInputStream
     * byteArrayOutputStream外部传入，这样不需要每次调用都重新new一个
     * @param bufferedInputStream
     * @param byteArrayOutputStream
     * @return byte[]
     * @throws IOException
     */
    private static byte[] readLine(RandomAccessFile raf, byte[] buffer, ByteArrayOutputStream bos) throws IOException {
        int length = 0;
        int bufferSize = buffer.length;
        long position = raf.getFilePointer();

        while((length = raf.read(buffer, 0, bufferSize)) > -1) {
            int k = indexOf(buffer, 0x0A, length);

            if(k > -1) {
                raf.seek(position + bos.size() + k + 1);
                bos.write(buffer, 0, k + 1);
                break;
            }
            else {
                bos.write(buffer, 0, length);
            }
        }

        if(length < 0 && bos.size() < 1) {
            return null;
        }
        return bos.toByteArray();
    }

    /**
     * @param bytes
     * @param b
     * @return int
     */
    private static int indexOf(byte[] bytes, int b, int length) {
        for(int i = 0; i < length; i++) {
            if(bytes[i] == b) {
                return i;
            }
        }
        return -1;
    }
}
