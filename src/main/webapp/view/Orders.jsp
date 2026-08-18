<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%
    List<OrdersBean> orders = (List<OrdersBean>) request.getAttribute("orders");
%>
<html>
<head>
    <title>I miei ordini</title>
</head>
<body>
    <h1>I miei ordini</h1>
    <%
        if (orders == null || orders.isEmpty()) {
    %>
        <p>Nessun ordine effettuato</p>
    <%
        } else {
            for (OrdersBean order : orders) {
    %>
        <div>
            <p>Numero ordine: <%= order.getOrderID() %></p>
            <p>Data ordine: <%= order.getOrderDate() %></p>
            <p>Totale ordine: <%= order.getTotalPrice() %></p>
            <a href="${pageContext.request.contextPath}/order-detail?orderId=<%= order.getOrderID() %>">Vedi dettaglio</a>
        </div>
        <hr>
    <%
            }
        }
    %>
</body>
</html>