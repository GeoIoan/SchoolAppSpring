package gr.aueb.cf.schoolappprospring.service;


import gr.aueb.cf.schoolappprospring.dto.StudentInsertDTO;
import gr.aueb.cf.schoolappprospring.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolappprospring.model.Student;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.CannotCreateTransactionException;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.util.List;

public interface IStudentService {

    Student insertStudent (StudentInsertDTO dto) throws CannotCreateTransactionException, ParseException;

    Student updateStudent (StudentUpdateDTO dto) throws CannotCreateTransactionException, EntityNotFoundException, ParseException;

    Student deleteStudent (Long id) throws CannotCreateTransactionException, EmptyResultDataAccessException;

    List<Student> getStudentByLastname (String lastname) throws CannotCreateTransactionException;

    Student getStudentById (Long id) throws CannotCreateTransactionException, javax.persistence.EntityNotFoundException;

    Long isUserRegisteredToStudent(Long id) throws CannotCreateTransactionException;
}
