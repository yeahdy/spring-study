package com.basic.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printHeaders(request);
        printStartLine(request);
    }


    private void printStartLine(HttpServletRequest request) {
        System.out.println("--- REQUEST START ---");
        System.out.println("request.getMethod() = " + request.getMethod()); //GET
        System.out.println("request.getProtocol() = " + request.getProtocol()); //HTTP/1.1
        System.out.println("request.getScheme() = " + request.getScheme()); //http
        System.out.println("request.getRequestURL() = " + request.getRequestURL()); // http://localhost:8080/request-header
        System.out.println("request.getRequestURI() = " + request.getRequestURI()); // /request-header

        System.out.println("request.getQueryString() = " + request.getQueryString()); //username=hi
        System.out.println("request.isSecure() = " + request.isSecure()); //https 사용유무
        System.out.println("--- REQUEST END ---\n");
    }

    private void printHeaders(HttpServletRequest request){
        System.out.println("--- Headers START ---");
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> System.out.println(headerName+": "+headerName));
        System.out.println();

        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " + request.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " + request.getServerPort()); //Host 헤더
        System.out.println();

        System.out.println("[Accept-Language 편의 조회]");
        request.getLocales().asIterator()
                .forEachRemaining(locale -> System.out.println("locale = " + locale));
        System.out.println("request.getLocale() = " + request.getLocale());
        System.out.println();

        if (request.getCookies() != null) {
            System.out.println("[cookie 편의 조회]");
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        System.out.println();

        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("request.getContentLength() = " + request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());

        System.out.println("--- Headers END ---\n");
    }

}
