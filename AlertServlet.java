package com.myapp.webappfirewall.resources;

import java.io.IOException;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


public class AlertServlet extends HttpServlet {
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String method = request.getParameter("method");
        String uri = request.getParameter("uri");
        String query = request.getParameter("query");
        String ip = request.getParameter("ip");

        String recipient = "manthanrahul88@gmail.com";  // 📍 Change to your alert receiver
        String subject = "🚨 WebAppFirewall Alert - Suspicious Request Raised";
        String body = String.format("Suspicious request details:\n\nMethod: %s\nURI: %s\nQuery: %s\nIP Address: %s",
                method, uri, query, ip);

        // SMTP config (use your SMTP provider details)
        String smtpHost = "smtp.gmail.com";
        String smtpPort = "587";
        final String smtpUser = "manthanrahul85@gmail.com";      // 📍 Change this
        final String smtpPass = "";   // 📍 Change this

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session mailSession = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPass);
            }
        });

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(smtpUser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            response.getWriter().println("✅ Alert raised and email sent successfully.");
             response.sendRedirect("mail_success.jsp");
        } catch (MessagingException e) {
            e.printStackTrace();
            response.getWriter().println("❌ Failed to send alert email: " + e.getMessage());
             response.sendRedirect("mail_failure.jsp");
        }
    }
}
