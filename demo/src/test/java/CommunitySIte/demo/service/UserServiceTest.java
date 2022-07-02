package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @Test
    void join() {
        Users user = new Users();
        user.setUserName("test");

        Long joinId = userService.join(user);

        Users findUser = userService.showUser(joinId);
        assertThat(findUser.getUserName()).isEqualTo(user.getUserName());
    }

    @Test
    void showUsers() {
        Users user = new Users();
        user.setUserName("test");
        Users user2 = new Users();
        user2.setUserName("test2");
        userService.join(user);
        userService.join(user2);

        List<Users> users = userService.showUsers();

        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    void update() {
        Users user = new Users();
        user.setUserName("test");
        user.setPassword("1234567");
        Long id = userService.join(user);

        userService.update(id, "changedName" ,"1111");

        Users findUser = userService.showUser(id);
        assertThat(findUser.getPassword()).isEqualTo("1111");
    }

    @Test
    void login() {
        Users user = new Users();
        user.setUserName("test");
        user.setPassword("1234567");
        userService.join(user);

        boolean result = userService.login("test", "1234567");

        assertThat(result).isEqualTo(true);
    }

    @Test
    void 중복회원검사() throws Exception{
        Users user = new Users();
        user.setUserName("test");
        user.setPassword("1234567");
        userService.join(user);

        Users user2 = new Users();
        user2.setUserName("test");
        user2.setPassword("1234567");

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> userService.join(user2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }
}