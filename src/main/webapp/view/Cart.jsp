<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CartBean" %>
<%@ page import="com.example.miaobau.model.CartItem" %>
<%
    CartBean cart = (CartBean) request.getSession().getAttribute("cart");
%>
<html>
<head>
    <title>Carrello</title>
</head>
<body>
    <h1>Carrello</h1>
    <%
        if (cart == null || cart.getCart().isEmpty()) {
    %>
        <p>Il carrello è vuoto</p>
        <a href="${pageContext.request.contextPath}/catalog">Vai al catalogo</a>
    <%
        } else {
            for (CartItem item : cart.getCart().values()) {
    %>
        <div>
            <p>Nome prodotto: <%= item.getProduct().getName() %></p>
            <p>Prezzo prodotto: <%= item.getProduct().getPrice() %></p>
            <p>Quantità: <%= item.getQuantity() %></p>

            <form method="post" action="${pageContext.request.contextPath}/cart">
                <input type="hidden" name="action" value="increase">
                <input type="hidden" name="productId" value="<%= item.getProduct().getProductID() %>">
                <button type="submit">Aumenta quantità</button>
            </form>

            <form method="post" action="${pageContext.request.contextPath}/cart">
                <input type="hidden" name="action" value="decrease">
                <input type="hidden" name="productId" value="<%= item.getProduct().getProductID() %>">
                <button type="submit">Diminuisci quantità</button>
            </form>

            <form method="post" action="${pageContext.request.contextPath}/cart">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="productId" value="<%= item.getProduct().getProductID() %>">
                <button type="submit">Rimuovi dal carrello</button>
            </form>
        </div>
        <hr>
    <%
            }
    %>
        <p><strong>Totale: <%= cart.getTotal() %></strong></p>

    <a href="${pageContext.request.contextPath}/secure/checkout">Procedi all'ordine</a>

        <form method="post" action="${pageContext.request.contextPath}/cart">
            <input type="hidden" name="action" value="clear">
            <button type="submit">Svuota carrello</button>
        </form>
    <%
        }
    %>
</body>
</html>
