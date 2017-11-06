package com.cse308.sbuify.email;

import com.cse308.sbuify.user.User;

public class NewAccountEmail extends Email {
	private User user;

    public NewAccountEmail(User user) {
        this.user = user;
    }

    @Override
    protected void build() {
        // todo: set toEmail, subject, and body
    }
}
