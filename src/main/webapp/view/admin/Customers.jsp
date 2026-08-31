<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    List<CustomerBean> customers = (List<CustomerBean>) request.getAttribute("customers");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Clienti &mdash; Admin Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Barra amministratore -->
<nav class="navbar mb-navbar shadow-sm">
    <div class="container">
        <a class="navbar-brand mb-brand mb-0" href="${pageContext.request.contextPath}/admin/home">&#128062; Miao &amp; Bau &middot; Admin</a>
        <a href="${pageContext.request.contextPath}/admin/home" class="btn btn-outline-secondary btn-sm">&larr; Pannello</a>
    </div>
</nav>

<main class="container my-4">
    <h1 class="mb-1">Clienti</h1>
    <p class="text-muted mb-4">Tutti gli utenti registrati allo store.</p>

    <% if (customers == null || customers.isEmpty()) { %>
        <div class="mb-panel"><p class="text-muted mb-0">Nessun cliente registrato.</p></div>
    <% } else { %>
        <div class="mb-panel p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Nome</th>
                            <th scope="col">E-mail</th>
                            <th scope="col">Telefono</th>
                            <th scope="col">Data di nascita</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (CustomerBean customer : customers) { %>
                        <tr>
                            <td class="text-muted"><%= customer.getCustomerID() %></td>
                            <td class="fw-semibold"><%= customer.getFirstName() %> <%= customer.getLastName() %></td>
                            <td><a href="mailto:<%= customer.getEmail() %>" class="text-decoration-none"><%= customer.getEmail() %></a></td>
                            <td>
                                <% if (customer.getPhone() != null) { %>
                                    <%= customer.getPhone() %>
                                <% } else { %>
                                    <span class="text-muted fst-italic">non disponibile</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (customer.getBirthDate() != null) { %>
                                    <%= customer.getBirthDate().format(fmt) %>
                                <% } else { %>
                                    <span class="text-muted fst-italic">&mdash;</span>
                                <% } %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    <% } %>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
