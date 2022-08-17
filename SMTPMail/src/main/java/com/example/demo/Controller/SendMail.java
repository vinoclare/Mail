package com.example.demo.Controller;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    final String smtpHost;
    final String username;
    final String password;
    final boolean debug;

    public SendMail(String smtpHost, String username, String password) {
        this.smtpHost = smtpHost;
        this.username = username;
        this.password = password;
        this.debug = true;
    }

    public static void main(String[] args) throws Exception {
        // 服务器地址（QQ）
        final String smtp = "smtp.qq.com";
        // 用户登录名
        final String username = "3213730851@qq.com";
        // 邮箱授权码（不是密码）
        final String password = "fogsipfmqugjdecb";
        // 发送方地址
        final String from = "3213730851@qq.com";
        // 接收方地址
        final String to = "213193004@seu.edu.cn";
        SendMail sender = new SendMail(smtp, username, password);
        Session session = sender.createTLSSession();
        Message message = createTextMessage(session, from, to, "JavaMail邮件", "这是一封来自javamail的邮件！");
        Transport.send(message);
    }

    // 建立SSL会话
    Session createSSLSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.smtpHost); // SMTP主机名
        props.put("mail.smtp.port", "465"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.auth.mechanisms", "NTLM");
        // 启动SSL:
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        Session session = Session.getInstance(props, new Authenticator() {
            // 用户名+口令认证:
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
            }
        });
        session.setDebug(this.debug); // 显示调试信息
        return session;
    }

    // 建立TLS会话
    Session createTLSSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.smtpHost); // SMTP主机名
        props.put("mail.smtp.port", "587"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
            }
        });
        session.setDebug(this.debug); // 显示调试信息
        return session;
    }

    // 建立不安全会话
    Session createInsecureSession(String host, String username, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.host", this.smtpHost); // SMTP主机名
        props.put("mail.smtp.port", "25"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.auth.mechanisms", "NTLM");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SendMail.this.username, SendMail.this.password);
            }
        });
        session.setDebug(this.debug); // 显示调试信息
        return session;
    }

    /**
     * 发送文本信息
     * @param session 会话名称
     * @param from    发件邮箱地址
     * @param to      收件邮箱地址
     * @param subject 邮件主题
     * @param body    邮件正文
     * @return message
      */
    static Message createTextMessage(Session session, String from, String to, String subject, String body)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject, "UTF-8");
        message.setText(body, "UTF-8");
        return message;
    }
}