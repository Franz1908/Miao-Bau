<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
%>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>

    <%
      if(customerBean != null) {
    %>
      <h2>Bentornato <%=customerBean.getFirstName()%>!</h2>
      <a href="${pageContext.request.contextPath}/index.jsp">torna alla home</a>
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
