package gr.aueb.cf.schoolappprospring.service;

import gr.aueb.cf.schoolappprospring.model.City;
import gr.aueb.cf.schoolappprospring.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

@Service
@Slf4j
public class CityServiceImpl implements ICityService{

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Map<Long, String> getAllCities() throws CannotCreateTransactionException{
        try{
        List<City> cities = cityRepository.findAll();
        Map<Long, String> citiesMap = new HashMap<>();
        cities.forEach(city -> citiesMap.put(city.getId(), city.getCity()));
        return citiesMap;
        } catch (CannotCreateTransactionException e) {
            log.warn("Could not get all cities due to a server error");
            throw e;
        }
    }
}
