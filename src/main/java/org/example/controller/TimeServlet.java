package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(value = { "/time", "/timezone", "/response"})
public class TimeServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(getServletContext().getRealPath("/WEB-INF/templates/"));
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        templateEngine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logCookies(req);

        HttpSession session = req.getSession(false);
        String timeZone = req.getParameter("timezone");

        if (timeZone != null && !timeZone.isEmpty()) {
            timeZone = timeZone.trim().replace(" ", "+");
            try {
                ZoneId.of(timeZone);
                resp.addCookie(new Cookie("lastTimezone", timeZone));
                if (session == null) {
                    session = req.getSession(true);
                }
                session.setAttribute("selectedTimeZone", timeZone);
            } catch (DateTimeException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid timezone");
                return;
            }
        } else {
            timeZone = (String) session.getAttribute("selectedTimeZone");
            if (timeZone == null || timeZone.isEmpty()) {
                timeZone = getLastTimezoneFromCookies(req);
            }
        }
        if (timeZone == null || timeZone.isEmpty()) {
            timeZone = "UTC";
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone));
        String formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

        List<String> timeZones = new ArrayList<>();
        for (int i = -12; i <= 12; i++) {
            if (i < 0) {
                timeZones.add("UTC" + i);
            } else if (i == 0) {
                timeZones.add("UTC");
            } else {
                timeZones.add("UTC+" + i);
            }

        }

        Context context = new Context(req.getLocale());
        context.setVariable("timeZone", timeZone);
        context.setVariable("formattedDateTime", formattedDateTime);
        context.setVariable("timeZones", timeZones);

        try (PrintWriter writer = resp.getWriter()) {
            templateEngine.process("index", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the template.");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logCookies(req);

        String timeZone = req.getParameter("timeZone");
        if (timeZone == null || timeZone.isEmpty()) {
            timeZone = getLastTimezoneFromCookies(req);
        }
        HttpSession session = req.getSession();
        session.setAttribute("selectedTimeZone", timeZone);

        resp.addCookie(new Cookie("lastTimezone", timeZone));
        System.out.println(timeZone);
        resp.sendRedirect(req.getContextPath() + "/time");
    }

    private String getLastTimezoneFromCookies(HttpServletRequest req) {
        String timeZone = "UTC";
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("lastTimezone".equals(cookie.getName())) {
                    timeZone = cookie.getValue();
                    break;
                }
            }
        }
        return timeZone;
    }

    private void logCookies(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("No cookies found.");
        }
    }
}