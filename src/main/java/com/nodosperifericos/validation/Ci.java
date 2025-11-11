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
@Constraint(validatedBy = Ci.CiValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ci {
    String message() default "CI inválido: el dígito de verificación no coincide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    class CiValidator implements ConstraintValidator<Ci, String> {
        
        private static final int[] WEIGHTS = {2, 9, 8, 7, 6, 3, 4};
        
        @Override
        public void initialize(Ci constraintAnnotation) {
        }
        
        @Override
        public boolean isValid(String ci, ConstraintValidatorContext context) {
            if (ci == null || ci.isEmpty()) {
                return true; // Use @NotNull for required validation
            }
            
            // Clean CI: remove dots and dashes
            String cleanCi = ci.replaceAll("[-.]", "");
            
            // Check format: 7-8 digits
            if (!cleanCi.matches("^\\d{7,8}$")) {
                return false;
            }
            
            // Extract number part and check digit
            String numberPart = cleanCi.length() == 8 ? cleanCi.substring(0, 7) : cleanCi;
            int checkDigit = Integer.parseInt(cleanCi.substring(cleanCi.length() - 1));
            
            // Calculate check digit
            int calculatedCheckDigit = calculateCheckDigit(numberPart);
            
            return calculatedCheckDigit == checkDigit;
        }
        
        private int calculateCheckDigit(String ci) {
            int sum = 0;
            for (int i = 0; i < ci.length() && i < WEIGHTS.length; i++) {
                int digit = Character.getNumericValue(ci.charAt(i));
                sum += digit * WEIGHTS[i];
            }
            
            int remainder = sum % 10;
            return remainder == 0 ? 0 : 10 - remainder;
        }
    }
}

