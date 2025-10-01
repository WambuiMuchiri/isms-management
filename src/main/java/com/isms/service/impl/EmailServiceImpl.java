package com.isms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.isms.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void sendOtpMessage(String to, String subject, String message) throws MessagingException {
		MimeMessage msg = javaMailSender.createMimeMessage();		
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		helper.setFrom("no-reply@isms.org");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(message, true);
		javaMailSender.send(msg);
	}

	@Override
	public void sendAssignmentNotificationMessage(String to, String subject, String message) throws MessagingException {
		MimeMessage msg = javaMailSender.createMimeMessage();		
		MimeMessageHelper helper = new MimeMessageHelper(msg);
		helper.setFrom("no-reply@isms.org");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(message, true);
		javaMailSender.send(msg);		
	}

}
