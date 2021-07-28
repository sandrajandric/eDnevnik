package com.iktakademija.ednevnik.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.iktakademija.ednevnik.services.dto.EmailObject;

public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendSimpleMessage(EmailObject emailObject) {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(emailObject.getTo());
		simpleMailMessage.setSubject(emailObject.getSubject());
		simpleMailMessage.setText(emailObject.getText());
		mailSender.send(simpleMailMessage);
	}

}
