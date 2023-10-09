package gr.aueb.cf.schoolappprospring.validator;

import gr.aueb.cf.schoolappprospring.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolappprospring.repository.StudentRepository;
import gr.aueb.cf.schoolappprospring.repository.TeacherRepository;
import gr.aueb.cf.schoolappprospring.service.IUserService;
import gr.aueb.cf.schoolappprospring.service.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class StudentUpdateValidator implements Validator {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return StudentUpdateDTO.class == clazz;
    }

    @Autowired
    public StudentUpdateValidator(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void validate(Object target, Errors errors) throws CannotCreateTransactionException {
        StudentUpdateDTO studentUpdateDTO = (StudentUpdateDTO) target;
        try {
            Long id = studentRepository.getIdWithUser(studentUpdateDTO.getUser());
            if (id == null) id = teacherRepository.getIdWithUser(studentUpdateDTO.getUser());

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "Firstname's field must not be empty or have whitespaces");

            if (errors.getFieldError("firstname") == null) {
                for (int i = 0; i < studentUpdateDTO.getFirstname().length(); i++) {
                    if (Character.isDigit(studentUpdateDTO.getFirstname().charAt(i))) {
                        errors.rejectValue("firstname", "Firstname field cannot contain digits");
                    }
                }
            }

            if (errors.getFieldError("firstname") == null) {
                if (studentUpdateDTO.getFirstname().length() < 3 || studentUpdateDTO.getFirstname().length() > 50) {
                    errors.rejectValue("firstname", "Firstnames field must be between 3 and 50 characters");
                }
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "Lastnames field must not be empty or have whitespaces");

            if (errors.getFieldError("lastname") == null) {
                for (int i = 0; i < studentUpdateDTO.getLastname().length(); i++) {
                    if (Character.isDigit(studentUpdateDTO.getLastname().charAt(i))) {
                        errors.rejectValue("lastname", "Lastname field cannot contain digits");
                    }
                }
            }

            if (errors.getFieldError("lastname") == null) {
                if (studentUpdateDTO.getLastname().length() < 3 || studentUpdateDTO.getLastname().length() > 50) {
                    errors.rejectValue("lastname", "Lastname field must be between 3 and 50 characters");
                }
            }

            if (id != null && !id.equals(studentUpdateDTO.getId())) {
                errors.rejectValue("user", "This username is already registered");
            }

            if (studentUpdateDTO.getGender() == null) errors.rejectValue("gender", "Gender's field must no be empty");

            if (!DateUtil.isDateValid(studentUpdateDTO.getBirthdate()))
                errors.rejectValue("birthdate", "Invalid birthdate");
        } catch (CannotCreateTransactionException e) {
            log.warn("Could not check the availability of the updated username and ssn of teacher with id " + studentUpdateDTO.getId() + " due to a server error");
            throw e;
        }
    }
}
