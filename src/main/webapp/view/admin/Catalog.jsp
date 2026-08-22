<%@ page import="com.example.miaobau.model.ProductBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("adminProducts");
%>
<html>
<head>
    <title>Catalogo</title>
</head>
<body>

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
        <a href="${pageContext.request.contextPath}/admin/product/update?productId=<%= product.getProductID() %>">Modifica prodotto</a>
        <%
            if (product.isDeleted()) {
        %>
            <form method="post" action="${pageContext.request.contextPath}/admin/product/restore">
                <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                <button type="submit">Ripristina prodotto</button>
            </form>
            <p style="color: red">Inattivo</p>
        <%
            } else {
        %>
            <form method="post" action="${pageContext.request.contextPath}/admin/product/delete">
                <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                <button type="submit">Elimina prodotto</button>
            </form>
            <p style="color: green">Attivo</p>
        <%
            }
        %>

    </div>
    <hr>
    <%
            }
        }
    %>

</body>
</html>
