package account.service;

import account.exception.RoleNotFoundException;
import account.model.Group;
import account.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    public Group findGroupByName(String name) {
        Group group = groupRepository.getGroupByNameIgnoreCase(name);
        if (group == null) {
            throw new RoleNotFoundException();
        }
        return group;
    }

}
