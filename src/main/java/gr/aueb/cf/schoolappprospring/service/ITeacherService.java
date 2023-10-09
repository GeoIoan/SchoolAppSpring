package gr.aueb.cf.schoolappprospring.service;



import gr.aueb.cf.schoolappprospring.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolappprospring.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolappprospring.model.Teacher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher (TeacherInsertDTO dto) throws CannotCreateTransactionException;
    Teacher updateTeacher (TeacherUpdateDTO dto) throws CannotCreateTransactionException, javax.persistence.EntityNotFoundException;
    Teacher deleteTeacher (Long id) throws CannotCreateTransactionException, EmptyResultDataAccessException;

    List<Teacher> getTeachersByLastname(String lastname) throws CannotCreateTransactionException;
    Teacher getTeacherById(Long id) throws CannotCreateTransactionException, javax.persistence.EntityNotFoundException;

    Long getTeachersIdBySSN(String ssn) throws CannotCreateTransactionException;;

    Long isUserRegisteredToTeacher(Long id) throws CannotCreateTransactionException;;
}
