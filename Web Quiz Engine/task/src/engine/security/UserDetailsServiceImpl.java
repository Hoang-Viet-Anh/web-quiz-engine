package engine.security;

import engine.model.user.User;
import engine.model.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!userService.existsByEmail(email)) {
            throw new UsernameNotFoundException("User not found!");
        }
        User user = userService.findByEmail(email);
        System.out.println(user == null);

        return new UserDetailsImpl(user);
    }
}
