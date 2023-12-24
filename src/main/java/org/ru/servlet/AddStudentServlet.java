package org.ru.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ru.Student;
import org.ru.StudentService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/addStudent"})
public class AddStudentServlet extends HttpServlet {

    private final StudentService studentService;
    private final Logger logger;

    public AddStudentServlet() {
        logger = Logger.getLogger(AddStudentServlet.class.getName());
        studentService = StudentService.getInstance();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Student newStudent = objectMapper.readValue(req.getReader(), Student.class);
            studentService.addStudent(newStudent);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}

