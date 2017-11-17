package com.cse308.sbuify.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public abstract class Email {
	
    /**
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

		mailServerProperties.put("mail.smtps.port", "465");
		mailServerProperties.put("mail.smtps.auth", "true");
		mailServerProperties.put("mail.smtps.starttls.enable", "true");

		session = Session.getDefaultInstance(mailServerProperties, null);

		try {
			MimeMessage generateMailMessage = new MimeMessage(session);
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.toEmail));
			generateMailMessage.setSubject(this.subject);

			String emailBody = this.body;

			generateMailMessage.setContent(emailBody, "text/html");

			Transport transport = session.getTransport("smtps");
			transport.connect("smtp.gmail.com", "SBUify", "cse308project");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();

			return true;

		} catch (MessagingException e){
			return false;
		}
	}
}
