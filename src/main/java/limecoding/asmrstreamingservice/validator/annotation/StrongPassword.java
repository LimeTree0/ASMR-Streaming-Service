package limecoding.asmrstreamingservice.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import limecoding.asmrstreamingservice.validator.implementation.PasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "비밀번호는 8~20자 길이이며, 영문, 숫자, 특수문자를 포함해야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
