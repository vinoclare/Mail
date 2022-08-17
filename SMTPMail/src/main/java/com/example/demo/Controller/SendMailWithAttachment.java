package com.example.demo.Controller;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class SendMailWithAttachment {

    public static void main(String[] args) throws Exception {
        final String smtp = "smtp.qq.com";
        final String username = "3213730851@qq.com";
        final String password = "fogsipfmqugjdecb";
        final String from = "3213730851@qq.com";
        final String to = "vinoclare7@gmail.com";
        SendMail sender = new SendMail(smtp, username, password);
        Session session = sender.createTLSSession();
        try (InputStream input = SendMailWithAttachment.class.getResourceAsStream("/static/1.jpg")) {
            Message message = createMessageWithAttachment(session, from, to, "Java邮件带附件",
                    "<h1>Hello</h1><p>这是一封带附件的<u>javamail</u>邮件！</p>", "javamail.jpg", input);
            Transport.send(message);
        }
    }

    /**
     *
     * @param session    会话名称
     * @param from       发件邮箱地址
     * @param to         收件邮箱地址
     * @param subject    邮件主题
     * @param body       邮件正文
     * @param fileName   附件名称
     * @param input      输入流
     * @return    message
     */
    static Message createMessageWithAttachment(Session session, String from, String to, String subject, String body,
                                               String fileName, InputStream input) throws MessagingException, IOException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject, "UTF-8");
        Multipart multipart = new MimeMultipart();
        // 添加text:
        BodyPart textpart = new MimeBodyPart();
        textpart.setContent(body, "text/html;charset=utf-8");
        multipart.addBodyPart(textpart);
        // 添加image:
        BodyPart imagepart = new MimeBodyPart();
        imagepart.setFileName(fileName);
        imagepart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "application/octet-stream")));
        multipart.addBodyPart(imagepart);
        message.setContent(multipart);
        return message;
    }

}

