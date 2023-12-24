package org.ru;

import java.util.Date;
import java.util.StringJoiner;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date birthDate;
    private String group;
    private String uniqueNumber;

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String unique_number) {
        this.uniqueNumber = unique_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        if (getId() != student.getId()) return false;
        if (!getFirstName().equals(student.getFirstName())) return false;
        if (!getLastName().equals(student.getLastName())) return false;
        if (!getMiddleName().equals(student.getMiddleName())) return false;
        if (!getBirthDate().equals(student.getBirthDate())) return false;
        if (!getGroup().equals(student.getGroup())) return false;
        return getUniqueNumber().equals(student.getUniqueNumber());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getMiddleName().hashCode();
        result = 31 * result + getBirthDate().hashCode();
        result = 31 * result + getGroup().hashCode();
        result = 31 * result + getUniqueNumber().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("middleName='" + middleName + "'")
                .add("birthDate=" + birthDate)
                .add("group='" + group + "'")
                .add("unique_number='" + uniqueNumber + "'")
                .toString();
    }
}
