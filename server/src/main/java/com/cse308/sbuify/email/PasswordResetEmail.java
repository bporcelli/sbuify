package com.cse308.sbuify.email;

import com.cse308.sbuify.user.AppUser;

public class PasswordResetEmail extends Email {
	private AppUser user;

	public PasswordResetEmail(AppUser user) {
		this.user = user;
	}

	@Override
	protected void build() {
		// todo: set toEmail, subject body
	}
}
