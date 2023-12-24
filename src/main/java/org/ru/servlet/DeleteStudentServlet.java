package org.ru.servlet;

import org.ru.StudentService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/deleteStudent"})
public class DeleteStudentServlet extends HttpServlet {
    private final StudentService studentService;

    public DeleteStudentServlet() {
        studentService = StudentService.getInstance();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String uniqueNumber = req.getParameter("uniqueNumber");
        studentService.deleteStudent(uniqueNumber);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
