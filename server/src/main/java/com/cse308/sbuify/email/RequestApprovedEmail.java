package com.cse308.sbuify.email;

import com.cse308.sbuify.label.ArtistRequest;

public class RequestApprovedEmail extends Email {
	private ArtistRequest request;

	public RequestApprovedEmail(ArtistRequest req) {
		this.request = req;
	}

	@Override
	protected void build() {
		// todo: set toEmail, subject, body
		this.toEmail = request.getLabel().getEmail();
		this.subject = "Artist Request Approved";
		this.body = "<h1>Hey, " + request.getLabel().getName() + "</h1>\n" +
				"<p>" + request.getArtist().getName() + " has accepted your request" + "</p>\n" +
				"<p>Your Truly, 49er's</p>";
	}
}
