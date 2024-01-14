package account.controller;

import account.dto.StatusDTO;
import account.model.Payment;
import account.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/acct/payments")
    public ResponseEntity<StatusDTO> addPayments(@RequestBody List<@Valid Payment> payments) {
        return paymentService.saveListOfPayments(payments);
    }

    @GetMapping("/acct/payments")
    public ResponseEntity<Payment> getPaymentByUser(@RequestBody @Valid Payment paymentRequest) {
        Payment payment = paymentService.getPaymentByEmployeeAndPeriod(paymentRequest.getEmployee(), paymentRequest.getPeriod());
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @PutMapping("/acct/payments")
    public ResponseEntity<StatusDTO> updatePayment(@RequestBody @Valid Payment paymentRequest) {
        return paymentService.updatePayment(paymentRequest);
    }
}