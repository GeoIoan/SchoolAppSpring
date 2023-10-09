package gr.aueb.cf.schoolappprospring.validator;


import gr.aueb.cf.schoolappprospring.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolappprospring.service.IStudentService;
import gr.aueb.cf.schoolappprospring.service.ITeacherService;
import gr.aueb.cf.schoolappprospring.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class TeacherInsertValidator implements Validator {

    private final ITeacherService teacherService;
    private final IStudentService studentService;


    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherInsertDTO.class == clazz;
    }

    @Autowired
    public TeacherInsertValidator(ITeacherService teacherService, IStudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @Override
    public void validate(Object target, Errors errors) throws CannotCreateTransactionException {
        TeacherInsertDTO teacherInsertDTO = (TeacherInsertDTO) target;
        try {

            Long id = teacherService.isUserRegisteredToTeacher(teacherInsertDTO.getUser());

            if (id == null) id = studentService.isUserRegisteredToStudent(teacherInsertDTO.getUser());

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "Firstname's field must not be empty or have whitespaces");

            if (errors.getFieldError("firstname") == null) {
                for (int i = 0; i < teacherInsertDTO.getFirstname().length(); i++) {
                    if (Character.isDigit(teacherInsertDTO.getFirstname().charAt(i))) {
                        errors.rejectValue("firstname", "Firstname field cannot contain digits");
                    }
                }
            }

            if (errors.getFieldError("firstname") == null) {
                if (teacherInsertDTO.getFirstname().length() < 3 || teacherInsertDTO.getFirstname().length() > 50) {
                    errors.rejectValue("firstname", "Firstnames field must be between 3 and 50 characters");
                }
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "Lastnames field must not be empty or have whitespaces");

            if (errors.getFieldError("lastname") == null) {
                for (int i = 0; i < teacherInsertDTO.getLastname().length(); i++) {
                    if (Character.isDigit(teacherInsertDTO.getLastname().charAt(i))) {
                        errors.rejectValue("lastname", "Lastname field cannot contain digits");
                    }
                }
            }

            if (errors.getFieldError("lastname") == null) {
                if (teacherInsertDTO.getLastname().length() < 3 || teacherInsertDTO.getLastname().length() > 50) {
                    errors.rejectValue("lastname", "Lastname field must be between 3 and 50 characters");
                }
            }

            for (int i = 0; i < teacherInsertDTO.getSsn().length(); i++) {
                if (!Character.isDigit(teacherInsertDTO.getSsn().charAt(i))) {
                    errors.rejectValue("ssn", "The provided SSN cannot include any characters");
                    break;
                }
            }

            if (errors.getFieldError("ssn") == null) {
                if (teacherInsertDTO.getSsn().length() != 6)
                    errors.rejectValue("ssn", "The provided SSN must be exactly 6 digits long");
            }

            if (errors.getFieldError("ssn") == null) {
                Long teachersId = teacherService.getTeachersIdBySSN(teacherInsertDTO.getSsn());
                if (teachersId != null) errors.rejectValue("ssn", "The provided SSN must be unique.");
            }

            if (id != null) {
                errors.rejectValue("user", "This username is already registered");
            }
        } catch(CannotCreateTransactionException e) {
            log.warn("Could not check the availability of the  username and ssn of the new teacher with lastname " + teacherInsertDTO.getLastname() + " due to a server error");
            throw e;
        }
    }
}
