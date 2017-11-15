package com.cse308.sbuify.email;

import com.cse308.sbuify.label.ArtistRequest;

public class RequestDeniedEmail extends Email {
	private ArtistRequest request;

	public RequestDeniedEmail(ArtistRequest req) {
		this.request = req;
	}

	@Override
	protected void build() {
		// todo: set toEmail, subject, body
		this.toEmail = request.getLabel().getEmail();
		this.subject = "Artist Request Denied";
		this.body = "<h1>Hey, " + request.getLabel().getName() + "</h1>" +
				"<p>" + request.getArtist().getName()+ " has denied your request" + "</p>" +
				"<p>Your Truly, 49er's</p>";;

	}
}
