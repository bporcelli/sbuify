package com.cse308.sbuify.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public abstract class Email {

    /**
     * From email (constant).
     */
	private static final InternetAddress FROM_EMAIL = new InternetAddress();

	static {
	    FROM_EMAIL.setAddress("no-reply@sbuify.com");
	}

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
        // todo: construct mimemessage, call setheaders, send
//
//		String host = "relay.jangosmtp.net";
//		final String username = "SBUify";//change accordingly
//		final String password = "cse308project";//change accordingly
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", host);
//		props.put("mail.smtp.port", "25");
//
//		// Get the Session object.
//		Session session = Session.getInstance(props,
//				new javax.mail.Authenticator() {
//					protected PasswordAuthentication getPasswordAuthentication() {
//						return new PasswordAuthentication(username, password);
//					}
//				});
//
//		try {
//			// Create a default MimeMessage object.
//			Message message = new MimeMessage(session);
//
//			// Set From: header field of the header.
//			message.setFrom(Email.FROM_EMAIL);
//
//			// Set To: header field of the header.
//			message.setRecipients(Message.RecipientType.TO,
//					InternetAddress.parse(this.toEmail));
//
//			// Set Subject: header field
//			message.setSubject(this.subject);
//
//			// Send the actual HTML message, as big as you like
//			message.setContent(
//						this.body,
//					"text/html");
//
//			// Send message
//			Transport.send(message);
//			return true;
//
//		} catch (MessagingException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
		try {
			Properties mailServerProperties = System.getProperties();

			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");

			session = Session.getDefaultInstance(mailServerProperties, null);

			MimeMessage generateMailMessage = new MimeMessage(session);

			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.toEmail));
			generateMailMessage.setSubject(this.subject);

			String emailBody = this.body;

			generateMailMessage.setContent(emailBody, "text/html");

			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", "SBUify", "cse308project");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();

			return true;

		} catch (MessagingException e){
			return false;
		}
	}
}
