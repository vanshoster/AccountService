package account.controller;

import account.repository.EventRepository;
import account.repository.UserRepository;
import account.dto.NewPasswordDTO;
import account.dto.NewPasswordResponseDTO;
import account.enums.EventName;
import account.model.Event;
import account.model.User;
import account.model.UserDetailsImpl;
import account.service.UserDetailsServiceImpl;
import account.service.UserService;
import account.validation.ValidPassword;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Validated
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final EventRepository eventRepository;

    @Autowired
    public AuthController(
            UserRepository userRepository,
            UserService userService,
            PasswordEncoder passwordEncoder,
            UserDetailsServiceImpl userDetailsService,
            EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<User> userSignUp(@Valid @RequestBody User user, HttpServletRequest request) {
        userService.createNewUser(user);
        Event event = new Event(EventName.CREATE_USER.toString(), "Anonymous", user.getEmail(), request.getRequestURI());
        eventRepository.save(event);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return "Access to '/test' granted";
    }

    @PostMapping("/login")
    public ResponseEntity userInfo(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest request) {
        User user = userRepository.getUsersByEmailIgnoreCase(authRequest.email);
        if (user == null) {
            return new ResponseEntity<>(authRequest.email + " not found!", HttpStatus.NOT_FOUND);
        }
        boolean passwordMatches = passwordEncoder.matches(authRequest.password, user.getPassword());
        if (passwordMatches) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong password!", HttpStatus.OK);
        }
//        Event event = new Event(EventName.CREATE_USER.toString(), "Anonymous", user.getEmail(), request.getRequestURI());
//        eventRepository.save(event);
//        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/auth/changepass")
    public ResponseEntity<NewPasswordResponseDTO> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody NewPasswordDTO newPassword, HttpServletRequest request) {
        userService.changePassword(newPassword.getNewPassword(), userDetails);
        Event event = new Event(EventName.CHANGE_PASSWORD.toString(), userDetails.getUsername(), userDetails.getUsername(), request.getRequestURI());
        eventRepository.save(event);
        return new ResponseEntity<>(new NewPasswordResponseDTO(userDetails.getUsername()), HttpStatus.OK);
    }

    public record AuthRequest(@NotBlank(message = "User email cannot be blank")
                              @Email(regexp = "^[\\w-\\.]+@acme\\.com$", message = "Email must be corporate and ends with @acme.com")
                              String email, @ValidPassword String password) { };
}

