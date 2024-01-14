package account.repository;

import account.model.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAllByOrderByIdAsc();
}