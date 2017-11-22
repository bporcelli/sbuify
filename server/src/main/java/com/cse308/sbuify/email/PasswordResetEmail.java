package com.cse308.sbuify.email;

import com.cse308.sbuify.user.User;

public class PasswordResetEmail extends Email {

	private User user;

	public PasswordResetEmail(User user) {
		this.user = user;
	}

	@Override
	protected void build() {
		// todo: save website port and url in a separate config file; move HTML to template file
		this.toEmail = user.getEmail();
		this.subject = "Password Reset Request";
		this.body = "<h1>Hey " + user.getName() + ",</h1>\n" +
				"<p>Please use the link below to set a new password.</p>\n" +
				"<p><a href=\"" + getResetLink() + "\">Reset Password</a></p>\n" +
				"<p>If you did not send this request, please ignore this email.</p>" +
                "<p>- The 49er's</p>";
	}

	private String getResetLink() {
		return "http://localhost:" + Email.websitePort + "/reset-password?t=" + user.getToken();
	}
}
