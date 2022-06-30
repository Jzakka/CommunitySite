package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.UserRepository;
import CommunitySIte.demo.repository.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Users showUser(Long id) {
        Users findUser = userRepository.findOne(id);
        return findUser;
    }

    public List<Users> showUsers() {
        return userRepository.findAll();
    }

    public void update(Long userId, UserDto dto) {
        userRepository.updateUser(userId, dto);
    }

    public void withDraw(Long userId) {
        userRepository.deleteUser(userId);
    }

    private void userRedundancyCheck(Users user) {
        List<Users> users = new ArrayList<>();
        Users one = userRepository.findOne(user.getId());
        users.add(one);
        if(!users.isEmpty()){
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }
    }

}
