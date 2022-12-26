package com.gradebook.data.generator;

import com.gradebook.data.repository.UserCredentialRepository;
import com.gradebook.data.entity.Role;
import com.gradebook.data.entity.UserCredential;
import com.gradebook.data.service.UserCredentialService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Set;

@SpringComponent
public class DataUserGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder,
                                      UserCredentialService userCredentialService,
                                      UserCredentialRepository userCredentialRepository){
        return args -> {
            if (userCredentialRepository.count() != 0L) {
                return;
            }
            userCredentialService.add(makeUser("admin","admin", Collections.singleton(Role.ADMIN)));
            userCredentialService.add(makeUser("teacher","teacher", Collections.singleton(Role.TEACHER)));
        };
    }

    private UserCredential makeUser(String name, String password, Set<Role> roles) {
        UserCredential userCredential = new UserCredential();
        userCredential.setUsername(name);
        userCredential.setPassword(password);
        userCredential.setRoles(roles);
        return userCredential;
    }

}
