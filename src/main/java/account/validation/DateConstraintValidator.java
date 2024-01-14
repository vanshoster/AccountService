package account.validation;

import account.exception.NullArgumentException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConstraintValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public void initialize(ValidDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {

        if (date == null) {
            throw new NullArgumentException();
        }
        try {
            YearMonth.parse(date, DateTimeFormatter.ofPattern("MM-yyyy"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

