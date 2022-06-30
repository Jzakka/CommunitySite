package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        Users user = new Users();
        user.setUserName("userA");
        Long savedId = userRepository.save(user);

        Assertions.assertThat(savedId).isEqualTo(user.getId());
    }

    @Test
    void findOne() {
    }

    @Test
    void findAll() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}