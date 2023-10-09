package gr.aueb.cf.schoolappprospring.validator;


import gr.aueb.cf.schoolappprospring.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolappprospring.service.IStudentService;
import gr.aueb.cf.schoolappprospring.service.ITeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class TeacherUpdateValidator implements Validator {

    private final ITeacherService teacherService;
    private final IStudentService studentService;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherUpdateDTO.class == clazz;
    }

    @Autowired
    public TeacherUpdateValidator(ITeacherService teacherService, IStudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @Override
    public void validate(Object target, Errors errors) throws CannotCreateTransactionException {
        TeacherUpdateDTO teacherUpdateDTO = (TeacherUpdateDTO) target;
        try{
            Long id = teacherService.isUserRegisteredToTeacher(teacherUpdateDTO.getUser());

            if (id == null) id = studentService.isUserRegisteredToStudent(teacherUpdateDTO.getUser());

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "Firstname's field must not be empty or have whitespaces");

            if(errors.getFieldError("firstname") == null) {
                for (int i = 0; i < teacherUpdateDTO.getFirstname().length(); i++) {
                    if (Character.isDigit(teacherUpdateDTO.getFirstname().charAt(i))) {
                        errors.rejectValue("firstname", "Firstname field cannot contain digits");
                    }
                }
            }

            if(errors.getFieldError("firstname") == null) {
                if (teacherUpdateDTO.getFirstname().length() < 3 || teacherUpdateDTO.getFirstname().length() > 50){
                    errors.rejectValue("firstname", "Firstnames field must be between 3 and 50 characters");
                }
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "Lastnames field must not be empty or have whitespaces");

            if(errors.getFieldError("lastname") == null) {
                for (int i = 0; i < teacherUpdateDTO.getLastname().length(); i++) {
                    if (Character.isDigit(teacherUpdateDTO.getLastname().charAt(i))) {
                        errors.rejectValue("lastname", "Lastname field cannot contain digits");
                    }
                }
            }

            if(errors.getFieldError("lastname") == null) {
                if (teacherUpdateDTO.getLastname().length() < 3 || teacherUpdateDTO.getLastname().length() > 50) {
                    errors.rejectValue("lastname", "Lastname field must be between 3 and 50 characters");
                }
            }

            for (int i = 0; i < teacherUpdateDTO.getSsn().length(); i++){
                if (!Character.isDigit(teacherUpdateDTO.getSsn().charAt(i))) {
                    errors.rejectValue("ssn", "The provided SSN cannot include any characters");
                    break;
                }
            }

            if (errors.getFieldError("ssn") == null) {
                if (teacherUpdateDTO.getSsn().length() != 6) errors.rejectValue("ssn", "The provided SSN must be exactly 6 digits long");
            }

            if (errors.getFieldError("ssn") == null) {
                Long teachersId = teacherService.getTeachersIdBySSN(teacherUpdateDTO.getSsn());
                System.out.println("teachersId: " + teachersId);
                if (teachersId != null && (!teachersId.equals(teacherUpdateDTO.getId()))) errors.rejectValue("ssn", "The provided SSN must be unique.");
            }

            if (id != null && (!id.equals(teacherUpdateDTO.getId()))) {
                errors.rejectValue("user", "This username is already registered");
            }
        }catch (CannotCreateTransactionException e) {
            log.warn("Could not check the availability of the updated username and ssn of teacher with id " + teacherUpdateDTO.getId() + " due to a server error");
            throw e;
        }
    }
}
