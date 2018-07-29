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
@Constraint(validatedBy = AppUserRegisterRequestValidator.class)
@Documented
public @interface ValidAppUserRegisterRequest {

	String message() default "Invalid user register";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
