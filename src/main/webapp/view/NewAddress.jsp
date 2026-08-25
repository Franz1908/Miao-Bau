<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Aggiungi indirizzo</title>
</head>
<body>
    <h1>Aggiungi un indirizzo</h1>

    <%
        List<String> errors = (List<String>) request.getAttribute("errors");
        if (errors != null) {
            for (String error : errors) {
    %>
        <p><%= error %></p>
    <%
            }
        }
    %>

    <form method="post" action="${pageContext.request.contextPath}/secure/address/new">

        <label for="street">Via</label>
        <input type="text" name="street" id="street" required>
        <br>

        <label for="civicNumber">Numero civico</label>
        <input type="text" name="civicNumber" id="civicNumber" required>
        <br>

        <label for="postalCode">CAP</label>
        <input type="text" name="postalCode" id="postalCode" required>
        <br>

        <label for="city">Città</label>
        <input type="text" name="city" id="city" required>
        <br>

        <label for="country">Paese</label>
        <input type="text" name="country" id="country" required>
        <br>

        <button type="submit">Aggiungi</button>
    </form>

    <a href="${pageContext.request.contextPath}/account">Torna all'account</a>
</body>
</html>