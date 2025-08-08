package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.Size;
import hibernate.Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;

@WebServlet(name = "LoadProductData", urlPatterns = {"/LoadProductData"})
public class LoadProductData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getsessionFactory().openSession();

        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        Criteria c1 = session.createCriteria(Status.class);
        List<Status> statusList = c1.list();

        Criteria c2 = session.createCriteria(Category.class);
        List<Category> categoryList = c2.list();

        Criteria c3 = session.createCriteria(Color.class);
        List<Color> colorList = c3.list();

        Criteria c4 = session.createCriteria(Size.class);
        List<Size> sizeList = c4.list();

        Criteria c5 = session.createCriteria(Product.class);
        List<Product> productList = c5.list();

        session.close();

        Gson gson = new Gson();
        resposeObject.add("statusList", gson.toJsonTree(statusList));
        resposeObject.add("categoryList", gson.toJsonTree(categoryList));
        resposeObject.add("colorList", gson.toJsonTree(colorList));
        resposeObject.add("sizeList", gson.toJsonTree(sizeList));
        resposeObject.add("productList", gson.toJsonTree(productList));

        resposeObject.addProperty("status", true);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(resposeObject));
    }

}
