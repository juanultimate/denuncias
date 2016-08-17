package com.denuncias.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {
        //response.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("REQUEST");
//        while(request.getHeaderNames().hasMoreElements()){
//            String value= request.getHeaderNames().nextElement();
//            System.out.println(request.getHeader(value));
//        }
        System.out.println("COOKIES");
        System.out.println(request.getCookies());


        System.out.println(request.getCookies());
        System.out.println("RESPONSE");
        for(String value : response.getHeaderNames()){
            System.out.println(response.getHeader(value));
        }


        response.setStatus(HttpServletResponse.SC_OK);
    }
}
