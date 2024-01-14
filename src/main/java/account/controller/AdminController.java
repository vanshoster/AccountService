package account.controller;

import account.enums.Operation;
import account.repository.EventRepository;
import account.dto.StatusDTO;
import account.enums.AccessOperation;
import account.enums.EventName;
import account.exception.AdministratorCannotBeLockedException;
import account.exception.AdministratorCannotBeRemovedException;
import account.model.Event;
import account.model.Group;
import account.model.User;
import account.model.UserDetailsImpl;
import account.service.GroupService;
import account.service.LoginAttemptService;
import account.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@Validated
public class AdminController {

    private final LoginAttemptService loginAttemptService;
    private final GroupService groupService;
    private final UserService userService;
    private final EventRepository eventRepository;

    @Autowired
    public AdminController(
            LoginAttemptService loginAttemptService,
            GroupService groupService,
            UserService userService,
            EventRepository eventRepository) {
        this.loginAttemptService = loginAttemptService;
        this.groupService = groupService;
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/admin/user/")
    public ResponseEntity<List<User>> getAllUsersInfoByIdAcs() {
        List<User> listOfUsersByIdAcs = userService.findAllUsersById();
        return new ResponseEntity<>(listOfUsersByIdAcs, HttpStatus.OK);
    }
    @GetMapping("/admin/user")
    public ResponseEntity<List<User>> getAllUsersInfo() {
        List<User> listOfUsersByIdAcs = userService.findAllUsersById();
        return new ResponseEntity<>(listOfUsersByIdAcs, HttpStatus.OK);
    }


    @DeleteMapping("/admin/user/{email}")
    public ResponseEntity<Map<String,String>> deleteUserByEmail(@PathVariable String email, HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userService.findUserByEmail(email).getAuthorities().contains("ROLE_ADMINISTRATOR")) {
            throw new AdministratorCannotBeRemovedException();
        }
        Event event = new Event(EventName.DELETE_USER.toString(), userDetails.getUsername(), email.toLowerCase(), request.getRequestURI().replace("/"+email.toLowerCase(),""));
        eventRepository.save(event);
        return userService.deleteUserByEmail(email);
    }

    @PutMapping("admin/user/role")
    public ResponseEntity<User> changeRoleOfUser(@RequestBody @Valid UserRoleOperation info, HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findUserByEmail(info.user);
        Group group = groupService.findGroupByName(info.role);
        if (info.operation == Operation.GRANT) {
            userService.grantRoleToUser(user, group);
            Event event = new Event(EventName.GRANT_ROLE.toString(), userDetails.getUsername(), "Grant role "+info.role+" to "+info.user.toLowerCase(), request.getRequestURI());
            eventRepository.save(event);
        } else if (info.operation == Operation.REMOVE) {
            userService.removeRoleFromUser(user, group);
            Event event = new Event(EventName.REMOVE_ROLE.toString(), userDetails.getUsername(), "Remove role "+info.role+" from "+info.user.toLowerCase(), request.getRequestURI());
            eventRepository.save(event);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("admin/user/access")
    public ResponseEntity<StatusDTO> lockUser(@RequestBody @Valid UserAccessOperation info, HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> body = new HashMap<>();
        String message = "";
        if (info.operation == AccessOperation.UNLOCK) {
            message = "User " + info.user.toLowerCase() + " unlocked!";
            loginAttemptService.resetAttempts(info.user.toLowerCase());
            Event event = new Event(EventName.UNLOCK_USER.toString(), userDetails.getUsername(), "Unlock user "+info.user.toLowerCase(), request.getRequestURI());
            eventRepository.save(event);
        } else if (info.operation == AccessOperation.LOCK) {
            if (userService.findUserByEmail(info.user).getAuthorities().contains("ROLE_ADMINISTRATOR")) {
                throw new AdministratorCannotBeLockedException();
            }
            message = "User " + info.user.toLowerCase() + " locked!";
            loginAttemptService.blockUser(info.user.toLowerCase());
            Event event = new Event(EventName.LOCK_USER.toString(), userDetails.getUsername(), "Lock user "+info.user.toLowerCase(), request.getRequestURI());
            eventRepository.save(event);
        }
        return new ResponseEntity<>(new StatusDTO(message), HttpStatus.OK);
    }



    record UserRoleOperation(@NotBlank(message = "User cannot be blank") String user,
                             String role,
                             Operation operation){};

    record UserAccessOperation(@NotBlank(message = "User cannot be blank") String user,
                               AccessOperation operation){};
}
