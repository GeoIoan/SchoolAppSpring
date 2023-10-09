package gr.aueb.cf.schoolappprospring.repository;

import gr.aueb.cf.schoolappprospring.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {

    Speciality findById(long id);
}
