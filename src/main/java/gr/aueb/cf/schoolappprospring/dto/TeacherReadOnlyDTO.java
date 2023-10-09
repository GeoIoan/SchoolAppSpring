package gr.aueb.cf.schoolappprospring.dto;

public class TeacherReadOnlyDTO extends BaseDTO{

    private String firstname;
    private String lastname;

    private String ssn;

    private String username;

    private String speciality;


    public TeacherReadOnlyDTO() {
    }

    public TeacherReadOnlyDTO(Long id, String firstname, String lastname, String ssn, String username, String speciality) {
        this.setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.username = username;
        this.speciality = speciality;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "TeacherReadOnlyDTO{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", ssn='" + ssn + '\'' +
                ", username='" + username + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
