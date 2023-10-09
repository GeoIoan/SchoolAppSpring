package gr.aueb.cf.schoolappprospring.repository;

import gr.aueb.cf.schoolappprospring.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    City findById(long id);
}
