package com.example.miaobau.filter;

import com.example.miaobau.model.CustomerBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/secure/*")
public class CustomerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        CustomerBean customerBean = (CustomerBean) httpRequest.getSession().getAttribute("customer");

        if (customerBean == null) {
            String destination = httpRequest.getRequestURI();
            httpRequest.getSession().setAttribute("redirectAfterLogin", destination);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
