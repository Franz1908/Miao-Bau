<%--
  Created by IntelliJ IDEA.
  User: francesco
  Date: 11/08/26
  Time: 18:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <form method="post" action="login">

        <label for="email">E-mail</label>
        <input type="email" name="email" id="email" required>
        <br>

        <label for="password">Password</label>
        <input type="password" name="password" id="password" required>
        <br>

        <button type="submit">Accedi</button>

    </form>
</body>
</html>
