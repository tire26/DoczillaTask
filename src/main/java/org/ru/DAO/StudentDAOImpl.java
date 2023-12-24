package org.ru.DAO;

import org.ru.ConfigReader;
import org.ru.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAOImpl implements StudentDAO {

    private static final String INSERT_QUERY = "INSERT INTO students (first_name, last_name, middle_name, birth_date," +
            "group_name, unique_number) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM students WHERE unique_number = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM students";
    private static volatile StudentDAOImpl INSTANCE;
    private String url;
    private String username;
    private String password;

    private Logger logger;

    private StudentDAOImpl() {
        Properties properties = ConfigReader.getConfig();
        url = properties.getProperty("database.url");
        username = properties.getProperty("database.username");
        password = properties.getProperty("database.password");

        logger = Logger.getLogger(StudentDAOImpl.class.getName());

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static StudentDAOImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (StudentDAOImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StudentDAOImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void addStudent(Student newStudent) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, newStudent.getFirstName());
            preparedStatement.setString(2, newStudent.getLastName());
            preparedStatement.setString(3, newStudent.getMiddleName());
            preparedStatement.setDate(4, new Date(newStudent.getBirthDate().getTime()));
            preparedStatement.setString(5, newStudent.getGroup());
            preparedStatement.setString(6, newStudent.getUniqueNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void deleteStudent(String uniqueNumber) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setString(1, uniqueNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setMiddleName(resultSet.getString("middle_name"));
                student.setBirthDate(resultSet.getDate("birth_date"));
                student.setGroup(resultSet.getString("group_name"));
                student.setUniqueNumber(resultSet.getString("unique_number"));
                students.add(student);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return students;
    }
}
