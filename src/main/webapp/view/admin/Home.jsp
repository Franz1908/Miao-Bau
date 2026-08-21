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

</body>
</html>
