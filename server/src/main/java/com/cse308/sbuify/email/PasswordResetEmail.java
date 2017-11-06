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
	}
}
