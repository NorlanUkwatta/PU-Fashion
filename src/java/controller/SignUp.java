package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Status;
import hibernate.User;
import hibernate.UserType;
import java.io.IOException;
import java.util.Date;
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

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);
        
        String firstName = user.get("firstName").getAsString();
        String lastName = user.get("lastName").getAsString();
        String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();
        String rePassword = user.get("repassword").getAsString();
        
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        if (firstName.isEmpty()) {
            responseObject.addProperty("message", "Please enter the first name");
        } else if (lastName.isEmpty()) {
            responseObject.addProperty("message", "Please enter the last name");
        } else if (email.isEmpty()) {
            responseObject.addProperty("message", "Please give your email");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Invalid email");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Please enter a password");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "Password must contain at least eight characters.(uppercase, lowercase, number, special character)");
        } else if (rePassword.isEmpty()) {
            responseObject.addProperty("message", "Please enter your password again");
        } else if (!Util.isPasswordValid(rePassword)) {
            responseObject.addProperty("message", "Invalid password. Please check");
        } else {
            
            Session session = HibernateUtil.getsessionFactory().openSession();
            
            Criteria c1 = session.createCriteria(User.class);
            c1.add(Restrictions.eq("email", email));
            
            if (!c1.list().isEmpty()) {
                responseObject.addProperty("message", "User already exist. Please sign in.");
            } else {
                User u = new User();
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setEmail(email);
                u.setPassword(password);
                
                String verificationCode = Util.generateCode();
                u.setVerificationCode(verificationCode);
                
                u.setCreatedAt(new Date());
                
                UserType userType = (UserType) session.get(UserType.class, 1);
                
                u.setUserType(userType);
                
                Status status = (Status) session.get(Status.class, 1);
                
                u.setStatus(status);
                
                session.save(u);
                session.beginTransaction().commit();
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, verificationCode);
                    }
                }).start();
                
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("email", email);
                
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Registration success! check your email for verification");
                
            }
            
            session.close();
            
        }
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }
    
}
