package com.example.miaobau.control.admin;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/orders")
public class AdminOrdersController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String dateFromStr = request.getParameter("dateFrom");
        String dateToStr = request.getParameter("dateTo");

        if (email == null || email.isBlank()) {
            email = null;
        }

        LocalDateTime dateFrom = ParseUtil.parseDateFromOrNull(dateFromStr);
        LocalDateTime dateTo = ParseUtil.parseDateToOrNull(dateToStr);

        // Controllo intervallo: se entrambe presenti e "from" successiva ad "to"
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            request.setAttribute("filterError", "L'intervallo di date non è valido: la data iniziale è successiva a quella finale.");
            request.getRequestDispatcher("/view/admin/Orders.jsp").forward(request, response);
            return;
        }

        try {
            List<OrdersBean> orders = new OrdersDAO().doRetrieveFiltered(email, dateFrom, dateTo);
            request.setAttribute("orders", orders);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.getRequestDispatcher("/view/admin/Orders.jsp").forward(request, response);
    }
}
