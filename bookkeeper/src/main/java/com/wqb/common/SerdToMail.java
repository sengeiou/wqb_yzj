package com.wqb.common;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SerdToMail {
    // 发送邮件服务器ip和端口
    private String mailServerHost;
    private String mailServerPort = "25";
    // 邮件发送者地址
    private String fromAddress;
    // 邮件接收者地址
    private String toAddress;
    // 登录邮件服务器的用户名和密码
    private String userName;
    private String password;
    // 需要身份验证
    private boolean validate = true;
    // 邮件主题
    private String subject;
    // 邮件内容
    private String context;
    // 邮件附件名
    private String[] attachFileName;

    // 邮件发送属性
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        p.put("mail.transport.protocol", "smtp");
        return p;
    }

    /*
     * 发送邮件
     */
    @SuppressWarnings("static-access")
    public void sendMail() throws Exception {
        // 获得会话
        Session session = Session.getDefaultInstance(getProperties(),
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
        // 输出调试
        session.setDebug(true);
        // 邮件信息
        Message message = new MimeMessage(session);
        // 邮件发送者地址
        Address from = new InternetAddress(fromAddress);
        // 设置邮件发送者
        message.setFrom(from);
        // 邮件接收者地址
        Address to = new InternetAddress(toAddress);
        // 设置邮件接收者
        message.setRecipient(Message.RecipientType.TO, to);
        // 设置主题
        message.setSubject(subject);
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置内容
        message.setText(context);

        Transport trans = session.getTransport();
        trans.connect(mailServerHost, userName, password);
        trans.send(message);
        trans.close();
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String[] getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String[] attachFileName) {
        this.attachFileName = attachFileName;
    }

}
