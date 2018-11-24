package org.vaadin.leif.splashscreen.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/slow.html")
public class SlowLoadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Optional.ofNullable(req.getParameter("duration"))
                .ifPresent(duration -> {
                    try {
                        Thread.sleep(Long.parseLong(duration));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                });

        req.setAttribute("Content-Type", "text/html");
        req.setAttribute("Cache-Control", "no-cache");

        try (PrintWriter writer = resp.getWriter()) {
            writer.println("<script>console.log('slow.html')</script>");
        }
    }
}
