package account.service;

import account.dto.StatusDTO;
import account.exception.PaymentAlreadyExistException;
import account.exception.PaymentNotFoundException;
import account.model.Payment;
import account.repository.PaymentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    PaymentsRepository paymentsRepository;

    public void savePayment(Payment payment) {
        paymentsRepository.save(payment);
    }

    public Payment getPaymentByEmployeeAndPeriod(String employee, String period) {
        Payment payment = paymentsRepository.findPaymentByEmployeeIgnoreCaseAndPeriod(employee, period);
        if (payment == null) {
            throw new PaymentNotFoundException();
        }
            return payment;
    }

    public List<Payment> getPaymentsByEmployee(String employee) {
        Sort sort = Sort.by(Sort.Direction.DESC, "period");
        List<Payment> listOfPayments = paymentsRepository.findPaymentsByEmployeeIgnoreCase(employee, sort);
        return listOfPayments;
    }

    @Transactional
    public ResponseEntity<StatusDTO> saveListOfPayments(List<Payment> payments) {
        payments.forEach(payment -> {
            if (paymentsRepository.existsPaymentByEmployeeAndAndPeriod(payment.getEmployee(), payment.getPeriod())) {
                throw new PaymentAlreadyExistException();
            } else {
                paymentsRepository.save(payment);
            }
        });
        return new ResponseEntity<>(new StatusDTO("Added successfully!"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<StatusDTO> updatePayment(Payment payment) {
        Payment paymentForUpdate = getPaymentByEmployeeAndPeriod(payment.getEmployee(), payment.getPeriod());
        paymentForUpdate.setSalary(payment.getSalary());
        paymentsRepository.save(paymentForUpdate);
        return new ResponseEntity<>(new StatusDTO("Updated successfully!"), HttpStatus.OK);
    }
}
