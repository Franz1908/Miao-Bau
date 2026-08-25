<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%
    CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
%>
<html>
<head>
    <title>Il mio account</title>
</head>
<body>
    <%
        if (customerBean != null) {
    %>
        <h2>Bentornato <%= customerBean.getFirstName() %>!</h2>

        <h3>I miei dati</h3>
        <p>Nome: <%= customerBean.getFirstName() %></p>
        <p>Cognome: <%= customerBean.getLastName() %></p>
        <p>Email: <%= customerBean.getEmail() %></p>
        <%
            if (customerBean.getBirthDate() != null) {
        %>
        <p>Data di nascita: <%= customerBean.getBirthDate() %></p>
        <%
        } else {
        %>
        <p>Data di nascita: non impostata</p>
        <%
            }
        %>
        <%
            if (customerBean.getPhone() != null) {
        %>
            <p>Telefono: <%= customerBean.getPhone() %></p>
        <%
            } else {
        %>
            <p>Telefono: non impostato</p>
        <%
            }
        %>

        <a href="${pageContext.request.contextPath}/secure/account/edit">Modifica dati</a>

        <h3>Sezioni</h3>
        <a href="${pageContext.request.contextPath}/cart">Carrello</a>
        <a href="${pageContext.request.contextPath}/secure/orders">Visualizza ordini</a>

        <form method="post" action="${pageContext.request.contextPath}/logout">
            <button type="submit">Logout</button>
        </form>
    <%
        } else {
    %>
        <a href="${pageContext.request.contextPath}/register">Registrati</a>
        <a href="${pageContext.request.contextPath}/login">Accedi</a>
    <%
        }
    %>
</body>
</html>