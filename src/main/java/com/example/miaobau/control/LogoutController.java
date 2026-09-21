package com.example.miaobau.control;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
 * Controller del logout cliente.
 * Distrugge la sessione (quindi disconnette l'utente) e riporta all'account.
 */
@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // getSession(false): restituisce la sessione esistente, o null se non c'è.
        // Il 'false' non crea una nuova sessione se non esiste (a
        // differenza di getSession() che ne creerebbe una)
        HttpSession session = request.getSession(false);
        if (session != null) {
            // invalidate: distrugge la sessione e tutto il suo contenuto (customer,
            // carrello, ecc.). Al prossimo accesso l'utente sarà "non loggato".
            session.invalidate();
        }
        // Redirect all'account: essendo "a doppio stato", ora mostrerà la versione
        // "accedi/registrati" (l'utente non è più loggato).
        response.sendRedirect(request.getContextPath() + "/account");
    }
}