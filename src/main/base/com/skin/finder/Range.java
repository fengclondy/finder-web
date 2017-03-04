/*
 * $RCSfile: Range.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-1-6 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: Range</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Range {
    /**
     * start position
     * [start, end)
     */
    public long start;
    
    /**
     * end position
     * [start, end)
     */
    public long end;

    /**
     * file length
     */
    public long length;

    /**
     * @param start
     * @param end
     * @param length
     */
    public Range(long start, long end, long length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }

    /**
     * @return long
     */
    public long getSize() {
        return (this.end - this.start + 1);
    }

    /**
     * @param request
     * @param length
     * @return String
     */
    public static Range parse(HttpServletRequest request, long length) {
        return parse(request.getHeader("Range"), length);
    }
    
    /**
     * ranges-specifier = byte-ranges-specifier
     * byte-ranges-specifier = bytes-unit "=" byte-range-set
     * byte-range-set = 1#( byte-range-spec | suffix-byte-range-spec )
     * byte-range-spec = first-byte-pos "-" [last-byte-pos]
     * suffix-byte-range-spec= "-" last-byte-pos
     * first-byte-pos = 1*DIGIT
     * last-byte-pos = 1*DIGIT
     * 
     * @param header
     * @param length
     * @return Range
     */
    public static Range parse(String header, long length) {
        String range = header;

        if(range != null) {
            long start = -1;
            long end = -1;

            if(range.startsWith("bytes=")) {
                range = range.substring(6).trim();
            }

            if(range.endsWith("-")) {
                try {
                    start = Long.parseLong(range.substring(0, range.length() - 1), 10);
                    end = (length - 1);
                }
                catch(NumberFormatException e) {
                }
            }
            else if(range.startsWith("-")) {
                try {
                    start = length - Long.parseLong(range.substring(1), 10);
                    end = (length - 1);
                }
                catch(NumberFormatException e) {
                }
            }
            else {
                String[] a = range.split("-");

                if(a.length > 1) {
                    try {
                        start = Long.parseLong(a[0], 10);
                    }
                    catch(NumberFormatException e) {
                    }

                    try {
                        end = Long.parseLong(a[1], 10);
                    }
                    catch(NumberFormatException e) {
                    }

                    if(start == -1) {
                        start = length - end;
                        end = length - 1;
                    }

                    if(end == -1) {
                        end = length - 1;
                    }
                }
            }

            if(start == -1 || end == -1 || start > end || end > length) {
                return null;
            }
            return new Range(start, end, length);
        }
        return null;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return "bytes=" + this.start + "-" + this.end;
    }

    /**
     * @return String
     */
    public String getContentRange() {
        return "bytes " + this.start + "-" + this.end + "/" + this.length;
    }
}
