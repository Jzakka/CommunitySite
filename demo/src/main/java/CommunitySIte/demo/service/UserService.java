package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long join(Users user) {
        userRedundancyCheck(user);
        Long saveId = userRepository.save(user);
        return saveId;
    }

    @Transactional(readOnly = true)
    public Users showUser(Long id) {
        Users findUser = userRepository.findOne(id);
        return findUser;
    }

    @Transactional(readOnly = true)
    public List<Users> showUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean login(String name, String password) {
        Users user = userExistenceCheck(name);
        return user.getPassword().equals(password);
    }

    @Transactional(readOnly = true)
    public List<Post> showPosts(Long id) {
        Users user = userRepository.findOne(id);
        return user.getPosts();
    }

    private Users userExistenceCheck(String name) {

        List<Users> users = userRepository.findByName(name);
        if (users.isEmpty()) {
            throw new IllegalStateException("Invalid username");
        }
        return users.get(0);
    }

    public void update(Long userId, String name, String password) {
        userRepository.updateUser(userId, name, password);
    }

    public void withDraw(Long userId) {
        userRepository.deleteUser(userId);
    }

    private void userRedundancyCheck(Users user) {
        List<Users> users= userRepository.findByName(user.getUserName());
        if(!users.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

}
