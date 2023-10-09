package gr.aueb.cf.schoolappprospring.validator;

import gr.aueb.cf.schoolappprospring.dto.RegisterDTO;
import gr.aueb.cf.schoolappprospring.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@Slf4j
@Component
public class UserRegisterValidator implements Validator {

    private final IUserService userService;

    @Autowired
    public UserRegisterValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) throws CannotCreateTransactionException {
        RegisterDTO userToRegister = (RegisterDTO) target;
        try {
            if (userService.isEmailTaken(userToRegister.getUsername())) {
                errors.rejectValue("username", "This username already exists");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "The password field cannot be empty");

            if(errors.getFieldError("password") == null) {
            if (!userToRegister.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{3,32}$")){
                errors.rejectValue("password", "The password must be between 3 and 32 characters long and must contain at least one digit, one special character, and no whitespace");
            }}

            if(errors.getFieldError("password") == null) {
            if (!Objects.equals(userToRegister.getPassword(), userToRegister.getConfirmedPassword())) {
                errors.rejectValue("confirmedPassword", "The provided passwords do not match");
            }}
        } catch (CannotCreateTransactionException e) {
                log.warn("Could not check the availability of the username " + userToRegister.getUsername() + " due to a server error");
                throw e;
            }

    }
}
