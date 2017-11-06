package com.cse308.sbuify.email;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
	private void setHeaders(MimeMessage msg) {
	    // todo
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
	    return false;
	}
}
