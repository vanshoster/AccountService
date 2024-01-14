package account.exception;

import account.repository.EventRepository;
import account.enums.EventName;
import account.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final EventRepository eventRepository;

    @Autowired
    public CustomAccessDeniedHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{ \"timestamp\": \"" + java.time.LocalDateTime.now() + "\", " +
                "\"status\": 403, " +
                "\"error\": \"Forbidden\", " +
                "\"message\": \"Access Denied!\", " +
                "\"path\": \"" + request.getRequestURI() + "\" }");
        Event event = new Event(EventName.ACCESS_DENIED.toString(), userEmail, request.getRequestURI(), request.getRequestURI());
        eventRepository.save(event);
    }
}



