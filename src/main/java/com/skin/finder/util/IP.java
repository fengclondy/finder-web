/*
 * $RCSfile: IP.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-3-4 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>Title: IP</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class IP {
    /**
     * local ip
     */
    public static final String LOCAL = getLocalHostAddress();

    /**
     * @param ip
     * @return byte[]
     */
    public static byte[] getAddress(String ip) {
        String[] parts = ip.split("[.]");
        byte[] bytes = new byte[4];

        if(parts.length == 4) {
            try {
                for(int i = 0; i < 4; i++) {
                    bytes[i] = Byte.parseByte(parts[i].trim());
                }
                return bytes;
            }
            catch(NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * @return String
     */
    public static String getLocalHostAddress() {
        try {
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = InetAddress.getLocalHost().getAddress();

            for(int i = 0; i < bytes.length; i++) {
                buffer.append(bytes[i] & 0xFF).append(".");
            }

            if(buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            return buffer.toString();
        }
        catch(Exception e) {
        }
        return "127.0.0.1";
    }

    /**
     * @return String
     */
    public static String getMacAddress() {
        return getMacAddress(getLocalHostAddress());
    }

    /**
     * @param host
     * @return String
     */
    public static String getMacAddress(String host) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(host));

            if(networkInterface != null) {
                return getMacAddress(networkInterface);
            }
        }
        catch(Exception e) {
        }
        return "";
    }

    /**
     * @return List<String>
     */
    public static List<String> getMacAddressList() {
        List<String> addressList = new ArrayList<String>();

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements()) {
                String address = getMacAddress(networkInterfaces.nextElement());

                if(address.length() > 0) {
                    addressList.add(address);
                }
            }
        }
        catch(Exception e) {
        }
        return addressList;
    }

    /**
     * @param networkInterface
     * @return String
     * @throws SocketException
     */
    public static String getMacAddress(NetworkInterface networkInterface) throws SocketException {
        StringBuffer buffer = new StringBuffer();
        byte[] mac = networkInterface.getHardwareAddress();

        if(mac != null && mac.length > 0) {
            for(byte b : mac) {
                String hexString = Integer.toHexString(b & 0xFF);
                buffer.append((hexString.length() == 1) ? "0" + hexString : hexString).append("-");
            }

            if(buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
        }
        return buffer.toString();
    }

    /**
     * @param ip
     * @param pattern
     * @return String
     */
    public static String format(String ip, String pattern) {
        if(ip == null || pattern == null) {
            return "";
        }

        char[] c1;
        char[] c2;
        String[] a1 = ip.split("\\.");
        String[] a2 = pattern.split("\\.");
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < a1.length; i++) {
            if(i < a2.length) {
                c1 = a1[i].toCharArray();
                c2 = a2[i].toCharArray();

                for(int j = c2.length - 1; j > -1; j--) {
                    if(c2[j] == '#') {
                        if(j < c1.length) {
                            c2[j] = c1[j];
                        }
                        else {
                            c2[j] = '\0';
                        }
                    }
                    else {
                        if(j >= c1.length) {
                            c2[j] = '\0';
                        }
                    }
                }

                for(int j = 0; j < c2.length; j++) {
                    if(c2[j] != '\0') {
                        buffer.append(c2[j]);
                    }
                }
                buffer.append(".");
            }
        }

        if(buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param ip
     * @return long
     */
    public static long toLong(String ip) {
        String[] ips = ip.split("[.]");
        long num = (Long.parseLong(ips[0]) << 24) + (Long.parseLong(ips[1]) << 16) + (Long.parseLong(ips[2]) << 8) + Long.parseLong(ips[3]);
        return num;
    }

    /**
     * @param ip
     * @return String
     */
    public static String toString(long ip) {
        long num = 0;
        long mask[] = {0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000};
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < 4; i++) {
            num = (ip & mask[i]) >> (i * 8);

            if(i > 0) {
                buffer.insert(0, ".");
            }
            buffer.insert(0, String.valueOf(num));
        }
        return buffer.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String ip = "127.123.0.234";
        String pattern = "###.###.##*.*";
        System.out.println(format(ip, pattern));

        Long value = toLong("192.168.0.1");
        System.out.println(value);
        System.out.println(toString(value));

        System.out.println(1L << 8);
        System.out.println(16 * 16);

        System.out.println(1L << 16);
        System.out.println(1024 * 64);

        System.out.println(1L << 24);
        System.out.println(16777216L);
        System.out.println(1024 * 1024 * 16);
        System.out.println("localhost: " + getLocalHostAddress());
        System.out.println("localhost: " + IP.getMacAddress(getLocalHostAddress()));
        System.out.println(IP.getMacAddressList());
        System.out.println(IP.toLong("192.168.1.1") + "-" + IP.toLong("192.168.1.2"));
    }
}
