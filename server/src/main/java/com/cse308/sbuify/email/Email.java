package com.cse308.sbuify.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public abstract class Email {

	/*
	 * Angular 2 port
	 */
	protected static String websitePort = "4200";

	/*
	 * Gmail SMTP port
	 */

	private static final String SMTP = "587";

	/**
	 *
	 * Gmail SMTP host
	 */
	private static final String HOST = "smtp.gmail.com";

	/**
	 *
	 *
	 */

    /*
     * Session (used when constructing MimeMessage).
     */
	private static Session session;

    /**
     * Is this email an HTML email? Set in the build method.
     */
	protected boolean html = true;

    /**
     * These parameters are set in the build() method, which is implemented by all subclasses.
     */
	protected String toEmail, subject, body;

    /**
     * Set the headers for the given message based on the values of toEmail, subject, and body.
     * @param msg
     */
	private void setHeaders(MimeMessage msg) throws MessagingException {

		msg.setHeader("Content-Type", "text/html; charset=UTF-8");
    }

    /**
     * Set the toEmail, subject, and body for the email.
     */
	protected abstract void build();

    /**
     * Dispatch the email.
     * @return true if email was sent successfully, else false.
     */
	public boolean dispatch() {
	    this.build();
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.host", HOST);
		mailServerProperties.put("mail.smtp.port", SMTP);
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		session = Session.getDefaultInstance(mailServerProperties, null);

		try {
			Message generateMailMessage = new MimeMessage(session);
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.toEmail));
			generateMailMessage.setSubject(this.subject);
			generateMailMessage.setSentDate(new Date());
			generateMailMessage.setContent(this.body,"text/html");
			Transport transport = session.getTransport("smtp");
			transport.connect(HOST, "SBUify", "cse308project");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();

			return true;

		} catch (MessagingException e){
			return false;
		}
	}
}
