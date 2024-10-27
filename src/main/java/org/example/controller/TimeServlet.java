package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@WebServlet(value = {"/time", "/timezone", "/response"})
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timeZone = req.getParameter("timezone");
        if (timeZone == null || timeZone.isEmpty()) {
            timeZone = "UTC";
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone));
        String formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        req.setAttribute("timeZone", timeZone);
        req.setAttribute("formattedDateTime", formattedDateTime);

        req.getRequestDispatcher("/time.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timeZone = req.getParameter("timeZone");
        if (timeZone == null || timeZone.isEmpty()) {
            timeZone = "UTC";
        }
        System.out.println(timeZone);
        resp.sendRedirect("/time?timezone=" + timeZone);
    }
}