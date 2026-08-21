<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String AdminErrorMessage = (String) request.getAttribute("AdminErrorMessage");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <%
        if (AdminErrorMessage != null) {
    %>
        <p style="color: red"><%= AdminErrorMessage %></p>
    <%
        }
    %>
    <form method="post" action="${pageContext.request.contextPath}/admin/login">

        <label for="username">Username</label>
        <input type="text" name="username" id="username" required>
        <br>

        <label for="password">Password</label>
        <input type="password" name="password" id="password" required>
        <br>

        <button type="submit">Accedi</button>

    </form>

</body>
</html>
