package org.ru;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/proxy")
public class ProxyServlet extends HttpServlet {

    private final Logger logger = Logger.getLogger(ProxyServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String targetUrl = request.getParameter("url");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String status = request.getParameter("status");
        if (targetUrl != null) {
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                try {
                    String queryParams = "";
                    if (from != null) {
                        queryParams += "?from=" + URLEncoder.encode(from, StandardCharsets.UTF_8.toString());
                    }
                    if (to != null) {
                        queryParams += "&to=" + URLEncoder.encode(to, StandardCharsets.UTF_8.toString());
                    }
                    if (status != null) {
                        queryParams += "&status=" + URLEncoder.encode(status, StandardCharsets.UTF_8.toString());
                    }

                    URL url = new URL(targetUrl + queryParams);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    Enumeration<String> headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        String headerValue = request.getHeader(headerName);
                        connection.setRequestProperty(headerName, headerValue);
                    }

                    int responseCode = connection.getResponseCode();
                    response.setStatus(responseCode);
                    for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                        for (String headerValue : header.getValue()) {
                            response.setHeader(header.getKey(), headerValue);
                        }
                    }

                    try (InputStream in = connection.getInputStream();
                         ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) != -1) {
                            result.write(buffer, 0, length);
                        }
                        String jsonResponse = result.toString(StandardCharsets.UTF_8.name());
                        ObjectMapper objectMapper = new ObjectMapper();
                        Task[] tasks = objectMapper.readValue(jsonResponse, Task[].class);

                        String jsonString = objectMapper.writeValueAsString(Arrays.asList(tasks));

                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentLength(jsonString.length());
                        response.getWriter().write(jsonString);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                    response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error connecting to the target server");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'url' parameter");
        }
    }
}