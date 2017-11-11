package com.cse308.sbuify.email;

import com.cse308.sbuify.user.User;

public class PasswordResetEmail extends Email {
	private User user;

	public PasswordResetEmail(User user) {
		this.user = user;
	}

	@Override
	protected void build() {
		// todo: set toEmail, subject body
		this.toEmail = user.getEmail();
		this.subject = "Reset Password Request";
		this.body = "<h1>Hey, " + user.getName() + "</h1>\n" +
				"<p>" + "If you did not send this request please ignore this email" + "</p>\n" +
				"<p>" + "<a href= \"/api/customer/"+ user.getToken() + "\">Reset Password</a>" + "</p>\n" +
				"<p>Your Truly, 49er's</p>";
	}
}
