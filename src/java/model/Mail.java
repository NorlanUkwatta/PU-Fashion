package model;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

    private static final String APP_EMAIL = "norlanukwatta363@gmail.com";
    private static final String APP_PASSWORD = "cpmr zunj zvkm szlx";

    public static void sendMail(String email, String code) {

        String htmlContent = "";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Mail.APP_EMAIL, Mail.APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Mail.APP_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setContent(htmlContent, "text/html");

            htmlContent = "<html>"
                    + "<body style='font-family:Arial,sans-serif;'>"
                    + "<div style='max-width:600px;margin:auto;padding:20px;border:1px solid #ddd;border-radius:10px;'>"
                    + "<h2 style='color:#333;'>Welcome to SmartTrade!</h2>"
                    + "<p>Use the following verification code to continue:</p>"
                    + "<div style='font-size:24px;font-weight:bold;background:#f1f1f1;padding:10px;text-align:center;margin:20px 0;border-radius:5px;'>"
                    + code
                    + "</div>"
                    + "<p>If you didn’t request this code, please ignore this email.</p>"
                    + "<p style='font-size:12px;color:#999;'>This is an automated message. Please do not reply.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
