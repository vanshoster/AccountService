package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User exist!")
public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException() {
        super();
    }
}
