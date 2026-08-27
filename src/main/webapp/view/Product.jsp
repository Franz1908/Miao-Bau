<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%
    ProductBean product = (ProductBean) request.getAttribute("product");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dettaglio prodotto</title>
</head>
<body>
<%
    if (product == null) {
%>
    <p>Errore, impossibile mostrare il prodotto.</p>
<%
    } else {
%>
    <h1><%= product.getName() %></h1>
    <p>Descrizione: <%= product.getDescription() %></p>
    <p>Marca: <%= product.getBrand() %></p>
<%
        if (product.isOnSale()) {
%>
    <p>Prezzo vecchio: <%= product.getPrice() %></p>
    <p>Prezzo scontato: <%= product.getDiscountedPrice() %></p>
<%
        } else {
%>
    <p>Prezzo: <%= product.getPrice() %></p>
<%
        }
%>
    <form method="post" action="${pageContext.request.contextPath}/cart">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="productId" value="<%= product.getProductID() %>">
        <input type="number" name="quantity" min="1" value="1" step="1">
        <button type="submit">Aggiungi al carrello</button>
    </form>
<%
    }
%>
<a href="${pageContext.request.contextPath}/catalog">Torna al catalogo</a>
</body>
</html>