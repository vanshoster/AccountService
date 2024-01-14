package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Argument cannot be null")
public class NullArgumentException extends RuntimeException {
    public NullArgumentException() {
        super();
    }
}