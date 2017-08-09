/*
 * $RCSfile: UserManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2017-08-05 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

/**
 * <p>Title: UserManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public interface UserManager {
    /**
     * @param userName
     * @return User
     */
    User getByName(String userName);

    /**
     * @param user
     * @return int
     */
    int create(User user);

    /**
     * @param user
     * @return int
     */
    int update(User user);

    /**
     * @param userName
     * @return int
     */
    int delete(String userName);
}
