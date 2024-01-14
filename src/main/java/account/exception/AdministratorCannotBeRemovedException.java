package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Can't remove ADMINISTRATOR role!")
public class AdministratorCannotBeRemovedException extends RuntimeException {
    public AdministratorCannotBeRemovedException() {
        super();
    }
}
