/*
 * $RCSfile: MailConfigAction.java,v $$
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.service.AngleService;
import com.skin.finder.util.CurrentUser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.mail.Mail;
import com.skin.j2ee.mail.MailSender;
import com.skin.j2ee.util.JsonUtil;
/**
 * <p>Title: MailConfigAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class MailConfigAction extends BaseAction {
    /**
     * mail config
     */
    public static final String MAIL_CONFIG_NAME = "skin.config.mail";
    private static final Logger logger = LoggerFactory.getLogger(MailConfigAction.class);

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/system/mail/config.html")
    public void index() throws ServletException, IOException {
        long appId = CurrentUser.getAppId(this.request);

        try {
            Properties properties = AngleService.getProperties(appId, MAIL_CONFIG_NAME);
            String sender = properties.getProperty("skin.mail.sender");
            String userName = properties.getProperty("skin.mail.userName");
            String password = properties.getProperty("skin.mail.password");
            String smtpHost = properties.getProperty("skin.mail.smtp.host");
            String auth = properties.getProperty("skin.mail.smtp.auth");

            /**
             * 隐藏密码
             */
            if(password != null && password.trim().length() > 0) {
                password = "******";
            }

            Map<String, String> config = new HashMap<String, String>();
            config.put("smtpHost", smtpHost);
            config.put("sender", sender);
            config.put("userName", userName);
            config.put("password", password);
            config.put("auth", ((auth != null && auth.equals("true")) ? "1" : "0"));
            this.setAttribute("config", config);
        }
        catch(Throwable t) {
            throw this.error(t);
        }
        this.forward("/template/system/mail/config.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/system/mail/save.html")
    public void save() throws ServletException, IOException {
        long appId = CurrentUser.getAppId(this.request);
        String sender = this.getTrimString("sender");
        String userName = this.getTrimString("userName");
        String password = this.getTrimString("password");
        String smtpHost = this.getTrimString("smtpHost");
        String auth = this.getTrimString("auth", "1");

        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("skin.mail.sender", sender);
        map.put("skin.mail.userName", userName);
        map.put("skin.mail.password", password);
        map.put("skin.mail.smtp.host", smtpHost);
        map.put("skin.mail.smtp.auth", (auth.equals("1") ? "true" : "false"));

        try {
            /**
             * 页面上不会显示已经保存的密码
             * 所以如果密码为"******"或者为空则使用之前的密码
             */
            if(password.length() < 1 || password.equals("******")) {
                Properties properties = AngleService.getProperties(appId, MAIL_CONFIG_NAME);
                map.put("skin.mail.password", properties.getProperty("skin.mail.password"));
            }
            AngleService.save(appId, MAIL_CONFIG_NAME, map);
            JsonUtil.success(this.request, this.response, true);
            return;
        }
        catch(Throwable t) {
            logger.error(t.getMessage(), t);
            JsonUtil.error(this.request, this.response, "error");
            return;
        }
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/system/mail/test.html")
    public void test() throws ServletException, IOException {
        String sender = this.getParameter("sender", "");
        String receiver = this.getParameter("receiver", "");
        String userName = this.getParameter("userName", "");
        String password = this.getParameter("password", "");
        String smtpHost = this.getParameter("smtpHost", "");
        String auth = this.getParameter("auth", "1");
        String subject = "这是一个测试邮件";
        String content = "<h1 style=\"color: #ff0000;\">这是一个测试邮件</h1>";

        Mail mail = new Mail();
        mail.setSmtpHost(smtpHost);
        mail.setFrom(sender);
        mail.setTo(receiver);
        mail.setSubject(subject);
        mail.setContent(content);
        mail.setUserName(userName);
        mail.setPassword(password);
        mail.setAuth((auth != null && auth.equals("1")));

        if(logger.isDebugEnabled()) {
            logger.debug("send mail - sender: {}, to: {}, subject: {}", new Object[]{sender, receiver, subject});
        }

        try {
            MailSender.send(mail);
            JsonUtil.success(this.request, this.response, true);
            return;
        }
        catch (Throwable t) {
            logger.error(t.getMessage(), t);
            JsonUtil.error(this.request, this.response, "error");
            return;
        }
    }
}
