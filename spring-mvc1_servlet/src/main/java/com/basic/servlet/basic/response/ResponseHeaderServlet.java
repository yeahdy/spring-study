package com.basic.servlet.basic.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    /*
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Type","text/plain;charset=utf-8");
        response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");   //캐시 무효화
        response.setHeader("Pragma","no-cache");   //캐시 히스토리 삭제
        response.setHeader("Ex-token","a1b2c3d4e5");   //커스텀 데이터
        response.getWriter().println("SUCCESS");
    }
     */
    //NOTE: service()메소드는 POST, GET 모두 처리하고, doGet, doPost 보다 우선순위가 높음

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        setContent(response);
        response.getWriter().println("Not authorized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_CREATED);
        setContent(response);
        cookie(response);
        response.getWriter().println("Create Cookie");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirect(resp);
        resp.getWriter().println("REDIRECT");
    }

    private void setContent(HttpServletResponse response){
        response.setHeader("Content-Type","text/plain;charset=utf-8");
        response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
        response.setHeader("Pragma","no-cache");
    }

    private void cookie(HttpServletResponse response){
        Cookie cookie = new Cookie("chocolate-cookies", "great!");
        cookie.setMaxAge(600); //600s
        response.addCookie(cookie);
    }

    private void redirect(HttpServletResponse response) throws IOException{
        response.sendRedirect("/basic/hello-form.html");
        //공식문서: 302 상태 코드와 함께 지정된 리디렉션 위치 URL을 사용하여 클라이언트로 리디렉션 응답을 보낸다.
    }


}
