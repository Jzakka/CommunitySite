package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public Users login(String loginId, String password) {
        return userRepository.findByLoginId(loginId)
                .filter(users -> users.getPassword().equals(password)).orElse(null);
    }
}
