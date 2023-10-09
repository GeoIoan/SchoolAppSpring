package gr.aueb.cf.schoolappprospring.service;

import gr.aueb.cf.schoolappprospring.model.Speciality;
import gr.aueb.cf.schoolappprospring.repository.SpecialityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpecialityServiceImpl implements ISpecialityService{

    private final SpecialityRepository specialityRepository;

    @Autowired
    public SpecialityServiceImpl(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    @Override
    public Map<Long, String> getAllSpecialities() throws CannotCreateTransactionException {
        try {
            List<Speciality> specialities = specialityRepository.findAll();
            Map<Long, String> specialitiesMap = new HashMap<>();
            specialities.forEach(speciality -> specialitiesMap.put(speciality.getId(), speciality.getSpeciality()));
            return specialitiesMap;
        } catch (CannotCreateTransactionException e) {
            log.warn("Could not get all specialities due to a server error");
            throw e;
        }
    }
}
