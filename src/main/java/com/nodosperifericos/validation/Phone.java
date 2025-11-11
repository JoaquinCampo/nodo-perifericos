package com.nodosperifericos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = Phone.PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "El formato del teléfono no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    class PhoneValidator implements ConstraintValidator<Phone, String> {
        
        private static final String PHONE_PATTERN = "^\\+[1-9]\\d{1,14}$";
        
        @Override
        public void initialize(Phone constraintAnnotation) {
        }
        
        @Override
        public boolean isValid(String phone, ConstraintValidatorContext context) {
            if (phone == null || phone.isEmpty()) {
                return true; // Use @NotNull for required validation
            }
            
            return phone.matches(PHONE_PATTERN);
        }
    }
}

