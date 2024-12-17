package com.component.checkout.infrastructure.repository;

import com.component.checkout.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindUserByLogin_success() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password");

        userRepository.save(user);

        User foundUser = userRepository.findUserByLogin("testuser");

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getLogin()).isEqualTo("testuser");
    }
}