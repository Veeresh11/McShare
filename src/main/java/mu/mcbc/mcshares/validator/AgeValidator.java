package mu.mcbc.mcshares.validator;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<AtLeast18, Instant> {

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        Instant sysTime = Instant.now();
        Long yearsDiff = ChronoUnit.YEARS.between(value.atZone(ZoneId.systemDefault()), sysTime.atZone(ZoneId.systemDefault()));
        if (yearsDiff >= 18) {
            return true;
        } else {
            return false;
        }
    }
}
