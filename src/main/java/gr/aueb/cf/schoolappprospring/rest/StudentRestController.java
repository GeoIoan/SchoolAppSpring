package gr.aueb.cf.schoolappprospring.rest;

import gr.aueb.cf.schoolappprospring.dto.StudentInsertDTO;
import gr.aueb.cf.schoolappprospring.dto.StudentReadOnlyDTO;
import gr.aueb.cf.schoolappprospring.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolappprospring.model.Student;
import gr.aueb.cf.schoolappprospring.service.ICityService;
import gr.aueb.cf.schoolappprospring.service.IStudentService;
import gr.aueb.cf.schoolappprospring.service.IUserService;
import gr.aueb.cf.schoolappprospring.service.util.DateUtil;
import gr.aueb.cf.schoolappprospring.validator.StudentInsertValidator;
import gr.aueb.cf.schoolappprospring.validator.StudentUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private final IStudentService studentService;

    private final StudentInsertValidator studentInsertValidator;

    private final StudentUpdateValidator studentUpdateValidator;

    private final IUserService userService;

    private final ICityService cityService;

    @Autowired
    public StudentRestController(IStudentService studentService, StudentInsertValidator studentInsertValidator, StudentUpdateValidator studentUpdateValidator, IUserService userService, ICityService cityService) {
        this.studentService = studentService;
        this.studentInsertValidator = studentInsertValidator;
        this.studentUpdateValidator = studentUpdateValidator;
        this.userService = userService;
        this.cityService = cityService;
    }

    @GetMapping("/students/options")
    public ResponseEntity<Map<String, Map<Long, String>>> getOptions () {
        try{
        Map<String, Map<Long, String>> options = new HashMap<>();
        Map <Long, String> usernames;
        Map<Long, String> cities;

        usernames = userService.getAllUsers();
        cities = cityService.getAllCities();

        options.put("usernames", usernames);
        options.put("cities", cities);

        return new ResponseEntity<>(options, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students")
    public ResponseEntity<?> getStudentsByLastname(@RequestParam("lastname") String lastname) {
        List<Student> students;
        try{
            students = studentService.getStudentByLastname(lastname);
            List<StudentReadOnlyDTO> studentsDto = new ArrayList<>();
            for (Student student : students) {
                studentsDto.add(convertToReadOnlyDto(student));
            }
            return new ResponseEntity<>(studentsDto, HttpStatus.OK);
        }catch (CannotCreateTransactionException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable("studentId") Long studentId) {
        Student student;
        try{
            student = studentService.getStudentById(studentId);
            StudentReadOnlyDTO dto = convertToReadOnlyDto(student);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (javax.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>("Could not be found", HttpStatus.BAD_REQUEST);
        }catch (CannotCreateTransactionException e1) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/students")
    public ResponseEntity<?> addStudent (@RequestBody StudentInsertDTO insertDto,  BindingResult bindingResult) {
        studentInsertValidator.validate(insertDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getCode());
            }

            return ResponseEntity.badRequest().body(errorMap);
        }
        try{
            Student student = studentService.insertStudent(insertDto);
            StudentReadOnlyDTO dto = convertToReadOnlyDto(student);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("{id}")
                    .buildAndExpand(dto.getId())
                    .toUri();
            return ResponseEntity.created(location).body(dto);
        }catch (ParseException e){
            return new ResponseEntity<>("Wrong date format", HttpStatus.BAD_REQUEST);
        } catch (CannotCreateTransactionException e1) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(path = "/students/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent (@PathVariable("id") Long id){
        try{
            Student student = studentService.deleteStudent(id);
            StudentReadOnlyDTO dto = convertToReadOnlyDto(student);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Could not be found", HttpStatus.BAD_REQUEST);
        } catch (CannotCreateTransactionException e1) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(path= "/students", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStudent(@Valid @RequestBody StudentUpdateDTO updateDto,
                                           BindingResult bindingResult) {
        studentUpdateValidator.validate(updateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getCode());
            }

            return ResponseEntity.badRequest().body(errorMap);
        }

        try{
            Student student = studentService.updateStudent(updateDto);
            StudentReadOnlyDTO dto = convertToReadOnlyDto(student);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (ParseException e) {
            return new ResponseEntity<>("Wrong date format", HttpStatus.BAD_REQUEST);
        } catch (javax.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>("Could not be found", HttpStatus.BAD_REQUEST);
        } catch (CannotCreateTransactionException e2) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private StudentReadOnlyDTO convertToReadOnlyDto (Student student) {
        StudentReadOnlyDTO readOnlyDTO = new StudentReadOnlyDTO();
        readOnlyDTO.setId(student.getId());
        readOnlyDTO.setFirstname(student.getFirstname());
        readOnlyDTO.setLastname(student.getLastname());
        readOnlyDTO.setGender(student.getGender());
        readOnlyDTO.setBirthdate(DateUtil.toString(student.getBirthdate()));
        readOnlyDTO.setUsername(student.getUser().getUsername());
        readOnlyDTO.setCity(student.getCity().getCity());

        return readOnlyDTO;
    }
}
