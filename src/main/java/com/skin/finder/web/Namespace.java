/*
 * $RCSfile: Namespace.java,v $
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

/**
 * <p>Title: Namespace</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Namespace {
    /**
     * @return String
     */
    public abstract String value();
}
