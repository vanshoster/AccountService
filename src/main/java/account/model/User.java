package account.model;

import account.validation.ValidPassword;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name="USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "User name cannot be blank")
    private String name;
    @NotBlank(message = "User lastname cannot be blank")
    private String lastname;
    @NotBlank(message = "User email cannot be blank")
    @Email(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,6}$", message = "Email is not valid")
    @Column(unique = true)
    private String email;


    @NotBlank(message = "User password cannot be blank")
    @ValidPassword
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_groups",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    @JsonIgnore
    private Set<Group> userGroups= new HashSet<>();

    public Set<Group> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<Group> userGroups) {
        this.userGroups = userGroups;
    }

    public User() {
    }

    public User(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email.toLowerCase();
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @JsonProperty(value = "roles")
    public ArrayList<String> getAuthorities() {
        ArrayList<String> userAuthorities = new ArrayList<>();
        userGroups.forEach(group -> userAuthorities.add(group.getCode()));
        Collections.sort(userAuthorities);
        return userAuthorities;
    }
}
