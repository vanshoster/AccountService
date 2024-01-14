package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Payment not found!")
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super();
    }
}