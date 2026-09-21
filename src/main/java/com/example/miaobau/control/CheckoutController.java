package com.example.miaobau.control;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.AddressBean;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Controller del checkout. Due fasi distinte:
 *  - doGet: mostra il RIEPILOGO dell'ordine (prodotti + indirizzi tra cui scegliere).
 *  - doPost: CONFERMA l'ordine, determinando l'indirizzo di spedizione e salvando.
 */
@WebServlet("/secure/checkout")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
        CartBean cart = (CartBean) session.getAttribute("cart");
        // addressChoice è il valore del gruppo di radio: un id di indirizzo esistente,
        // "new" per un nuovo indirizzo, o null se non è stato selezionato nulla.
        String addressChoice = request.getParameter("addressChoice");
        Integer addressID;              // id dell'indirizzo da usare per l'ordine (dai vari rami)
        AddressBean addressBean;
        AddressDAO addressDAO = new AddressDAO();
        List<String> errors = new ArrayList<>();

        // Non si ordina un carrello vuoto: torna al carrello.
        if (cart == null || cart.getCart().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // --- Determinazione dell'indirizzo: tre casi in base a addressChoice ---

        // CASO 1: nessuna scelta -> errore, torna al riepilogo (ripassando gli indirizzi).
        if (addressChoice == null) {
            errors.add("Aggiungi o seleziona un indirizzo di spedizione");
            showSummaryWithErrors(request, response, customerBean.getCustomerID(), errors);
            return;
        }
        // CASO 2: nuovo indirizzo -> valida i 5 campi, salvalo, usa l'id generato.
        else if (addressChoice.equals("new")) {
            String street = request.getParameter("street");
            String civicNumber = request.getParameter("civicNumber");
            String postalCode = request.getParameter("postalCode");
            String city = request.getParameter("city");
            String country = request.getParameter("country");

            if (street == null || street.isBlank()) {
                errors.add("Inserire una via valida");
            }

            if (civicNumber == null || civicNumber.isBlank()) {
                errors.add("Inserire un numero civico valido");
            }

            if (postalCode == null || postalCode.isBlank() || !postalCode.matches("\\d{5}")) {
                errors.add("Inserire un CAP valido");
            }

            if (city == null || city.isBlank()) {
                errors.add("Inserire una città valida");
            }

            if (country == null || country.isBlank()) {
                errors.add("Inserire una paese valido");
            }

            // Se i campi non sono validi, torna al riepilogo con gli errori (e con gli
            // indirizzi ripassati, che la JSP usa per i radio). Un solo punto di uscita.
            if (!errors.isEmpty()) {
                showSummaryWithErrors(request, response, customerBean.getCustomerID(), errors);
                return;
            }

            // Costruisce e salva il nuovo indirizzo; customerID dalla SESSIONE (sicurezza).
            addressBean = new AddressBean();
            addressBean.setCity(city);
            addressBean.setPostalCode(postalCode);
            addressBean.setStreet(street);
            addressBean.setCivicNumber(civicNumber);
            addressBean.setCountry(country);
            addressBean.setCustomerID(customerBean.getCustomerID());
            try {
                // doSave restituisce l'id generato: serve per collegarlo all'ordine.
                addressID = addressDAO.doSave(addressBean);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        // CASO 3: indirizzo esistente -> l'addressChoice è il suo id.
        else {
            addressID = ParseUtil.parseIntOrNull(addressChoice);
            // Id non numerico (manomissione): torna al riepilogo pulito (via doGet).
            if (addressID == null) {
                response.sendRedirect(request.getContextPath() + "/secure/checkout");
                return;
            }

            try {
                addressBean = addressDAO.doRetriveByID(addressID);
            } catch (SQLException e) {
                throw new ServletException(e);
            }

            // L'indirizzo deve esistere ed essere del cliente loggato,
            // altrimenti (indirizzo altrui o inesistente) si annulla tornando al riepilogo.
            if (addressBean == null || addressBean.getCustomerID() != customerBean.getCustomerID()) {
                response.sendRedirect(request.getContextPath() + "/secure/checkout");
                return;
            }
        }

        // --- A questo punto addressID è valido e legittimo: creo e salvo l'ordine ---
        OrdersBean ordersBean = new OrdersBean();
        ordersBean.setAddressID(addressID);
        ordersBean.setTotalPrice(cart.getTotal());
        ordersBean.setOrderDate(LocalDateTime.now());
        ordersBean.setCustomerID(customerBean.getCustomerID());

        try {
            // doSave è transazionale (testata + righe insieme). Restituisce l'order_id.
            int orderID = new OrdersDAO().doSave(ordersBean, cart);
            cart.clearCart();   // ordine salvato: svuoto il carrello
            // Redirect alla conferma col numero d'ordine (POST-Redirect-GET: evita il
            // doppio invio dell'ordine se l'utente ricarica).
            response.sendRedirect(request.getContextPath() + "/secure/confirmation?orderId=" + orderID);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /*
     * Mostra il riepilogo. Recupera carrello e indirizzi del cliente
     * e fa forward alla JSP. Carrello vuoto -> torna al carrello.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CartBean cartBean = (CartBean) request.getSession().getAttribute("cart");
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");

        if (cartBean == null || cartBean.getCart().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        try {
            // Indirizzi del cliente per popolare i radio del riepilogo.
            List<AddressBean> addressesBean = new AddressDAO().doRetrieveByCustomer(customerBean.getCustomerID());
            request.setAttribute("addresses", addressesBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/OrderSummary.jsp");
        dispatcher.forward(request, response);
    }

    /*
     * Helper privato: torna al riepilogo mostrando gli errori. Raccoglie i
     * tre passi ripetuti — recupera gli indirizzi (che la JSP usa per i radio, e
     * che vanno ri-recuperati perché il doPost è una richiesta nuova senza gli
     * attributi del doGet), imposta gli errori, forward.
     */
    private void showSummaryWithErrors(HttpServletRequest request, HttpServletResponse response, int customerID, List<String> errors)
            throws ServletException, IOException {
        try {
            List<AddressBean> addresses = new AddressDAO().doRetrieveByCustomer(customerID);
            request.setAttribute("addresses", addresses);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/view/OrderSummary.jsp").forward(request, response);
    }
}