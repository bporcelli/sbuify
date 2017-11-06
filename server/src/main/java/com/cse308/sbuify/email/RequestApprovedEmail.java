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
	}
}
