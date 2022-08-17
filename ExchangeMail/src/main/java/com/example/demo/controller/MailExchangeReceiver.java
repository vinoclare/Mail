package com.example.demo.controller;

import java.net.URI;
import java.util.ArrayList;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;

public class MailExchangeReceiver {
    public static void main(String[] args)throws Exception {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        //登录的账号和密码，账号一定是可以登录进去的账号，不一定是邮箱地址
        ExchangeCredentials credentials = new WebCredentials("vinoclare7@outlook.com", "a20011225a");
        service.setCredentials(credentials);
        service.autodiscoverUrl("vinoclare7@outlook.com");
        // 绑定Inbox.
        Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
        System.out.println(inbox.getDisplayName());
        ItemView view = new ItemView(10);
        // 查询
        FindItemsResults<Item> findResults = service.findItems(inbox.getId(), view);
        ArrayList<Item> items = findResults.getItems();
        for(int i=0;i<items.size();i++){
            EmailMessage message = EmailMessage.bind(service, items.get(i).getId());
            message.load();
            System.out.println(message.getSender());
            System.out.println("邮件主题 -->" +items.get(i).getSubject());
            System.out.println("接收方："+message.getReceivedBy());
            System.out.println("抄送方："+message.getCcRecipients());
            System.out.println("发送："+message.getSender());
            System.out.println("发送人："+message.getFrom());
            System.out.println("接收时间："+items.get(i).getDateTimeReceived());
            System.out.println("是否已读："+message.getIsRead());
            System.out.println("邮件内容："+getContentFromHtml(message.getBody().toString()));
            System.out.println("邮件ID："+items.get(i).getId());
        }
//        // 查询会议信息
//        getAppoinement(service,"adc@456.com");
//        // 新建一条会议信息
//        saveAppoinement(service);
    }

    // 获取会议信息
//    public static void getAppoinement(ExchangeService service,String emailAddress) throws Exception{
//        Date start = new Date();
//        Date end = new Date(start.getTime() + 1000*3600*24);
//
//        CalendarView cView = new CalendarView(start, end);
//        //指定要查看的邮箱
//        FolderId folderId = new FolderId(WellKnownFolderName.Calendar, new Mailbox(emailAddress));
//        FindItemsResults<Appointment> findResults = null;
//        try {
//            findResults = service.findAppointments(folderId, cView);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ArrayList<Appointment> appointmentItems = findResults==null?null:findResults.getItems();
//
//        for(Appointment ap:appointmentItems){
//            ap.load();
//            String subject = ap.getSubject();
//            //如邮箱主题以“已取消”开头，说明该会议已经被取消
//            if(subject.startsWith("已取消")){
//                continue;
//            }
//            //得到HTML格式的内容，通过工具类提取body标签的内容
//            String html_body = ap.getBody().toString();
//            String body = getContentFromHtml(html_body);
//            System.out.println(body);
//            //会议的开始和结束时间
//            Date start1 = ap.getStart();
//            Date end1 = ap.getEnd();
//
//            //会议使用的资源
//            List<Attendee> resources = ap.getResources().getItems();
//
//            //参加会议的员工
//            List<Attendee> RequiredAttendees = ap.getRequiredAttendees().getItems();
//            List<Attendee> OptionalAttendees = ap.getOptionalAttendees().getItems();
//        }
//    }

    // 向 Exchange 发送一条会议邮件
//    public static void saveAppoinement(ExchangeService service) throws Exception{
//        Appointment appointment = null;
//        try {
//            appointment = new Appointment(service);
//            appointment.setSubject("会议主题");
//            appointment.setBody(MessageBody.getMessageBodyFromText("会议消息体"));
//
//            appointment.setStart(new Date());
//            appointment.setEnd(new Date());
//
//            appointment.setLocation("会议位置");
//            appointment.getResources().add("会议资源账号，如：meetingroom@company.com");
//            // 必须参加的员工的账号
//            appointment.getRequiredAttendees().add("abc@456.com");
//            // 可选参加的员工的账号
//            appointment.getOptionalAttendees().add("abc@456.com");
//
//            appointment.save();
//            appointment.update(ConflictResolutionMode.AutoResolve);
//            System.out.println("会议创建成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // 从HTML提取body信息
    public static String getContentFromHtml(String content){
        content = content.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        content = content.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        content = content.replaceAll("&nbsp;", "");
        content = content.replaceAll("\n", "");
        return content;
    }

}