<%@ page import="com.example.miaobau.model.AdminBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  AdminBean adminBean = (AdminBean) request.getSession().getAttribute("admin");
%>
<html>
<head>
    <title>Home</title>
</head>
<body>

  <h1>Bentornato <%= adminBean.getUsername() %></h1>

  <a href="${pageContext.request.contextPath}/admin/catalog">Gestisci prodotti</a>
  <a href="${pageContext.request.contextPath}/admin/product/insert">Aggiungi prodotto</a>
  <a href="${pageContext.request.contextPath}/admin/orders">Vedi ordini</a>
  <a href="">Vedi utenti</a>

</body>
</html>
