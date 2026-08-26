<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%@ page import="com.example.miaobau.model.OrderItemBean" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%
    OrdersBean order = (OrdersBean) request.getAttribute("order");
    AddressBean address = (AddressBean) request.getAttribute("address");
%>
<html>
<head>
    <title>Dettaglio ordine</title>
</head>
<body>
    <h1>Ordine #<%= order.getOrderID() %></h1>
    <p>Data: <%= order.getOrderDate() %></p>

    <h3>Indirizzo di spedizione</h3>
    <%
        if (address != null) {
    %>
        <p><%= address.getStreet() %>, <%= address.getCivicNumber() %></p>
        <p><%= address.getPostalCode() %> <%= address.getCity() %> (<%= address.getCountry() %>)</p>
    <%
        } else {
    %>
        <p>Indirizzo non disponibile</p>
    <%
        }
    %>

    <hr>
    <%
        for (OrderItemBean item : order.getItems()) {
    %>
        <div>
            <p>Prodotto: <%= item.getProductName() %></p>
            <p>Quantità: <%= item.getQuantity() %></p>
            <p>Prezzo unitario: <%= item.getUnitPrice() %></p>
            <p>IVA: <%= item.getVatFrozen() %></p>
        </div>
        <hr>
    <%
        }
    %>
    <p><strong>Totale ordine: <%= order.getTotalPrice() %></strong></p>
    <a href="${pageContext.request.contextPath}/secure/orders">Torna ai miei ordini</a>
</body>
</html>
