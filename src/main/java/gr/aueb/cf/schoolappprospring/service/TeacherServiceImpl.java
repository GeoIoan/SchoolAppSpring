package gr.aueb.cf.schoolappprospring.service;

import gr.aueb.cf.schoolappprospring.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolappprospring.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolappprospring.model.Teacher;
import gr.aueb.cf.schoolappprospring.repository.SpecialityRepository;
import gr.aueb.cf.schoolappprospring.repository.TeacherRepository;
import gr.aueb.cf.schoolappprospring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherRepository teacherRepository;

    private final UserRepository userRepository;

    private final SpecialityRepository specialityRepository;


    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, UserRepository userRepository, SpecialityRepository specialityRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.specialityRepository = specialityRepository;
    }

    @Transactional
    @Override
    public Teacher insertTeacher(TeacherInsertDTO dto) throws CannotCreateTransactionException {
        Teacher teacher;
        try{
            teacher = teacherRepository.save(convertInsertDto(dto));
            log.info("Teacher with id " + teacher.getId() + " was inserted");
        } catch (CannotCreateTransactionException e){
            log.warn("Teacher with lastname " + dto.getLastname() + " could not be inserted due to a server error");
            throw e;
        }
        return teacher;
    }


    @Transactional
    @Override
    public Teacher updateTeacher(TeacherUpdateDTO dto) throws CannotCreateTransactionException, EntityNotFoundException {
        Teacher updatedTeacher;
        try{
            updatedTeacher = teacherRepository.save(convertUpdateDto(dto));
            log.info("Teacher with id " + updatedTeacher.getId() + " was updated");
        } catch (javax.persistence.EntityNotFoundException e) {
            log.warn("Update error: Could not find teacher with id " + dto.getId());
            throw e;
        } catch (CannotCreateTransactionException e1) {
            log.warn("Could not update teacher with id " + dto.getId() + " due to a sever error");
            throw e1;
        }
        return updatedTeacher;
    }

    @Transactional
    @Override
    public Teacher deleteTeacher(Long id) throws CannotCreateTransactionException, EmptyResultDataAccessException {
        Teacher teacher;
        try {
            teacher = teacherRepository.getById(id);
            teacherRepository.deleteById(id);
            log.info("Teacher with id " + teacher.getId() + " was deleted");
        } catch (EmptyResultDataAccessException e) {
            log.warn("Delete error: Could not find teacher with id " + id);
            throw e;
        } catch (CannotCreateTransactionException e1) {
            log.warn("Could not delete teacher with id " + id + " due to a server error");
            throw e1;
        }
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByLastname(String lastname) throws CannotCreateTransactionException {
        List<Teacher> teachers;
        try{
        teachers = teacherRepository.getByLastnameStartingWith(lastname);
        log.info("All teachers with lastname " + lastname + " were found");
        } catch (CannotCreateTransactionException e) {
            log.warn("Could not find teachers with lastname " + lastname + " due to a server error");
            throw e;
        }
        return teachers;
    }

    @Override
    public Teacher getTeacherById(Long id) throws CannotCreateTransactionException, javax.persistence.EntityNotFoundException {
        Teacher teacher;
        try{
            teacher = teacherRepository.getById(id);
            log.info("Teacher with id " + id + " was found");
        } catch (javax.persistence.EntityNotFoundException e) {
            log.warn("Could not find teacher with id " + id);
            throw e;
        }catch (CannotCreateTransactionException e1) {
            log.warn("Could not find teacher with id " + id + " due to a server error");
            throw e1;
        }
        return teacher;
    }

    @Override
    public Long getTeachersIdBySSN(String ssn) throws CannotCreateTransactionException{
        return teacherRepository.getTeacherBySSN(ssn);
    }

    @Override
    public Long isUserRegisteredToTeacher(Long id) throws CannotCreateTransactionException{
        return teacherRepository.getIdWithUser(id);
    }

    private Teacher convertInsertDto(TeacherInsertDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setId(null);
        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        teacher.setSsn(dto.getSsn());
        teacher.setUser(userRepository.findById(dto.getUser()));
        teacher.setSpeciality(specialityRepository.findById(dto.getSpeciality()));

        return teacher;
    }

    private Teacher convertUpdateDto(TeacherUpdateDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        teacher.setSsn(dto.getSsn());
        teacher.setUser(userRepository.findById(dto.getUser()));
        teacher.setSpeciality(specialityRepository.findById(dto.getSpeciality()));

        return teacher;
    }
}
