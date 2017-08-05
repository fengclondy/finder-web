/*
 * $RCSfile: ResourceBundleEnumeration.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-02-26 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * <p>Title: PropertyResourceBundle</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ResourceBundleEnumeration implements Enumeration<String> {
    private Set<String> set;
    private Iterator<String> iterator;
    private Enumeration<String> enumeration;
    private String next = null;

    /**
     * @param set
     * @param enumeration
     */
    public ResourceBundleEnumeration(Set<String> set, Enumeration<String> enumeration) {
        this.set = set;
        this.iterator = set.iterator();
        this.enumeration = enumeration;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean hasMoreElements() {
        if(this.next == null) {
            if(this.iterator.hasNext()) {
                this.next = this.iterator.next();
            }
            else if (this.enumeration != null) {
                while((this.next == null) && (this.enumeration.hasMoreElements())) {
                    this.next = this.enumeration.nextElement();

                    if(this.set.contains(this.next)) {
                        this.next = null;
                    }
                }
            }
        }
        return (this.next != null);
    }

    /**
     * @return String
     */
    @Override
    public String nextElement() {
        if(hasMoreElements()) {
            String str = this.next;
            this.next = null;
            return str;
        }
        throw new NoSuchElementException();
    }
}
