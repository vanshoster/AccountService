package account.model;

import account.validation.ValidDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="PAYMENTS")
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Employee cannot be blank")
    private String employee;
    @JsonFormat(pattern = "MM-yyyy")
    @NotBlank(message = "Period cannot be blank")
    @ValidDate
    private String period;
    @NotNull(message = "Salary cannot be null")
    @Min(value = 0, message = "Salary must be non negative!")
    private Long salary;

    public Payment() {
    }

    public Payment(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
