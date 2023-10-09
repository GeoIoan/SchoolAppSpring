package gr.aueb.cf.schoolappprospring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "SPECIALITIES")
public class Speciality extends AbstractEntity{

    @Column(name = "SPECIALITY", length = 100, unique = false, nullable = false)
    private String speciality;

    @OneToMany(mappedBy = "speciality")
    private List<Teacher> teachers = new ArrayList<>();

    public Speciality () {}
    public Speciality(Long id, String speciality) {
        this.setId(id);
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    protected List<Teacher> getTeachers() {
        return teachers;
    }

    protected void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Teacher> getAllTeachers () {return Collections.unmodifiableList(teachers);
    }

    public boolean addTeacher (Teacher teacher) {
        if (teacher == null) return false;
        if (teacher.getSpeciality() == this) return false;

        teacher.setSpeciality(this);
        this.teachers.add(teacher);
        return true;
    }
}
