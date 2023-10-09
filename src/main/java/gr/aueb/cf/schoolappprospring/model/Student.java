package gr.aueb.cf.schoolappprospring.model;


import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "STUDENTS")
public class Student extends AbstractEntity{

    @Column(name = "FIRSTNAME", length = 50, unique = false, nullable = true)
    private String firstname;

    @Column(name = "LASTNAME", length = 50, unique = false, nullable = true)
    private String lastname;

    @Column(name = "GENDER", length = 1, unique = false, nullable = true)
    private String gender;

    @Column (name = "BIRTHDATE", length = 10, unique = false, nullable = true)
    private Date birthdate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", nullable = true)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "CITY_ID", nullable = true)
    private City city;

    public Student () {}

    public Student(Long id, String firstname, String lastname, String gender, Date birthdate) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthdate = birthdate;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean addCity (City city) {
        if (city == null) return false;
        for (Student student : city.getStudents()) {
            if (student == this) return false;
        }

        this.city = city;
        city.getStudents().add(this);
        return true;
    }
}
