package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

// Controller del carrello: gestisce tutte le operazioni (aggiungi/incrementa/decrementa/rimuovi/svuota).
@WebServlet("/cart")
public class CartController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Recupero il carrello dalla sessione.
        CartBean cart = (CartBean) session.getAttribute("cart");

        // Prima azione dell'utente: il carrello non esiste ancora -> lo creo e lo metto in sessione.
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }

        // "action" dice quale operazione fare: add / increase / decrease / remove / clear.
        String action = request.getParameter("action");

        if ("clear".equals(action)) {
            // "costante".equals(param): idioma anti-NPE (regge anche action == null).
            // Svuota tutto: non serve il productId.
            cart.clearCart();
        } else if (action != null) {
            // parseIntOrNull: conversione sicura, torna null se il parametro manca o non è un numero.
            Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));
            if (productID != null) {
                switch (action) {
                    case "add":
                    case "increase":
                        // add e increase condividono la stessa logica (aggiungono quantità).
                        Integer quantity = ParseUtil.parseIntOrNull(request.getParameter("quantity"));
                        // Se la quantità non è specificata (es. il "+" nel carrello non la manda), default 1.
                        add(cart, productID, quantity != null ? quantity : 1);
                        break;
                    case "remove":
                        remove(cart, productID);
                        break;
                    case "decrease":
                        decrease(cart, productID);
                        break;
                }
            }
        }

        // Dopo l'azione: riporto l'utente alla pagina di provenienza.
        // Referer = URL della pagina da cui è partita la richiesta (lo manda il browser).
        String back = request.getHeader("Referer");
        // Fallback: il Referer NON è garantito (privacy, richiesta a mano...). Se manca, vado al catalogo.
        if (back == null) {
            back = request.getContextPath() + "/catalog";
        }
        // REDIRECT (non forward) = pattern Post-Redirect-Get: così un F5 non ripete l'operazione sul carrello.
        response.sendRedirect(back);
    }

    // doGet = mostra la pagina del carrello (sola lettura, nessuna modifica).
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward alla JSP: stessa richiesta, è una vista, non ho modificato nulla.
        request.getRequestDispatcher("/view/Cart.jsp").forward(request, response);
    }

    // --- Metodi privati helper:
    // Aggiunge un prodotto al carrello (o ne aumenta la quantità).
    private void add(CartBean cart, int productId, int quantity) throws ServletException {
        try {
            // Rileggo il prodotto dal DB dall'id: mi servono nome/prezzo/immagine aggiornati
            // doRetrieveById filtra is_deleted=false -> un prodotto cancellato non è aggiungibile manipolando l'id.
            ProductBean product = new ProductDAO().doRetrieveById(productId);
            if (product == null) {
                // Prodotto inesistente o cancellato: esco silenziosamente, non aggiungo nulla.
                return;
            }
            if(quantity < 1){
                // Quantità non valida forzata a 1 (l'utente potrebbe manomettere il form).
                quantity = 1;
            }
            cart.addToCart(product, quantity);
        } catch (SQLException e) {
            // Eccezione DB "impacchettata" in ServletException -> gestita dalla error page 500.
            throw new ServletException(e);
        }
    }

    // Rimuove del tutto un prodotto dal carrello.
    private void remove(CartBean cart, int productId) {
        cart.removeFromCart(productId);
    }

    // Diminuisce di 1 la quantità di un prodotto.
    private void decrease(CartBean cart, int productId) {
        cart.decreaseQuantity(productId);
    }

}