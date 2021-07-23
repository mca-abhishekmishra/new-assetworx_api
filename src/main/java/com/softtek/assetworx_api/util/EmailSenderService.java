package com.softtek.assetworx_api.util;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.softtek.assetworx_api.model.Mail;

@Service
public class EmailSenderService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Value("${spring.mail.mail_from}")
	private String mailFrom;

	public void sendEmail(Mail mail) {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			mail.getProps().put("subject", mail.getSubject());
			context.setVariables(mail.getProps());
			// context.setVariable("url", "http://localhost:9100"+mail.getUrl());
			context.setVariable("url", "https://10.2.2.200/"+mail.getUrl());
			context.setVariable("type", mail.getType());
			String htmlTemplate = templateEngine.process(mail.getTemplateName(), context);
			helper.setText(htmlTemplate, true);
			helper.setFrom(mailFrom);
			System.out.println("mail.getMailTo():"+mail.getMailTo());
			System.out.println("mail.getMailCc():"+mail.getMailCc());
			helper.setTo(parseEmailAddress(mail.getMailTo()));
			helper.setBcc("AssetWorx Notification - AssetWorx <ebc9011e.onesofttek.onmicrosoft.com@amer.teams.ms>");
			helper.setSubject("PROD - ASSETWORX: "+mail.getSubject());
			if(mail.getDocument()!=null) {
				String fileName = mail.getDocument().getFilePath() + File.separator + mail.getDocument().getFileName();
				helper.addAttachment(mail.getDocument().getOriginalFileName(), new File(fileName));
			}
			if(mail.getMailCc()!=null && !mail.getMailCc().isEmpty()) {
				helper.setCc(parseEmailAddress(mail.getMailCc()));
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		emailSender.send(message);
		System.out.println("Email sent successfully to "+ mail.getMailTo()+"...!!!");
	}
	
	private InternetAddress[] parseEmailAddress(String emails) {
		try {
			String addresslist = emails.trim().replaceAll(";", ",");
			return InternetAddress.parse(addresslist);
		} catch (AddressException e) {
			try {
				return InternetAddress.parse("ebc9011e.onesofttek.onmicrosoft.com@amer.teams.ms");
			} catch (AddressException e1) {
				return null;
			}
		}
	}
}
