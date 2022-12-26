package com.gradebook.data.service;

import com.gradebook.data.repository.UserCredentialRepository;
import com.gradebook.data.entity.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;

@Service
public class UserCredentialService implements CrudListener<UserCredential> {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserCredentialRepository userCredentialRepository;

    @Autowired
    public UserCredentialService(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public List<UserCredential> findAll() {
        return this.userCredentialRepository.findAll();
    }

    @Override
    public UserCredential add(UserCredential userCredential) {
        UserCredential newUserCredential = new UserCredential();
        newUserCredential.setUsername(userCredential.getUsername());
        newUserCredential.setRoles(userCredential.getRoles());
        newUserCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        return this.userCredentialRepository.save(newUserCredential);
    }

    @Override
    public UserCredential update(UserCredential userCredential) {
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        return userCredentialRepository.save(userCredential);
    }

    @Override
    public void delete(UserCredential userCredential){
        this.userCredentialRepository.delete(userCredential);
    }

    public boolean existsUserName(String username){
        try{
            return userCredentialRepository.getByUsername(username) != null;
        } catch (Exception e){
            return false;
        }
    }
}
