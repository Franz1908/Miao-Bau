<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CartBean" %>
<%@ page import="com.example.miaobau.model.CartItem" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%@ page import="java.util.List" %>
<%
    CartBean cart = (CartBean) session.getAttribute("cart");
    List<AddressBean> addresses = (List<AddressBean>) request.getAttribute("addresses");
    List<String> errors = (List<String>) request.getAttribute("errors");
%>
<html>
<head>
    <title>Riepilogo ordine</title>
</head>
<body>
    <h1>Riepilogo ordine</h1>

    <%
        if (errors != null) {
    %>
        <p><%= errors %></p>
    <%
        }
    %>

    <h3>Prodotti</h3>
    <%
        for (CartItem item : cart.getCart().values()) {
    %>
        <div>
            <p><%= item.getProduct().getName() %> —
               <%= item.getQuantity() %> ×
               <%= item.getProduct().getPrice() %> €</p>
        </div>
    <%
        }
    %>

    <p><strong>Totale: <%= cart.getTotal() %> €</strong></p>

    <hr>

    <form method="post" action="${pageContext.request.contextPath}/secure/checkout">

        <h3>Indirizzo di spedizione</h3>
        <%
            if (addresses != null && !addresses.isEmpty()) {
                boolean first = true;
                for (AddressBean address : addresses) {
        %>
            <div>
                <label>
                    <input type="radio" name="addressChoice"
                           value="<%= address.getAddressID() %>" <%= first ? "checked" : "" %>>
                    <%= address.getStreet() %>, <%= address.getCivicNumber() %> —
                    <%= address.getPostalCode() %> <%= address.getCity() %> (<%= address.getCountry() %>)
                </label>
            </div>
        <%
                    first = false;
                }
            }
        %>

        <div>
            <label>
                <input type="radio" name="addressChoice" value="new"
                    <%= (addresses == null || addresses.isEmpty()) ? "checked" : "" %>>
                Usa un nuovo indirizzo
            </label>
        </div>

        <h4>Nuovo indirizzo</h4>
        <label for="street">Via</label>
        <input type="text" name="street" id="street">
        <br>
        <label for="civicNumber">Numero civico</label>
        <input type="text" name="civicNumber" id="civicNumber">
        <br>
        <label for="postalCode">CAP</label>
        <input type="text" name="postalCode" id="postalCode">
        <br>
        <label for="city">Città</label>
        <input type="text" name="city" id="city">
        <br>
        <label for="country">Paese</label>
        <input type="text" name="country" id="country">
        <br>

        <hr>

        <button type="submit">Conferma ordine</button>
    </form>
</body>
</html>
