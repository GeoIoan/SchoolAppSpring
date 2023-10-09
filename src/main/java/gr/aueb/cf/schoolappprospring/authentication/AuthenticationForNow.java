package gr.aueb.cf.schoolappprospring.authentication;

import gr.aueb.cf.schoolappprospring.dto.LoginDTO;
import gr.aueb.cf.schoolappprospring.repository.UserRepository;
import gr.aueb.cf.schoolappprospring.service.IUserService;
import gr.aueb.cf.schoolappprospring.service.security.SecUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;

@Component
@Slf4j
public class AuthenticationForNow {

    private final IUserService userService;

    @Autowired
    public AuthenticationForNow(IUserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(LoginDTO dto) throws CannotCreateTransactionException {
        boolean isUserAuthenticated;
        if (userService.isEmailTaken(dto.getUsername()) && SecUtil.checkPassword(dto.getPassword(), userService.getUser(dto.getUsername()).getPassword())) {
            isUserAuthenticated = true;
            log.info("User with username " + dto.getUsername() + " was authenticated");
        } else isUserAuthenticated = false;

        return isUserAuthenticated;
    }
}
