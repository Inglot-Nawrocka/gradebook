package com.gradebook.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;


import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserCredential {

    @Column(unique = true)
    private String username;

    private String passwordSalt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public UserCredential(String username, String password, Set<Role> roles) {
        this.username = username;

        this.roles = roles;

        this.passwordSalt = RandomStringUtils.random(32);

        this.password = DigestUtils.sha1Hex(password + this.passwordSalt);
    }
}
