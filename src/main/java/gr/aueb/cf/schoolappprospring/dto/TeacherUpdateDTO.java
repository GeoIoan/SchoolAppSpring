package gr.aueb.cf.schoolappprospring.dto;



import gr.aueb.cf.schoolappprospring.model.Speciality;
import gr.aueb.cf.schoolappprospring.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TeacherUpdateDTO extends BaseDTO{


    private String firstname;


    private String lastname;


    private String ssn;


    private long user;


    private long speciality;



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

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getSpeciality() {
        return speciality;
    }

    public void setSpeciality(long speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "TeacherUpdateDTO{" +
                "id='" + super.getId() + '\'' +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", ssn='" + ssn + '\'' +
                ", user=" + user +
                ", speciality=" + speciality +
                '}';
    }
}
