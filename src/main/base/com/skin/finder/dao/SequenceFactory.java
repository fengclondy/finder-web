/*
 * $RCSfile: SequenceFactory.java,v $$
 * $Revision: 1.1  $
 * $Date: 2012-10-31  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>Title: SequenceFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SequenceFactory {
    private static final Sequence sequence = new DefaultSequence();

    /**
     * @param connection
     * @param name
     * @return long
     * @throws SQLException
     */
    public static long next(Connection connection, String name) throws SQLException {
        return sequence.next(connection, name);
    }
}
