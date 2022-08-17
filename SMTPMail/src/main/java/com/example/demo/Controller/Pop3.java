package com.example.demo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;

import com.sun.mail.pop3.POP3SSLStore;

public class Pop3 {

	final String popHost;   // POP3主机
	final String username;  // 邮箱地址
	final String password;  // 邮箱授权码
	final boolean debug;

	public Pop3(String popHost, String username, String password) {
		this.popHost = popHost;
		this.username = username;
		this.password = password;
		this.debug = true;
	}

	public static void main(String[] args) throws Exception {
		Pop3 pop = new Pop3("pop.qq.com", "3213730851@qq.com", "fogsipfmqugjdecb");
		Folder folder = null;
		Store store = null;
		try {
			store = pop.createSSLStore();
			folder = store.getFolder("INBOX");  // 指定INBOX收件箱
			folder.open(Folder.READ_WRITE);
			System.out.println("Total messages: " + folder.getMessageCount());
			System.out.println("New messages: " + folder.getNewMessageCount());
			System.out.println("Unread messages: " + folder.getUnreadMessageCount());
			System.out.println("Deleted messages: " + folder.getDeletedMessageCount());
			Message[] messages = folder.getMessages();   // 获取所有邮件

			// 打印所有邮件
//			for (Message message : messages) {
//				printMessage((MimeMessage) message);
//			}

			// 打印num封邮件
			int num = 5;
			for (int i=0; i<num; i++) {
				Message message = messages[i];
				printMessage((MimeMessage) message);
			}

		} finally {
			if (folder != null) {
				try {
					folder.close(true);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			if (store != null) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 获取邮箱的存储（Store）
	public Store createSSLStore() throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "pop3");   // 协议名称
		props.setProperty("mail.pop3.port", "995"); // 主机端口号
		props.setProperty("mail.pop3.host", this.popHost);// POP3主机名

		// 启动SSL:
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "995");

		// 连接Store:
		URLName url = new URLName("pop3", this.popHost, 995, "", this.username, this.password);
		Session session = Session.getInstance(props, null);
		session.setDebug(this.debug); // 显示调试信息
		Store store = new POP3SSLStore(session, url);
		store.connect();
		return store;
	}

	// 打印一封邮件信息
	static void printMessage(MimeMessage msg) throws IOException, MessagingException {
		System.out.println("--------------------");
		System.out.println("Subject: " + MimeUtility.decodeText(msg.getSubject()));   // 主题
		System.out.println("From: " + getFrom(msg));								  // 发件人
		System.out.println("To: " + getTo(msg));								      // 收件人
		System.out.println("Sent: " + msg.getSentDate().toString());     		      // 发送时间
		System.out.println("Seen: " + msg.getFlags().contains(Flags.Flag.SEEN));	  // 是否已读
		System.out.println("Priority: " + getPriority(msg));    					  // Priority
		System.out.println("Size: " + msg.getSize() / 1024 + "kb");					  // 邮件大小
		System.out.println("Body: " + getBody(msg));								  // 邮件正文
		System.out.println("--------------------");
	}

	// 获取邮件发件人
	static String getFrom(MimeMessage msg) throws IOException, MessagingException {
		Address[] froms = msg.getFrom();
		return addressToString(froms[0]);
	}

	// 获取所有收件人
	static String getTo(MimeMessage msg) throws MessagingException, IOException {
		Address[] tos = msg.getRecipients(RecipientType.TO);
		List<String> list = new ArrayList<>();
		for (Address to : tos) {
			list.add(addressToString(to));
		}
		return String.join(", ", list);
	}


	static String addressToString(Address addr) throws IOException {
		InternetAddress address = (InternetAddress) addr;
		String personal = address.getPersonal();
		return personal == null ? address.getAddress()
				: (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");
	}

	// 获取邮件Priority
	static String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "Normal";
		String[] headers = msg.getHeader("X-Priority");
		if (headers != null) {
			String header = headers[0];
			if ("1".equals(header) || "high".equalsIgnoreCase(header)) {
				priority = "High";
			} else if ("5".equals(header) || "low".equalsIgnoreCase(header)) {
				priority = "Low";
			}
		}
		return priority;
	}

	// 获取邮件正文内容
	static String getBody(Part part) throws MessagingException, IOException {
		if (part.isMimeType("text/*")) {
			return part.getContent().toString();
		}
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				String body = getBody(bodyPart);
				if (!body.isEmpty()) {
					return body;
				}
			}
		}
		return "";
	}
}
