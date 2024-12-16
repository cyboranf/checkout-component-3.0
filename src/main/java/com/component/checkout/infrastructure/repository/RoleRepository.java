package com.component.checkout.infrastructure.repository;

import com.component.checkout.model.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(String name);
}
