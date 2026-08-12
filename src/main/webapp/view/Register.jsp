<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <form method="post" action="${pageContext.request.contextPath}/register">

        <label for="firstName">Nome</label>
        <input type="text" name="firstName" id="firstName" required>
        <br>

        <label for="lastName">Cognome</label>
        <input type="text" name="lastName" id="lastName" required>
        <br>

        <label for="email">E-mail</label>
        <input type="email" name="email" id="email" required>
        <br>

        <label for="password">Password</label>
        <input type="password" name="password" id="password" required>
        <br>

        <label for="birthDate">Data di nascita</label>
        <input type="date" name="birthDate" id="birthDate">
        <br>

        <label for="telephone">Numero di Telefono:</label>
        <input type="tel" name="telephone" id="telephone">
        <br>

        <button type="submit">Registrati</button>

    </form>
</body>
</html>


