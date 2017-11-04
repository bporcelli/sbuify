package com.cse308.sbuify.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Preferences {
	@Id
	private Integer id;

	private User user;
	private String prefKey;
	private String prefValue;

	// Must include no-arg constructor to satisfy Jackson
	public Preferences() {}

	public Preferences(@NotNull String prefKey, @NotNull String prefValue) {
        this.prefKey = prefKey;
        this.prefValue = prefValue;
    }
}
