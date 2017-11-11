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
        this.toEmail = user.getEmail();

        this.subject = "Thank you for registering with SBUify";

        this.body = "<h1>"  + user.getName() +"Welcome to the SBUify family</h1>\n" +
                    "<p>Your username is: " + user.getEmail()  + "</p>\n" +
                    "<p>Your Truly, 49er's</p>";

    }
}
