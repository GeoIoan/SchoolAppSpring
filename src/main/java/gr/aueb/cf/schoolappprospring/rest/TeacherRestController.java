package gr.aueb.cf.schoolappprospring.rest;

import gr.aueb.cf.schoolappprospring.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolappprospring.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolappprospring.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolappprospring.model.Teacher;
import gr.aueb.cf.schoolappprospring.service.ISpecialityService;
import gr.aueb.cf.schoolappprospring.service.ITeacherService;
import gr.aueb.cf.schoolappprospring.service.IUserService;
import gr.aueb.cf.schoolappprospring.validator.TeacherInsertValidator;
import gr.aueb.cf.schoolappprospring.validator.TeacherUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TeacherRestController {
    private final ITeacherService teacherService;
    private final TeacherInsertValidator teacherInsertValidator;
    private final TeacherUpdateValidator teacherUpdateValidator;

    private final IUserService userService;

    private final ISpecialityService specialityService;


    @Autowired
    public TeacherRestController(ITeacherService teacherService, TeacherInsertValidator teacherInsertValidator, TeacherUpdateValidator teacherUpdateValidator, IUserService userService, ISpecialityService specialityService) {
        this.teacherService = teacherService;
        this.teacherInsertValidator = teacherInsertValidator;
        this.teacherUpdateValidator = teacherUpdateValidator;
        this.userService = userService;
        this.specialityService = specialityService;
    }


    @GetMapping("/teachers/options")
    public ResponseEntity<Map<String, Map<Long, String>>> getOptions () {
        try{
        Map<String, Map<Long, String>> options = new HashMap<>();
        Map <Long, String> usernames;
        Map<Long, String> specialities;

        usernames = userService.getAllUsers();
        specialities = specialityService.getAllSpecialities();

        options.put("usernames", usernames);
        options.put("specialities", specialities);

        return new ResponseEntity<>(options, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get teachers by their lastname starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid lastname supplied",
                    content = @Content)})
    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachersByLastname (@RequestParam("lastname") String lastname) {
        List<Teacher> teachers;
        try{
            teachers = teacherService.getTeachersByLastname(lastname);
            if (teachers.isEmpty()) {
                return new ResponseEntity<>("Could not find any teachers with lastname " + lastname, HttpStatus.BAD_REQUEST);
            }
            List<TeacherReadOnlyDTO> teachersDto = new ArrayList<>();
            for (Teacher teacher : teachers) {
                teachersDto.add(convertToReadOnlyDto(teacher));
            }
            return new ResponseEntity<>(teachersDto, HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<?> getTeacherById(@PathVariable("teacherId") Long teacherId) {
        Teacher teacher;
        try{
            teacher = teacherService.getTeacherById(teacherId);
            TeacherReadOnlyDTO dto = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (javax.persistence.EntityNotFoundException e) {
            return new ResponseEntity<>("Could not find teacher with id " + teacherId, HttpStatus.NOT_FOUND);
        } catch (CannotCreateTransactionException e1) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Add a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})

    @PostMapping("/teachers")
    public ResponseEntity<?> addTeacher (@RequestBody TeacherInsertDTO insertDto, BindingResult bindingResult) {
        teacherInsertValidator.validate(insertDto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getCode());
            }

            return ResponseEntity.badRequest().body(errorMap);
        }
        try{
            Teacher teacher = teacherService.insertTeacher(insertDto);
            TeacherReadOnlyDTO dto = convertToReadOnlyDto(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("{id}")
                    .buildAndExpand(dto.getId())
                    .toUri();
            return ResponseEntity.created(location).body(dto);
        }catch (CannotCreateTransactionException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @RequestMapping(path = "/teachers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTeacher (@PathVariable("id") Long id) {
        try {
            Teacher teacher =  teacherService.deleteTeacher(id);
            TeacherReadOnlyDTO dto = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Could not find teacher with id " + id, HttpStatus.NOT_FOUND);
        } catch (CannotCreateTransactionException e1) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content) })
    @RequestMapping(path= "/teachers", method = RequestMethod.PUT)
    public ResponseEntity<?> updateTeacher (@Valid @RequestBody TeacherUpdateDTO updateDto,
                                                             BindingResult bindingResult) {
        teacherUpdateValidator.validate(updateDto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getCode());
            }

            return ResponseEntity.badRequest().body(errorMap);
        }

        try {
            Teacher teacher = teacherService.updateTeacher(updateDto);
            TeacherReadOnlyDTO teacherReadOnlyDTO = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Could not find teacher with id " + updateDto.getId(), HttpStatus.NOT_FOUND);
        } catch (CannotCreateTransactionException e1) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private TeacherReadOnlyDTO convertToReadOnlyDto (Teacher teacher) {
        TeacherReadOnlyDTO readOnlyDTO = new TeacherReadOnlyDTO();
        readOnlyDTO.setId(teacher.getId());
        readOnlyDTO.setFirstname(teacher.getFirstname());
        readOnlyDTO.setLastname(teacher.getLastname());
        readOnlyDTO.setSsn(teacher.getSsn());
        readOnlyDTO.setUsername(teacher.getUser().getUsername());
        readOnlyDTO.setSpeciality(teacher.getSpeciality().getSpeciality());

        return readOnlyDTO;
    }

}
