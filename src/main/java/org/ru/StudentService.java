package org.ru;

import org.ru.DAO.StudentDAO;
import org.ru.DAO.StudentDAOImpl;

import java.util.List;

public class StudentService {

    private static volatile StudentService INSTANCE;

    private StudentDAO studentDAO;

    private StudentService() {
        studentDAO = StudentDAOImpl.getInstance();
    }

    public static StudentService getInstance() {
        if (INSTANCE == null) {
            synchronized (StudentService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StudentService();
                }
            }
        }
        return INSTANCE;
    }

    public void addStudent(Student newStudent) {
        studentDAO.addStudent(newStudent);
    }

    public void deleteStudent(String uniqueNumber) {
        studentDAO.deleteStudent(uniqueNumber);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }
}
