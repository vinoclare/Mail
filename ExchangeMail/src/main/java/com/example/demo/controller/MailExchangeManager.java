package com.example.demo.controller;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import java.net.URI;


public class MailExchangeManager {

    /**
     *
     * @param username  发件人用户名
     * @param password  发件人密码
     * @param sendAddress  EWS服务地址
     * @param to    收件人数组
     * @param cc    抄送人数组
     * @param subject   主题
     * @param content   内容
     * @param filePath  附件路径
     * @return  发送结果
     */
    public boolean sendMail(String username,String password,String sendAddress,String[] to, String[] cc,
                            String subject, String content, String[] filePath)  {
        boolean result=true;
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
        ExchangeCredentials credentials = new WebCredentials(username, password);
        service.setCredentials(credentials);




        try {
            service.autodiscoverUrl("vinoclare7@outlook.com");
            EmailMessage msg = new EmailMessage(service);
            msg.setSubject(subject);
            MessageBody body = MessageBody.getMessageBodyFromText(content);
            body.setBodyType(BodyType.HTML);
            msg.setBody(body);
            //多个收件人
            for (String toPerson : to) {
                msg.getToRecipients().add(toPerson);
            }
            // 多个抄送
            if (cc != null) {
                for (String ccPerson : cc) {
                    msg.getCcRecipients().add(ccPerson);
                }
            }
            // 多个附件
            if (filePath != null) {
                for (String attachmentPath : filePath) {
                    msg.getAttachments().addFileAttachment(attachmentPath);
                }
            }
            msg.send();
        } catch (Exception e) {
            result=false;
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        MailExchangeManager mailExchangeManager = new MailExchangeManager();
        String[] to={"213193004@seu.edu.cn"};
        String[] cc={"vinoclare7@gmail.com","vinoclare7@163.com"};
        String[] filePath={"D:\\OneDrive - 东南大学\\实习\\邮箱\\ExchangeMail\\src\\main\\resources\\static\\final_processed.xlsx",
                "D:\\OneDrive - 东南大学\\实习\\邮箱\\ExchangeMail\\src\\main\\resources\\static\\导入数据Rules.txt"};
        mailExchangeManager.sendMail("vinoclare7@outlook.com","a20011225a",
                "vinoclare7@outlook.com",to,cc,
                "Exchange测试邮件","文本内容",filePath);
        System.out.println("邮件发送成功！");
    }

}
