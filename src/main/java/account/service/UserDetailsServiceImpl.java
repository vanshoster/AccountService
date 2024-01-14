package account.service;

import account.exception.UserNotFoundException;
import account.model.User;
import account.model.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final LoginAttemptService loginAttemptService;

    public UserDetailsServiceImpl(UserService userService, LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UserNotFoundException {
        User user = userService.findUserByEmail(email);

        return new UserDetailsImpl(user, loginAttemptService.isBlocked(email));
    }
}
