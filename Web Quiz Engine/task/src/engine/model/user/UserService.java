package engine.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public boolean existsByEmail(String username) {
        return userRepository.existsByEmail(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
