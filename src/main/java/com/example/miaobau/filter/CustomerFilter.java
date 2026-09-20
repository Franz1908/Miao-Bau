package com.example.miaobau.filter;

import com.example.miaobau.model.CustomerBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Filtro di AUTENTICAZIONE per l'area cliente riservata.
 * Protegge tutte le pagine sotto /secure/* (checkout, ordini, account, indirizzi):
 * se il cliente non è loggato, lo reindirizza al login, MEMORIZZANDO prima la
 * pagina che voleva raggiungere, così dopo il login potrà esserci riportato.
 */
@WebFilter("/secure/*")
public class CustomerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Cast alle versioni HTTP per accedere a sessione, URI e redirect.
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Recupera il cliente dalla sessione: null se non ha fatto il login.
        CustomerBean customerBean = (CustomerBean) httpRequest.getSession().getAttribute("customer");

        if (customerBean == null) {
            // Non loggato: salva in sessione la pagina richiesta, così dopo il login
            // il controller potrà rimandarcelo (es. tornare al checkout che voleva fare).
            String destination = httpRequest.getRequestURI();
            httpRequest.getSession().setAttribute("redirectAfterLogin", destination);

            // Reindirizza al login e ferma la catena: la pagina protetta non viene servita.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Cliente autenticato: la richiesta prosegue verso la servlet/pagina richiesta.
        chain.doFilter(request, response);
    }
}