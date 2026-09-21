package com.example.miaobau.control.admin;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.AddressBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Controller del dettaglio ordine lato ADMIN. Sotto /admin/*, protetto dal filtro.
 * Mostra un ordine con i dati del cliente (chi ha ordinato) e l'indirizzo di spedizione.
 */
@WebServlet("/admin/order-detail")
public class AdminOrderDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer orderID = ParseUtil.parseIntOrNull(request.getParameter("orderId"));
        OrdersDAO ordersDAO = new OrdersDAO();
        AddressBean address;

        // Id mancante o non valido: torna all'elenco ordini admin (guard clause).
        if (orderID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try {
            // Recupera l'ordine con i dati del cliente (join): l'admin vuole sapere
            // chi ha ordinato. I dati cliente sono attuali (non congelati), presi
            // in tempo reale dalla tabella customer.
            OrdersBean order = ordersDAO.doRetrieveByIdWithCustomer(orderID);
            // Ordine inesistente: torna all'elenco. Qui non serve il controllo di
            // proprietà (l'admin può vedere ogni ordine), basta che esista.
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }
            // Righe dell'ordine (prodotti con valori congelati).
            order.setItems(ordersDAO.doRetrieveItemsByOrder(orderID));
            // Indirizzo di spedizione: doRetriveByID NON filtra is_deleted, così si
            // mostra anche se il cliente l'ha poi cancellato (dato storico dell'ordine).
            address = new AddressDAO().doRetriveByID(order.getAddressID());
            request.setAttribute("order", order);
            request.setAttribute("address", address);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/OrderDetail.jsp");
        dispatcher.forward(request, response);
    }
}