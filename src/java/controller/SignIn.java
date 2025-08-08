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

@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();

        if (email.isEmpty()) {
            responseObject.addProperty("message", "Please enter your email");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Invalid email");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Please enter your password");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "Invalid password");
        } else {
            Session session = HibernateUtil.getsessionFactory().openSession();
            Criteria c1 = session.createCriteria(User.class);
            c1.add(Restrictions.eq("email", email));
            c1.add(Restrictions.eq("password", password));

            if (c1.list().isEmpty()) {
                responseObject.addProperty("message", "Incorrect email or password");
            } else {

                responseObject.addProperty("status", true);

                User currentUser = (User) c1.list().get(0);

                HttpSession httpSession = request.getSession();

                if (!currentUser.getVerificationCode().equals("Verified")) {

                    String verificationCode = Util.generateCode();
                    currentUser.setVerificationCode(verificationCode);

                    session.update(currentUser);
                    session.beginTransaction().commit();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Mail.sendMail(email, verificationCode);
                        }

                    }).start();

                    responseObject.addProperty("firstName", currentUser.getFirstName());

                    httpSession.setAttribute("email", email);

                    responseObject.addProperty("message", "1");
                } else {
                    responseObject.addProperty("message", "2");
                    responseObject.addProperty("firstName", currentUser.getFirstName());
                    httpSession.setAttribute("user", currentUser);
                }
            }

            session.close();
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

}
