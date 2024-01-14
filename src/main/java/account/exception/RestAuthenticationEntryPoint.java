package account.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        if (authException instanceof UsernameNotFoundException) {
//            // Обработка сценария с несуществующим пользователем
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found!");
//        } else
        if (authException instanceof LockedException) {
            // Обработка сценария с заблокированным пользователем
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User account is locked");
        } else if (authException instanceof BadCredentialsException)  {
            // Обработка других сценариев аутентификации
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong password!");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied!");
        }
    }
}
