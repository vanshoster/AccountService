package account.security;

import account.model.Event;
import account.model.User;
import account.repository.EventRepository;
import account.repository.UserRepository;
import account.service.LoginAttemptService;
import account.enums.EventName;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public AuthenticationFailureListener(
            LoginAttemptService loginAttemptService,
            UserService userService,
            UserRepository userRepository,
            EventRepository eventRepository) {
        this.loginAttemptService = loginAttemptService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String pathInfo = attributes.getRequest().getRequestURI();
        String userEmail = ((String) event.getAuthentication().getPrincipal()).toLowerCase();
        Event securityEvent = new Event(EventName.LOGIN_FAILED.toString(), userEmail, pathInfo, pathInfo);
        User adminUser = userRepository.findUserById(1L);
        eventRepository.save(securityEvent);
        if (adminUser == null || !userEmail.equals(adminUser.getEmail())) {
            System.out.println("Admin not null and user not admin");
            User user = userService.findUserByEmail(userEmail);
            loginAttemptService.loginFailed(userEmail);
        }
    }
}
