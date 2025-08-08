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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AccountVerify", urlPatterns = {"/AccountVerify"})
public class AccountVerify extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject verification = gson.fromJson(request.getReader(), JsonObject.class);
        String verificationCode = verification.get("verificationCode").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        HttpSession httpSession = request.getSession();

        if (verificationCode.isEmpty()) {
            responseObject.addProperty("message", "Please enter the verification code.");
        } else if (httpSession.getAttribute("email") == null) {
            responseObject.addProperty("message", "Email not found!");
        } else {

            String email = httpSession.getAttribute("email").toString();

            Session session = HibernateUtil.getsessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);

            Criterion crt1 = Restrictions.eq("email", email);
            criteria.add(crt1);
            Criterion crt2 = Restrictions.eq("verificationCode", verificationCode);
            criteria.add(crt2);

            if (criteria.list().isEmpty()) {
                responseObject.addProperty("message", "Invalid verification code!");
            } else {
                User user = (User) criteria.list().get(0); //get(0) or can direct give the index
                user.setVerificationCode("Verified");

                session.update(user);
                session.beginTransaction().commit();
                session.close();

                httpSession.setAttribute("user", user);

                responseObject.addProperty("firstName", user.getFirstName());

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "verificaiton Success!");
            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
