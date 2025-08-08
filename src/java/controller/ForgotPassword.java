package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword"})
public class ForgotPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();

        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);
        String email = user.get("email").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (email.isEmpty()) {
            responseObject.addProperty("message", "Please enter your email");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Invalid email");
        } else {
            Session session = HibernateUtil.getsessionFactory().openSession();
            Criteria c1 = session.createCriteria(User.class);
            c1.add(Restrictions.eq("email", email));

            if (c1.list().isEmpty()) {
                responseObject.addProperty("message", "This email not registered");
            } else {
                User currentUser = (User) c1.list().get(0);
                String generateCode = Util.generateCode();
                currentUser.setVerificationCode(generateCode);

                session.update(currentUser);
                session.beginTransaction().commit();
                session.close();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, generateCode);
                    }
                }).start();

                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("email", email);

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Verification code sent. Please check your email.");
            }

        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

}
