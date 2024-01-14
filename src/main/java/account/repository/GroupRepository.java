package account.repository;

import account.model.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {
    Group getGroupByNameIgnoreCase(String name);
    long count();
}
