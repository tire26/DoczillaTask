package org.ru.DAO;

import org.ru.Student;

import java.util.List;

public interface StudentDAO {
    void addStudent(Student student);
    void deleteStudent(String uniqueNumber);
    List<Student> getAllStudents();
}
