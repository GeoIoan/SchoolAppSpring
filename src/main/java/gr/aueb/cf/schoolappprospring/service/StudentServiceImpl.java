package gr.aueb.cf.schoolappprospring.service;

import gr.aueb.cf.schoolappprospring.dto.StudentInsertDTO;
import gr.aueb.cf.schoolappprospring.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolappprospring.model.Student;
import gr.aueb.cf.schoolappprospring.repository.CityRepository;
import gr.aueb.cf.schoolappprospring.repository.StudentRepository;
import gr.aueb.cf.schoolappprospring.repository.UserRepository;
import gr.aueb.cf.schoolappprospring.service.IStudentService;
import gr.aueb.cf.schoolappprospring.service.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final CityRepository cityRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, CityRepository cityRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    @Override
    public Student insertStudent(StudentInsertDTO dto) throws CannotCreateTransactionException, ParseException {
        Student student;
        try{
            student = studentRepository.save(convertInsertDto(dto));
            log.info("Student with id " + student.getId() + " was inserted");
        } catch (ParseException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (CannotCreateTransactionException e1) {
            log.warn("The insertion of student with lastname " + dto.getLastname() + " could not be completed due to server error");
            throw e1;
        }
        return student;
    }

    @Transactional
    @Override
    public Student updateStudent(StudentUpdateDTO dto) throws CannotCreateTransactionException, EntityNotFoundException, ParseException {
        Student updatedStudent;
        try{
            updatedStudent = studentRepository.save(convertUpdateDto(dto));
            log.warn("Student with id " + updatedStudent.getId() + " was updated");
        } catch (ParseException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (javax.persistence.EntityNotFoundException e1) {
            log.warn("Student with id " + dto.getId() + " waw not found");
            throw e1;
        } catch (CannotCreateTransactionException e2) {
            log.warn("Student with id " + dto.getId() + " could not be updated due to a server error");
            throw e2;
        }
        return updatedStudent;
    }

    @Transactional
    @Override
    public Student deleteStudent(Long id) throws CannotCreateTransactionException, EmptyResultDataAccessException {
        Student student;
        try{
            student = studentRepository.getById(id);
            studentRepository.deleteById(id);
            log.info("Student with id " + id + " was deleted");
        } catch (EmptyResultDataAccessException e) {
            log.warn("Student with id " + id + " could not be deleted");
            throw e;
        } catch (CannotCreateTransactionException e1) {
            log.warn("Student with id " + id + " could not be deleted due to a server error");
            throw e1;
        }
        return student;
    }

    @Override
    public List<Student> getStudentByLastname(String lastname) throws CannotCreateTransactionException{
        List<Student> students;
        try{
        students = studentRepository.getByLastnameStartingWith(lastname);
        } catch (CannotCreateTransactionException e) {
            log.warn("Students with lastname " + lastname + " could not be found due to a server error");
            throw e;
        }
        return students;
    }

    @Override
    public Student getStudentById(Long id) throws CannotCreateTransactionException, javax.persistence.EntityNotFoundException {
        Student student;
        try {
            student = studentRepository.getById(id);
            log.info("Student with id " + id + " was found");
        } catch (javax.persistence.EntityNotFoundException e) {
            log.warn("Student with id " + id + " could not be found");
            throw e;
        } catch (CannotCreateTransactionException e1) {
            log.warn("Student with id " + id + " could not be found due to a server error");
            throw e1;
        }
        return student;
    }

    @Override
    public Long isUserRegisteredToStudent(Long id) throws CannotCreateTransactionException{
        return studentRepository.getIdWithUser(id);
    }

    private Student convertInsertDto(StudentInsertDTO dto) throws ParseException{
        Student student = new Student();
        try{
            student.setId(null);
            student.setFirstname(dto.getFirstname());
            student.setLastname(dto.getLastname());
            student.setGender(dto.getGender());
            student.setBirthdate(DateUtil.toDate(dto.getBirthdate()));
            student.setUser(userRepository.findById(dto.getUser()));
            student.setCity(cityRepository.findById(dto.getCity()));
        }catch (ParseException e){
            throw e;
        }

        return student;
    }

    private Student convertUpdateDto(StudentUpdateDTO dto) throws ParseException{
        Student student = new Student();
        try{
            student.setId(dto.getId());
            student.setFirstname(dto.getFirstname());
            student.setLastname(dto.getLastname());
            student.setGender(dto.getGender());
            student.setBirthdate(DateUtil.toDate(dto.getBirthdate()));
            student.setUser(userRepository.findById(dto.getUser()));
            student.setCity(cityRepository.findById(dto.getCity()));
        }catch (ParseException e){
            throw e;
        }

        return student;
    }
}
