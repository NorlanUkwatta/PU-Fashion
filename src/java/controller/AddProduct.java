package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.Size;
import hibernate.Status;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String qty = request.getParameter("qty");
        String categoryId = request.getParameter("category");
        String colorId = request.getParameter("color");
        String statusId = request.getParameter("status");
        String sizeId = request.getParameter("size");

        Part img1 = request.getPart("img1");
        Part img2 = request.getPart("img2");
        Part img3 = request.getPart("img3");
        Part img4 = request.getPart("img4");

        if (request.getSession().getAttribute("user") == null) {
            responseObject.addProperty("message", "Session expired or user not found. Please sign in");
            responseObject.addProperty("status", false);
        } else if (title.isEmpty()) {
            responseObject.addProperty("message", "Please give a title");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Please give a description");
        } else if (price.isEmpty()) {
            responseObject.addProperty("message", "Please give a price");
        } else if (!Util.isDouble(price)) {
            responseObject.addProperty("message", "Invalid price");
        } else if (Double.parseDouble(price) < 0) {
            responseObject.addProperty("message", "Invalid price");
        } else if (qty.isEmpty()) {
            responseObject.addProperty("message", "Please enter quantity");
        } else if (!Util.isInteger(qty)) {
            responseObject.addProperty("message", "Invalid qty");
        } else if (Integer.parseInt(qty) < 0) {
            responseObject.addProperty("message", "Invalid qty");
        } else if (Integer.parseInt(categoryId) == 0) {
            responseObject.addProperty("message", "Please select category");
        } else if (!Util.isInteger(categoryId)) {
            responseObject.addProperty("message", "Invalid category");
        } else if (Integer.parseInt(colorId) == 0) {
            responseObject.addProperty("message", "Please select color");
        } else if (!Util.isInteger(colorId)) {
            responseObject.addProperty("message", "Invalid color");
        } else if (Integer.parseInt(statusId) == 0) {
            responseObject.addProperty("message", "Please select product status");
        } else if (!Util.isInteger(statusId)) {
            responseObject.addProperty("message", "Invalid status");
        } else if (Integer.parseInt(sizeId) == 0) {
            responseObject.addProperty("message", "Please select size");
        } else if (!Util.isInteger(sizeId)) {
            responseObject.addProperty("message", "Invalid size");
        } else if (img1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please upload the image 1");
        } else if (img2.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please upload the image 2");
        } else if (img3.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please upload the image 3");
        } else if (img4.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Please upload the image 4");
        } else {
            Session session = HibernateUtil.getsessionFactory().openSession();

            Category category = (Category) session.get(Category.class, Integer.valueOf(categoryId));

            if (category == null) {
                responseObject.addProperty("message", "Please select valid category");
            } else {
                Color color = (Color) session.get(Color.class, Integer.valueOf(colorId));
                if (color == null) {
                    responseObject.addProperty("message", "Please select valid color");
                } else {
                    Status status = (Status) session.get(Status.class, Integer.valueOf(statusId));
                    if (status == null) {
                        responseObject.addProperty("message", "Please select valid status");
                    } else {
                        Size size = (Size) session.get(Size.class, Integer.valueOf(sizeId));
                        if (size == null) {
                            responseObject.addProperty("message", "Please select valid size");
                        } else {

                            Product product = new Product();
                            product.setTitle(title);
                            product.setDescription(description);
                            product.setPrice(Double.parseDouble(price));
                            product.setQty(Integer.parseInt(qty));
                            product.setCreated_at(new Date());
                            product.setStatus(status);
                            product.setCategory(category);
                            product.setColor(color);
                            product.setSize(size);

                            User user = (User) request.getSession().getAttribute("user");
                            Criteria c1 = session.createCriteria(User.class);
                            c1.add(Restrictions.eq("email", user.getEmail()));
                            product.setUser((User) c1.uniqueResult());

                            Integer id = (Integer) session.save(product);
                            session.beginTransaction().commit();
                            session.close();

                            String appPath = getServletContext().getRealPath("");
                            String newAppPath = appPath.replace("build" + File.separator + "web", "web" + File.separator + "product-images");
                            File productFolder = new File(newAppPath, String.valueOf(id));
                            
                            if (!productFolder.exists()) {
                                productFolder.mkdirs();
                            }

                            File file1 = new File(productFolder, "image1.png");
                            Files.copy(img1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            File file2 = new File(productFolder, "image2.png");
                            Files.copy(img2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            File file3 = new File(productFolder, "image3.png");
                            Files.copy(img3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            File file4 = new File(productFolder, "image4.png");
                            Files.copy(img4.getInputStream(), file4.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            responseObject.addProperty("status", true);
                            responseObject.addProperty("message", "Product successfuly added");

                        }
                    }
                }
            }

        }

        Gson gson = new Gson();
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }
}
