package com.example.miaobau.filter;

import com.example.miaobau.model.AdminBean;
import com.example.miaobau.model.CustomerBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * Filtro di AUTORIZZAZIONE per l'area amministrativa.
 * Intercetta TUTTE le richieste verso /admin/* prima che raggiungano le servlet
 * e decide chi può passare:
 *   - admin loggato        -> passa ovunque;
 *   - cliente loggato       -> 403 Accesso negato (autenticato ma non autorizzato);
 *   - utente anonimo        -> redirect al login admin (tranne la pagina di login stessa).
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Cast alle versioni HTTP per accedere a sessione, path e redirect/errori.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        // Chi c'è in sessione: admin e/o cliente (uno, entrambi o nessuno).
        AdminBean adminBean = (AdminBean) session.getAttribute("admin");
        CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");

        // 1) Admin autenticato: può accedere a tutta l'area /admin/* (login compreso).
        if (adminBean != null) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Cliente loggato ma NON admin: è autenticato ma non autorizzato -> 403.
        //    Blocco diretto su qualsiasi pagina /admin/*, senza passare dal login.
        if (customerBean != null) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // 3) Utente anonimo: lascio passare solo la pagina di login (per non creare
        //    un vicolo cieco), altrimenti lo reindirizzo lì.
        if (httpRequest.getServletPath().equals("/admin/login")) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/login");
        }
    }
}