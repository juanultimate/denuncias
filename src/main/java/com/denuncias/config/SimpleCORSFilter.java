package com.denuncias.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("======================================================================================================");
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        System.out.println("********************************************************************************");
        System.out.println(request.getHeader("Origin"));
        System.out.println(request.getHeader("Cookie"));
        System.out.println("********************************************************************************");


        //if(!request.getMethod().equals(HttpMethod.OPTIONS)){
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //}
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(req, res);
    }
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println(" INICIO========================================================");
    }

    public void destroy() {}

}
