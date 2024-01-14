package account.repository;

import account.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    Boolean existsUserByEmailIgnoreCase(String email);

    long count();

    List<User> findAllByOrderByIdAsc();

    long deleteUserByEmailContainsIgnoreCase(String email);

    User getUsersByEmailIgnoreCase(String email);

    User findUserById(Long Id);
}
