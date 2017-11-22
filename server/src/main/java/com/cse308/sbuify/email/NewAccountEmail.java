package com.cse308.sbuify.email;

import com.cse308.sbuify.user.User;

public class NewAccountEmail extends Email {
	private User user;

    public NewAccountEmail(User user) {
        this.user = user;
    }

    @Override
    protected void build() {
        // todo: put HTML in a separate template file
        this.toEmail = user.getEmail();

        this.subject = "Thank you for registering with SBUify";

        this.body = "<h1>Welcome to SBUify, "+ user.getName() +"</h1>\n" +
                    "<p>Your username is: " + user.getEmail()  + "</p>\n" +
                    "<p>- The 49er's</p>";

    }
}
