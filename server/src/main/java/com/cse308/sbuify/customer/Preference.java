package com.cse308.sbuify.customer;

import com.cse308.sbuify.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Preference implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
    @PrimaryKeyJoinColumn
	private User user;

	private String prefKey;

	private String prefValue;

	// Must include no-arg constructor to satisfy Jackson
	public Preference() {}

	public Preference(@NotNull String prefKey, @NotNull String prefValue) {
        this.prefKey = prefKey;
        this.prefValue = prefValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPrefKey() {
        return prefKey;
    }

    public void setPrefKey(String prefKey) {
        this.prefKey = prefKey;
    }

    public String getPrefValue() {
        return prefValue;
    }

    public void setPrefValue(String prefValue) {
        this.prefValue = prefValue;
    }
}
