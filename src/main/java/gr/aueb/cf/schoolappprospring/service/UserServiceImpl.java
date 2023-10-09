package gr.aueb.cf.schoolappprospring.service;

import gr.aueb.cf.schoolappprospring.dto.RegisterDTO;
import gr.aueb.cf.schoolappprospring.model.User;
import gr.aueb.cf.schoolappprospring.repository.UserRepository;
import gr.aueb.cf.schoolappprospring.service.security.SecUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements IUserService{

    public final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User insertUser(RegisterDTO dto) throws CannotCreateTransactionException{
        User user;
        try{
        user = mapUser(dto);
        user = userRepository.save(user);
        log.info("User with username " + user.getUsername() + " was inserted");
        } catch (CannotCreateTransactionException e) {
            log.warn("User with username " + dto.getUsername() + " was not inserted due to a server error");
            throw e;
        }
        return user;
    }

    @Override
    public User getUser(String username) throws CannotCreateTransactionException{
        return userRepository.getUserByEmail(username);
    }

    @Override
    public Map<Long, String> getAllUsers() throws CannotCreateTransactionException {
        try{
            List<User> users = userRepository.findAll();
            Map<Long, String> usersMap = new HashMap<>();
            users.forEach(user -> usersMap.put(user.getId(),user.getUsername()));
            return usersMap;
        } catch (CannotCreateTransactionException e) {
            log.warn("Could not get all users due to a server error");
            throw e;
        }
    }
    @Override
    public boolean isEmailTaken(String email) throws CannotCreateTransactionException{
        return userRepository.emailExists(email);
    }

    private User mapUser (RegisterDTO dto) {
        User user = new User();
        user.setId(null);
        user.setUsername(dto.getUsername());
        user.setPassword(SecUtil.hashPassword(dto.getPassword()));
        return user;
    }
}
