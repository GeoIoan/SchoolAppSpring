package gr.aueb.cf.schoolappprospring.service;



import gr.aueb.cf.schoolappprospring.model.Speciality;
import org.springframework.transaction.CannotCreateTransactionException;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;

public interface ISpecialityService {

    Map<Long, String> getAllSpecialities () throws CannotCreateTransactionException;
}
