package org.example.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        String timeZone = req.getParameter("timezone");
        System.out.println(timeZone);
        if (timeZone != null && !timeZone.isEmpty()) {
            try {
                ZoneId.of(timeZone);
            } catch (DateTimeException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("Invalid timezone");
                return;
            }
        }
        try {
            chain.doFilter(req, res);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
