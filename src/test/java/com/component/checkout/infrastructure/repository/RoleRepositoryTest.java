package com.component.checkout.infrastructure.repository;

import com.component.checkout.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindUserByLogin_success() {
        Role role = new Role();
        role.setName("ROLE_CLIENT");

        roleRepository.save(role);

        Role foundRole = roleRepository.findByName("ROLE_CLIENT").get();

        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getName()).isEqualTo("ROLE_CLIENT");
    }
}