<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<OrdersBean> orders = (List<OrdersBean>) request.getAttribute("orders");
%>
<html>
<head>
    <title>Ordini</title>
</head>
<body>

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
            <p>Nome cliente: <%= order.getCustomerFirstName() + " " + order.getCustomerLastName()%> </p>
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
