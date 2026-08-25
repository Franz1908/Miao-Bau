<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page import="java.util.List" %>
<%
    CustomerBean customer = (CustomerBean) session.getAttribute("customer");
%>
<html>
<head>
    <title>Modifica dati</title>
</head>
<body>
    <h1>Modifica i tuoi dati</h1>

    <%
        List<String> errors = (List<String>) request.getAttribute("errorMessage");
        if (errors != null) {
            for (String error : errors) {
    %>
        <p><%= error %></p>
    <%
            }
        }
    %>

    <form method="post" action="${pageContext.request.contextPath}/secure/account/edit">

        <h3>Dati personali</h3>

        <label for="firstName">Nome</label>
        <input type="text" name="firstName" id="firstName" value="<%= customer.getFirstName() %>" required>
        <br>

        <label for="lastName">Cognome</label>
        <input type="text" name="lastName" id="lastName" value="<%= customer.getLastName() %>" required>
        <br>

        <label for="email">Email</label>
        <input type="email" name="email" id="email" value="<%= customer.getEmail() %>" required>
        <br>

        <label for="birthDate">Data di nascita</label>
        <input type="date" name="birthDate" id="birthDate"
               value="<%= customer.getBirthDate() != null ? customer.getBirthDate() : "" %>">
        <br>

        <label for="phone">Telefono</label>
        <input type="text" name="phone" id="phone"
               value="<%= customer.getPhone() != null ? customer.getPhone() : "" %>">
        <br>

        <h3>Cambia password (lascia vuoto per non modificarla)</h3>

        <label for="currentPassword">Password attuale</label>
        <input type="password" name="currentPassword" id="currentPassword">
        <br>

        <label for="newPassword">Nuova password</label>
        <input type="password" name="newPassword" id="newPassword">
        <br>

        <label for="confirmPassword">Conferma nuova password</label>
        <input type="password" name="confirmPassword" id="confirmPassword">
        <br>

        <button type="submit">Salva modifiche</button>
    </form>

    <a href="${pageContext.request.contextPath}/account">Torna all'account</a>
</body>
</html>
