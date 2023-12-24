package org.ru.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ru.Student;
import org.ru.StudentService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/getStudents"})
public class GetStudentsServlet extends HttpServlet {

    private final StudentService studentService;

    public GetStudentsServlet() {
        studentService = StudentService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Student> students = studentService.getAllStudents();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStudents = objectMapper.writeValueAsString(students);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonStudents);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
