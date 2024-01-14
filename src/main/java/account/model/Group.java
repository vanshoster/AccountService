package account.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "principle_groups")
public class Group{

    //removed getter and setter to save space
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;
    private String name;

    @ManyToMany(mappedBy = "userGroups")
    private Set<User> users;

    public Group(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Group() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
