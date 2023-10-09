package gr.aueb.cf.schoolappprospring.repository;

import gr.aueb.cf.schoolappprospring.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> getByLastnameStartingWith(String lastname); // %lastname
    Teacher getById(Long id);
    @Query("SELECT t.id FROM Teacher t WHERE t.ssn= ?1")
    Long getTeacherBySSN(String ssn);

    @Query("SELECT t.id FROM Teacher t WHERE t.user.id = ?1")
    Long getIdWithUser (Long id);
}
