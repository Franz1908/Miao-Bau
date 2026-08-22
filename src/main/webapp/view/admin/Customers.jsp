<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<CustomerBean> customers = (List<CustomerBean>) request.getAttribute("customers");
%>
<html>
<head>
    <title>Clienti</title>
</head>
<body>

<%
    if (customers == null || customers.isEmpty()) {
%>
    <p>Nessun cliente registrato</p>
<%
    } else {
        for (CustomerBean customer : customers) {
%>
    <div>
        <p>Nome: <%= customer.getFirstName() %> <%= customer.getLastName() %></p>
        <p>Email: <%= customer.getEmail() %></p>
        <%
            if (customer.getPhone() == null) {
        %>
            <p>Numero di telefono: non disponibile</p>
        <%
            } else {
        %>
            <p>Numero di telefono: <%= customer.getPhone() %></p>
        <%
            }
        %>
    </div>
    <hr>
<%
        }
    }
%>

</body>
</html>
