package com.cse308.sbuify.label;

import com.cse308.sbuify.user.User;
import org.hibernate.search.annotations.ContainedIn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a user who owns/manages a Label.
 */
@Entity
public class LabelOwner extends User {

    private final static Collection<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_LABEL"));
    }

    @ContainedIn
    @OneToOne(mappedBy = "owner")
    @NotNull
    private Label label;

    public LabelOwner() {
    }

    public LabelOwner(@NotNull String email, @NotNull String password, @NotNull Label label) {
        super(email, password);
        this.label = label;
    }

    /**
     * The name of the owned Label.
     */
    public String getName() {
        return label.getName();
    }

    /**
     * {@link #getName()}
     */
    public void setName(String name) {
        label.setName(name);
    }

    /**
     * The Label owned by the label owner.
     */
    public Label getLabel() {
        return label;
    }

    /**
     * {@link #getLabel()}
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * The authorities granted to label owners.
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }
}
