package com.example.miaobau.filter;

import com.example.miaobau.model.AdminBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        AdminBean adminBean = (AdminBean) session.getAttribute("admin");

        if(httpRequest.getServletPath().equals("/admin/login") || adminBean != null){
            chain.doFilter(request, response);
        }
        else{
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/admin/login");
        }
    }
}
