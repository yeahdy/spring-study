package com.basic.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "basicServlet", urlPatterns = "/hello")
public class BasicServlet extends HttpServlet {

    //해당 서블릿이 호출될때 실행
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("BasicServlet.service");

        String course = request.getParameter("lecture");
        System.out.printf("Param = {%s}\n",course);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("What kind of lecture is it?\n" + course);
    }
}
