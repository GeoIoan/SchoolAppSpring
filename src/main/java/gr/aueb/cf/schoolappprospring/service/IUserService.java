package gr.aueb.cf.schoolappprospring.service;


import gr.aueb.cf.schoolappprospring.dto.RegisterDTO;
import gr.aueb.cf.schoolappprospring.model.User;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.Map;

public interface IUserService {

    User insertUser (RegisterDTO dto) throws CannotCreateTransactionException;

    User getUser (String username) throws CannotCreateTransactionException;

    Map<Long,String> getAllUsers() throws CannotCreateTransactionException;

   boolean isEmailTaken(String email) throws CannotCreateTransactionException;
}
