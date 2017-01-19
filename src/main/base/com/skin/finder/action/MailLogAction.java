/*
 * $RCSfile: MailLogAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2015-2-5 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.finder.manager.MailLogManager;
import com.skin.finder.model.MailLog;
import com.skin.finder.util.CurrentUser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.Response;
import com.skin.j2ee.util.ScrollPage;
import com.skin.util.StringUtil;
/**
 * <p>Title: MailLogAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MailLogAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/system/mail/log.html")
    public void list() throws ServletException, IOException {
        List<MailLog> mailLogList = null;
        long appId = CurrentUser.getAppId(this.request);
        long userId = CurrentUser.getUserId(this.request);
        String customerCode = this.getParameter("customerCode", "");
        ScrollPage<MailLog> page = this.getScrollPage(MailLog.class);

        try {
            MailLogManager mailLogManager = new MailLogManager();

            if(!StringUtil.isBlank(customerCode)) {
                mailLogManager.getListByUserId(appId, userId, page);
            }
            else {
                mailLogManager.getListByUserId(appId, userId, page);
            }

            mailLogList = page.getItems();
            this.setAttribute("scrollPage", page);
            this.setAttribute("customerCode", customerCode);
            this.setAttribute("mailLogList", mailLogList);
        }
        catch(Throwable t) {
            throw this.error(t);
        }
        this.forward("/template/system/mail/log.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/system/maillog/preview.html")
    public void preview() throws ServletException, IOException {
        long logId = this.getLong("logId", 0L);
        long appId = CurrentUser.getAppId(this.request);

        try {
            MailLog mailLog = null;
            MailLogManager mailLogManager = new MailLogManager();

            if(logId > 0L) {
                mailLog = mailLogManager.getById(appId, logId);
            }

            if(mailLog == null) {
                Response.write(this.request, this.response, "<h3 style=\"color: #ff0000;\">日志不存在！</h3>");
                return;
            }
            Response.write(this.request, this.response, mailLog.getContent());
        }
        catch(Throwable t) {
            throw this.error(t);
        }
    }
}
