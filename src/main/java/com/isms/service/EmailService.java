package com.isms.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	void sendOtpMessage(String to, String subject, String message) throws MessagingException;

	void sendAssignmentNotificationMessage(String to, String subject, String message) throws MessagingException;

}
