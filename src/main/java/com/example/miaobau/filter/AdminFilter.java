package com.example.miaobau.filter;

import com.example.miaobau.model.AdminBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * Filtro di AUTENTICAZIONE per l'area amministrativa.
 * Intercetta TUTTE le richieste verso /admin/* prima che
 * raggiungano le servlet: se chi richiede non è un admin loggato, lo reindirizza
 * al login.
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Il filtro riceve i tipi generici Servlet(Request/Response), per usare i
        // metodi HTTP (sessione, path, redirect) serve il cast alle versioni Http.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        // Recupera l'admin dalla sessione: null se nessuno ha fatto il login admin.
        AdminBean adminBean = (AdminBean) session.getAttribute("admin");

        // Lascia passare in due casi:
        //  1) la richiesta è la pagina di login stessa (/admin/login) — altrimenti
        //     il filtro bloccherebbe anche l'accesso al login, creando un vicolo cieco;
        //  2) c'è un admin in sessione (già autenticato).
        if(httpRequest.getServletPath().equals("/admin/login") || adminBean != null){
            chain.doFilter(request, response);   // prosegue verso la servlet richiesta
        }
        else{
            // Non autenticato e non sta andando al login: lo mando al login admin.
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + "/admin/login");
        }
    }
}