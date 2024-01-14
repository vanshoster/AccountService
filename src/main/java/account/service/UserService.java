package account.service;

import account.exception.*;
import account.model.Group;
import account.model.User;
import account.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GroupService groupService;


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void createNewUser(User user) throws UsernameNotFoundException {
        if (userRepository.getUsersByEmailIgnoreCase(user.getEmail()) != null) {
            throw new UserAlreadyExistException();
        } else {
            user.setEmail(user.getEmail().toLowerCase());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            long usersCount = userRepository.count();
            String groupName = usersCount == 0 ? "ADMINISTRATOR" : "USER";
            String groupName2 = usersCount == 0 ? "SA" : "USER";
            Group group = groupService.findGroupByName(groupName);
            Group group2 = groupService.findGroupByName(groupName2);
            user.getUserGroups().add(group);
            user.getUserGroups().add(group2);
            userRepository.save(user);
        }
    }

    public User findUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.getUsersByEmailIgnoreCase(email);
        if (user == null) {
            throw new UserNotFoundException("User not found!");
        }
        return user;
    }

    public List<User> findAllUsersById() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public Boolean exsistsUserByEmail(String email) {
        return userRepository.existsUserByEmailIgnoreCase(email);
    }

    public ResponseEntity<User> getUserInfo(User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public void changePassword(String newPassword, UserDetails userDetails) {
        if (!passwordEncoder.matches(newPassword, userDetails.getPassword())) {
            User user = findUserByEmail(userDetails.getUsername());
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user); //обновление данных пользователя
        } else {
            throw new PasswordChangeException();
        }
    }

    public ResponseEntity<Map<String,String>> deleteUserByEmail(String email) {
        long isDeleted = userRepository.deleteUserByEmailContainsIgnoreCase(email);
        if (isDeleted > 0) {
            Map<String, String> info = new HashMap<>();
            info.put("user", email.toLowerCase());
            info.put("status", "Deleted successfully!");
            return new ResponseEntity<>(info, HttpStatus.OK);
        } else {
            throw new UserNotFoundException("User not found!");
        }
    }


    //Grant role to user
    public void grantRoleToUser(User user, Group group) {
        if (group.getName().equals("ADMINISTRATOR") && !user.getAuthorities().contains("ROLE_ADMINISTRATOR") ||
                !group.getName().equals("ADMINISTRATOR") && user.getAuthorities().contains("ROLE_ADMINISTRATOR")) {
            throw new CombineRolesException();
        }
        user.getUserGroups().add(group);
        userRepository.save(user);
    }

    //Remove role from user
    public void removeRoleFromUser(User user, Group group) {
        ArrayList authorities = user.getAuthorities();
        if (group.getName().equals("ADMINISTRATOR") && authorities.contains("ROLE_ADMINISTRATOR")) {
            throw new AdministratorCannotBeRemovedException();
        }
        if (!authorities.contains(group.getCode())) {
            throw new RoleExceptionsWithBadRequest("The user does not have a role!");
        }
        if (authorities.size()==1) {
            throw new RoleExceptionsWithBadRequest("The user must have at least one role!");
        }
        user.getUserGroups().remove(group);
        userRepository.save(user);
    }
}

