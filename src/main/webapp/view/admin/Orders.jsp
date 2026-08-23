<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%
    List<OrdersBean> orders = (List<OrdersBean>) request.getAttribute("orders");
    String filterError = (String) request.getAttribute("filterError");
    String emailValue = request.getParameter("email") != null ? request.getParameter("email") : "";
    String dateFromValue = request.getParameter("dateFrom") != null ? request.getParameter("dateFrom") : "";
    String dateToValue = request.getParameter("dateTo") != null ? request.getParameter("dateTo") : "";
%>
<html>
<head>
    <title>Ordini</title>
</head>
<body>
    <h1>Ordini</h1>

    <form method="get" action="${pageContext.request.contextPath}/admin/orders">
        <label for="email">Email cliente</label>
        <input type="text" name="email" id="email" value="<%= emailValue %>">

        <label for="dateFrom">Da</label>
        <input type="date" name="dateFrom" id="dateFrom" value="<%= dateFromValue %>">

        <label for="dateTo">A</label>
        <input type="date" name="dateTo" id="dateTo" value="<%= dateToValue %>">

        <button type="submit">Filtra</button>
    </form>

    <hr>

    <%
        if (filterError != null) {
    %>
        <p><%= filterError %></p>
    <%
        } else if (orders == null || orders.isEmpty()) {
    %>
        <p>Nessun ordine trovato</p>
    <%
        } else {
            for (OrdersBean order : orders) {
    %>
        <div>
            <p>Numero ordine: <%= order.getOrderID() %></p>
            <p>Data ordine: <%= order.getOrderDate() %></p>
            <p>Totale ordine: <%= order.getTotalPrice() %></p>
            <p>Nome cliente: <%= order.getCustomerFirstName() + " " + order.getCustomerLastName() %></p>
            <p>Email cliente: <%= order.getCustomerEmail() %></p>
            <a href="${pageContext.request.contextPath}/admin/order-detail?orderId=<%= order.getOrderID() %>">Vedi dettaglio</a>
        </div>
        <hr>
    <%
            }
        }
    %>
</body>
</html>