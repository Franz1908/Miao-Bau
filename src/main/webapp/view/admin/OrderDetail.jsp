<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%@ page import="com.example.miaobau.model.OrderItemBean" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    OrdersBean order = (OrdersBean) request.getAttribute("order");
    AddressBean address = (AddressBean) request.getAttribute("address");
    String customer = order.getCustomerFirstName() + " " + order.getCustomerLastName();
    String customerEmail = order.getCustomerEmail();
    String customerPhone = order.getCustomerPhone();
%>
<html>
<head>
    <title>Dettagli ordine</title>
</head>
<body>
    <h1>Ordine #<%= order.getOrderID() %></h1>
    <p>Data: <%= order.getOrderDate() %></p>
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
    <p>Nome cliente: <%= customer %> </p>
    <p>Email cliente: <%= customerEmail %></p>
    <%
        if (customerPhone != null) {
    %>
        <p>Telefono cliente: <%= customerPhone %></p>
    <%
        } else {
    %>
        <p>Telefono cliente:non disponibile</p>
    <%
        }
    %>

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

    <p><strong>Totale ordine: <%= order.getTotalPrice() %></strong></p>
    <a href="${pageContext.request.contextPath}/admin/orders">Torna indietro</a>
</body>
</html>
