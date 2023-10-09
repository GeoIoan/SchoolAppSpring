package gr.aueb.cf.schoolappprospring.rest;

import gr.aueb.cf.schoolappprospring.authentication.AuthenticationForNow;
import gr.aueb.cf.schoolappprospring.dto.LoginDTO;

import gr.aueb.cf.schoolappprospring.dto.RegisterDTO;

import gr.aueb.cf.schoolappprospring.service.IUserService;
import gr.aueb.cf.schoolappprospring.validator.UserRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final IUserService userService;

    private final UserRegisterValidator userRegisterValidator;


    private final AuthenticationForNow auth;

    @Autowired
    public UserRestController( IUserService userService, UserRegisterValidator userRegisterValidator, AuthenticationForNow auth) {
        this.userService = userService;
        this.userRegisterValidator = userRegisterValidator;
        this.auth = auth;
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try{
            if (!auth.authenticate(dto)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else return new ResponseEntity<>(HttpStatus.OK);
        } catch (CannotCreateTransactionException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto, BindingResult bindingResult) {
        userRegisterValidator.validate(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getCode());
            }

            return ResponseEntity.badRequest().body(errorMap);
        }

        try {
            userService.insertUser(dto);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "User registered successfully");
            return ResponseEntity.ok(successResponse);
        } catch (CannotCreateTransactionException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
