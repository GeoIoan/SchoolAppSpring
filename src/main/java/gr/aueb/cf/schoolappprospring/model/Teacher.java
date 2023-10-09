package gr.aueb.cf.schoolappprospring.model;


import javax.persistence.*;

@Entity
@Table(name = "TEACHERS")
public class Teacher extends AbstractEntity{

    @Column(name = "FIRSTNAME", length = 50, unique = false, nullable = true)
    private String firstname;

    @Column(name = "LASTNAME", length = 50, unique = false, nullable = true)
    private String lastname;

    @Column(name = "SSN", length = 10, unique = true, nullable = false)
    private String ssn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", nullable = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPECIALITY_ID", nullable = true)
    private Speciality speciality;


    public Teacher () {}

    public Teacher(Long id, String firstname, String lastname, String ssn) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public boolean addSpeciality (Speciality speciality) {
        if (speciality == null) return false;
        for (Teacher teacher : speciality.getTeachers()) {
            if (teacher == this) return false;
        }
        this.speciality = speciality;
        speciality.getTeachers().add(this);
        return true;
    }
}
