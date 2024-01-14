package account.controller;

import account.repository.EventRepository;
import account.model.Event;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class AuditorController {

    private final UserService userService;
    private final EventRepository eventRepository;

    @Autowired
    public AuditorController(UserService userService, EventRepository eventRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/security/events/")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> listOfEvents = eventRepository.findAllByOrderByIdAsc();
        return new ResponseEntity<>(listOfEvents, HttpStatus.OK);
    }
}
