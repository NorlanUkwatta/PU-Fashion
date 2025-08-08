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
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ResetPassword", urlPatterns = {"/ResetPassword"})
public class ResetPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String verificationCode = user.get("verificationCode").getAsString();
        String fnp = user.get("fnp").getAsString();
        String frp = user.get("frp").getAsString();

        if (verificationCode.isEmpty()) {
            responseObject.addProperty("message", "Please enter the verification code.");
        } else if (fnp.isEmpty()) {
            responseObject.addProperty("message", "Please enter new password");
        } else if (!Util.isPasswordValid(fnp)) {
            responseObject.addProperty("message", "Password must contain at least eight characters.(uppercase, lowercase, number, special character");
        } else if (frp.isEmpty()) {
            responseObject.addProperty("message", "Please re-enter the new password");
        } else if (!frp.equals(fnp)) {
            responseObject.addProperty("message", "Password mismatch");
        } else {

            HttpSession httpSession = request.getSession();

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
                User currentUser = (User) criteria.list().get(0); //get(0) or can direct give the index
                if (currentUser.getPassword().equals(frp)) {
                    responseObject.addProperty("message", "Password already used. Give a new password.");
                } else {
                    currentUser.setVerificationCode("Verified");
                    currentUser.setPassword(frp);

                    session.update(currentUser);
                    session.beginTransaction().commit();
                    session.close();

                    httpSession.setAttribute("user", currentUser);

                    responseObject.addProperty("firstName", currentUser.getFirstName());

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Password reset successfuly!");
                }
            }

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);
    }

}
