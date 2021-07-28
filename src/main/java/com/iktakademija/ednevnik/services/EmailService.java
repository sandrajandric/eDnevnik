package com.iktakademija.ednevnik.services;

import com.iktakademija.ednevnik.services.dto.EmailObject;

public interface EmailService {

	public void sendSimpleMessage(EmailObject emailObject);
}
