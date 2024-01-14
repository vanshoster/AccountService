package account.validation;

import account.exception.CompromisedPasswordException;
import account.exception.NullArgumentException;
import account.exception.PasswordLengthException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Set<String> INVALID_PASSWORDS = new HashSet<>(Arrays.asList(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
    ));

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            throw new NullArgumentException();
        } else if (INVALID_PASSWORDS.contains(password)) {
            throw new CompromisedPasswordException();
        } else if (password.length() < 12) {
            throw new PasswordLengthException();
        }
        return true;
    }
}

