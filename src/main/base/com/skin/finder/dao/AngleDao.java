/*
 * $RCSfile: AngleDao.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-02-06 09:57:22 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.skin.finder.model.Angle;
import com.skin.j2ee.dao.DAOException;
import com.skin.j2ee.dao.Dao;
import com.skin.j2ee.dao.Insert;

/**
 * <p>Title: AngleDao</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author yankui.cyk
 * @version 1.0
 */
public class AngleDao extends Dao<Angle, Long> {
    /**
     * default
     */
    public AngleDao() {
    }

    /**
     * @param connection
     */
    public AngleDao(Connection connection) {
        super(connection);
    }

    /**
     * @param id
     * @return Angle
     */
    public Angle getById(long id) {
        return this.getBean("select * from skin_angle where angle_id=?", new Object[]{id});
    }

    /**
     * @param appId
     * @param angleName
     * @return Angle
     */
    public Angle getByName(long appId, String angleName){
        return this.getBean("select * from skin_angle where app_id=? and angle_name=?", new Object[]{appId, angleName});
    }

    /**
     * @param angle
     * @return int
     */
    public int insert(Angle angle) {
        try {
            Insert insert = new Insert("skin_angle", angle, true);
            insert.remove("angleId");
            return insert.execute(this.getConnection());
        }
        catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * @param angleId
     * @param content
     * @param updateTime
     * @return int
     */
    public int update(long angleId, String content, Date updateTime) {
        if(angleId > 0L) {
            return this.update("update skin_angle set content=?, update_time=? where angle_id=?", content, updateTime, angleId);
        }
        return 0;
    }

    /**
     * @param angleId
     * @return int
     */
    public int delete(long angleId) {
        if(angleId > 0L) {
            return this.update("delete from skin_angle where angle_id=?", angleId);
        }
        return 0;
    }

    /**
     * @param resultSet
     * @return Angle
     * @throws SQLException 
     */
    @Override
    public Angle getBean(ResultSet resultSet) throws SQLException {
        Angle angle = new Angle();
        angle.setAngleId(resultSet.getLong("angle_id"));
        angle.setAppId(resultSet.getLong("app_id"));
        angle.setAngleName(resultSet.getString("angle_name"));
        angle.setCatalog(resultSet.getString("catalog"));
        angle.setContent(resultSet.getString("content"));
        angle.setCreateTime(resultSet.getTimestamp("create_time"));
        angle.setUpdateTime(resultSet.getTimestamp("update_time"));
        return angle;
    }
}