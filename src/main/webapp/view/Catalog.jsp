<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    String title = (String) request.getAttribute("title");
%>
<html>
<head>
    <title>Catalogo</title>
</head>
<body>
    <h1><%=title%></h1>
    <%
        if (products == null || products.isEmpty()) {
    %>
        <p>Nessun prodotto disponibile.</p>
    <%
        } else {
            for (ProductBean product : products) {
    %>
        <div>
            <p>Nome prodotto: <%= product.getName() %></p>
            <p>Marca prodotto: <%= product.getBrand() %></p>
            <p>Prezzo prodotto: <%= product.getPrice() %></p>
            <a href="${pageContext.request.contextPath}/product?productId=<%= product.getProductID() %>">Scopri di più</a>
        </div>
        <hr>
    <%
            }
        }
    %>
</body>
</html>
