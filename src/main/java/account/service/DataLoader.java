package account.service;

import account.model.Group;
import account.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        if (groupRepository.count() == 0) {
            createRoles();
        }
    }

    private void createRoles() {
        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR", "ADMINISTRATOR"));
            groupRepository.save(new Group("ROLE_USER", "USER"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT", "ACCOUNTANT"));
            groupRepository.save(new Group("ROLE_AUDITOR", "AUDITOR"));
            groupRepository.save(new Group("ROLE_SA", "SA"));
        } catch (Exception e) {

        }
    }
}