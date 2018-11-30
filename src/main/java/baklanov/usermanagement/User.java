package baklanov.usermanagement;

import java.io.Serializable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import  java.time.temporal.ChronoUnit;

public class User implements Serializable{
    private static final long serialVersionUID = 4188062179191390064L;
    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    public User()
    {}

    public User(long userId, String firstName, String lastName, java.sql.Date dateOfBirth) {
        this.id=userId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.dateOfBirth=dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName () {
        return new StringBuilder(getLastName())
                .append(", ")
                .append(getFirstName())
                .toString();
    }

    public long getAge(){
        LocalDate currentDate = LocalDate.now();
        LocalDate date = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.YEARS.between(date, currentDate );
    }
}
