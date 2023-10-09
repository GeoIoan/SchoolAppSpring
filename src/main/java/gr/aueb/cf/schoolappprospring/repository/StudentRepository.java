package gr.aueb.cf.schoolappprospring.repository;

import gr.aueb.cf.schoolappprospring.model.Student;
import gr.aueb.cf.schoolappprospring.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> getByLastnameStartingWith(String lastname);
    Student getById(Long id);

    @Query("SELECT s.id FROM Student s WHERE s.user.id = ?1")
    Long getIdWithUser (Long id);
}
