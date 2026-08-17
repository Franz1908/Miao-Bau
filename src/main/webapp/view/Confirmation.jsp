<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String orderId = request.getParameter("orderId");
%>
<html>
<head>
    <title>Conferma ordine</title>
</head>
<body>
    <h1>Ordine confermato!</h1>
    <p>Numero ordine <%= orderId %></p>
    <a href="${pageContext.request.contextPath}/catalog">Torna al catalogo</a>
</body>
</html>
