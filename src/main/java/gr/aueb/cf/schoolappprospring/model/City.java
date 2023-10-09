package gr.aueb.cf.schoolappprospring.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CITIES")
public class City extends AbstractEntity{

    @Column(name = "CITY", length = 50, unique = false, nullable = true)
    private String city;

    @OneToMany(mappedBy = "city")
    private List<Student> students = new ArrayList<>();

    public City () {}

    public City(Long id, String city) {
        this.setId(id);
        this.city = city;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    protected List<Student> getStudents() {
        return students;
    }

    protected void setStudents(List<Student> student) {
        this.students = student;
    }

    public boolean addStudent (Student student){
        if (student == null) return false;
        if (student.getCity() == this) return false;

        student.addCity(this);
        this.getStudents().add(student);
        return true;
    }
}
