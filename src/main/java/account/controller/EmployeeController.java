package account.controller;

import account.dto.PaymentResponse;
import account.model.Payment;
import account.model.User;
import account.model.UserDetailsImpl;
import account.service.PaymentService;
import account.service.UserService;
import account.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Validated
public class EmployeeController {

    private final PaymentService paymentService;
    private final UserService userService;

    @Autowired
    public EmployeeController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }


    @GetMapping("/empl/payment")
    public ResponseEntity getPayment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(required = false) @Valid Period period) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        if (period == null) {
            List<Payment> listOfPayments = paymentService.getPaymentsByEmployee(userDetails.getUsername());
            List<PaymentResponse> listOfPaymentsResponse = listOfPayments.stream()
                    .map(data -> new PaymentResponse(user, data)) // преобразование каждого элемента в PaymentResponse
                    .collect(Collectors.toList());
            return new ResponseEntity<>(listOfPaymentsResponse, HttpStatus.OK);
        } else {
            Payment payment = paymentService.getPaymentByEmployeeAndPeriod(userDetails.getUsername(), period.period);
            PaymentResponse paymentResponse = new PaymentResponse(user, payment);
            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
        }
    }

    public record Period(@ValidDate @JsonFormat(pattern = "MM-yyyy") @DateTimeFormat(pattern = "MM-yyyy") String period) { };
}
