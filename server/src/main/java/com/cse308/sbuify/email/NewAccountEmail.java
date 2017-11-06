package com.cse308.sbuify.email;

import com.cse308.sbuify.user.AppUser;

public class NewAccountEmail extends Email {
	private AppUser user;

    public NewAccountEmail(AppUser user) {
        this.user = user;
    }

    @Override
    protected void build() {
        // todo: set toEmail, subject, and body
    }
}
