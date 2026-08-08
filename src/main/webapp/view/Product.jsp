<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%
    ProductBean product = (ProductBean) request.getAttribute("product");
%>
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
        <p>Prezzo: <%= product.getPrice() %></p>
    <%
        }
    %>
    <a href="${pageContext.request.contextPath}/catalog">Torna al catalogo</a>
</body>
</html>
