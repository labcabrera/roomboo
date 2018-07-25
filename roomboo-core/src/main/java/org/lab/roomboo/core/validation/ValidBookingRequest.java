package org.lab.roomboo.core.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingRequestValidator.class)
@Documented
public @interface ValidBookingRequest {

	String message() default "Invalid booking request";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
