package account.repository;

import account.model.Payment;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PaymentsRepository extends CrudRepository<Payment, Long> {
    Payment findPaymentByEmployeeIgnoreCaseAndPeriod(String employee, String period);
    List<Payment> findPaymentsByEmployeeIgnoreCase(String employee, Sort sort);
    Boolean existsPaymentByEmployeeAndAndPeriod(String employee, String period);


}
