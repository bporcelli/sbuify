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
	}
}
