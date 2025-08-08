package controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String qty = request.getParameter("qty");
        String category = request.getParameter("category");
        String color = request.getParameter("color");
        String status = request.getParameter("status");
        String size = request.getParameter("size");

        Part img1 = request.getPart("img1");
        Part img2 = request.getPart("img2");
        Part img3 = request.getPart("img3");
        Part img4 = request.getPart("img4");

    }

}
