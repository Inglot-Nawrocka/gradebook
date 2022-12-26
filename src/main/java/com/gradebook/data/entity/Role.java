package com.gradebook.data.entity;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN("admin"), TEACHER("teacher");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static List<Role> getRoles(){
        return Arrays.asList(Role.values());
    }
}