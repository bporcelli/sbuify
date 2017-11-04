package com.cse308.sbuify.email;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// TODO
public class Email {
	private static InternetAddress FROM_EMAIL;
	private static Session session;
	private static boolean html;
	private static String toEmail, subject, body;

	public Email() {

	}

	public void setHeaders(MimeMessage msg) {

	}

	public void build() {

	}

	public boolean dispatch() {
		return false;
	}
}
