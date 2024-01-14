package account.service;

import account.model.Event;
import account.repository.EventRepository;
import account.enums.EventName;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 5;
    private LoadingCache<String, Integer> attemptsCache;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    EventRepository eventRepository;


    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String pathInfo = attributes.getRequest().getRequestURI();
        if (attempts >= MAX_ATTEMPT) {
            Event event = new Event(EventName.BRUTE_FORCE.toString(), key, pathInfo, pathInfo);
            eventRepository.save(event);
            Event event2 = new Event(EventName.LOCK_USER.toString(), key, "Lock user "+key, pathInfo);
            eventRepository.save(event2);
        }
    }

    public boolean isBlocked(String userEmail) {
        try {
            return attemptsCache.get(userEmail) >= MAX_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    public void resetAttempts(String userEmail) {
        int attempts;
        try {
            attempts = attemptsCache.get(userEmail);
        } catch (final ExecutionException ignored) {
        }
        attempts = 0;
        attemptsCache.put(userEmail, attempts);
    }

    public void blockUser(String userEmail) {
        int attempts;
        try {
            attempts = attemptsCache.get(userEmail);
        } catch (final ExecutionException ignored) {
        }
        attempts = MAX_ATTEMPT;
        attemptsCache.put(userEmail, attempts);
    }


    public void successfullAuth(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts = 0;
        attemptsCache.put(key, attempts);
    }
}