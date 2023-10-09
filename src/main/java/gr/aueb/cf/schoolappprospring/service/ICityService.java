package gr.aueb.cf.schoolappprospring.service;


import gr.aueb.cf.schoolappprospring.model.City;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;
import java.util.Map;

public interface ICityService {

    Map<Long, String> getAllCities () throws CannotCreateTransactionException;
}
