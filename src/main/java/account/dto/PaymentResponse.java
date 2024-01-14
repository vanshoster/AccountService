package account.dto;

import account.validation.ValidDate;
import account.model.Payment;
import account.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class PaymentResponse {
    private String name;
    private String lastname;
    @JsonFormat(pattern = "MM-yyyy")
    @NotNull(message = "Period cannot be null")
    @DateTimeFormat(pattern = "MM-yyyy")
    @ValidDate
    private String period;
    private String salary;

    public PaymentResponse(User user, Payment payment) {
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.period = payment.getPeriod();
        this.salary = payment.getSalary().toString();
    }

    public PaymentResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy");

        YearMonth yearMonth = YearMonth.parse(period, inputFormatter);
        return yearMonth.format(outputFormatter);
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalary() {
        long dollars = Long.valueOf(salary) / 100;
        long cents = Long.valueOf(salary) % 100;

        return String.format("%d dollar(s) %02d cent(s)", dollars, cents);
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
