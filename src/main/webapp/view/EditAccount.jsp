<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page import="java.util.List" %>
<%
    CustomerBean customer = (CustomerBean) session.getAttribute("customer");
    List<String> errors = (List<String>) request.getAttribute("errors");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Modifica dati &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="Navbar.jsp" %>

<main class="container my-4">
    <a href="${pageContext.request.contextPath}/account" class="text-decoration-none d-inline-block mb-3">&larr; Torna all'account</a>

    <div class="mb-auth-card mb-auth-wide">
        <div class="mb-auth-head">
            <span class="mb-auth-emoji">&#128062;</span>
            <h1 class="mb-auth-title h3">Modifica i tuoi dati</h1>
            <p class="mb-auth-sub">Aggiorna le tue informazioni personali o cambia la password.</p>
        </div>

        <% if (errors != null && !errors.isEmpty()) { %>
        <div class="mb-alert mb-alert-error">
            <ul>
                <% for (String error : errors) { %>
                <li><%= error %></li>
                <% } %>
            </ul>
        </div>
        <% } %>

        <form class="mb-form" method="post" action="${pageContext.request.contextPath}/secure/account/edit">

            <h2 class="mb-panel-title mb-3">Dati personali</h2>
            <div class="row g-3">
                <div class="col-12 col-md-6">
                    <label for="firstName" class="form-label">Nome</label>
                    <input type="text" class="form-control" name="firstName" id="firstName"
                           value="<%= customer.getFirstName() %>" required>
                </div>
                <div class="col-12 col-md-6">
                    <label for="lastName" class="form-label">Cognome</label>
                    <input type="text" class="form-control" name="lastName" id="lastName"
                           value="<%= customer.getLastName() %>" required>
                </div>
                <div class="col-12">
                    <label for="email" class="form-label">E-mail</label>
                    <input type="email" class="form-control" name="email" id="email"
                           value="<%= customer.getEmail() %>" required>
                </div>
                <div class="col-12 col-md-6">
                    <label for="birthDate" class="form-label">Data di nascita <span class="text-muted">(facoltativa)</span></label>
                    <input type="date" class="form-control" name="birthDate" id="birthDate"
                           value="<%= customer.getBirthDate() != null ? customer.getBirthDate() : "" %>">
                </div>
                <div class="col-12 col-md-6">
                    <label for="phone" class="form-label">Telefono <span class="text-muted">(facoltativo)</span></label>
                    <input type="tel" class="form-control" name="phone" id="phone"
                           value="<%= customer.getPhone() != null ? customer.getPhone() : "" %>">
                </div>
            </div>

            <hr class="my-4">

            <h2 class="mb-panel-title mb-1">Cambia password</h2>
            <p class="form-text mb-3">Lascia i campi vuoti se non vuoi modificarla. Nuova password: 8&ndash;16 caratteri, con almeno un numero e un carattere speciale.</p>
            <div class="row g-3">
                <div class="col-12">
                    <label for="currentPassword" class="form-label">Password attuale</label>
                    <input type="password" class="form-control" name="currentPassword" id="currentPassword">
                </div>
                <div class="col-12 col-md-6">
                    <label for="newPassword" class="form-label">Nuova password</label>
                    <input type="password" class="form-control" name="newPassword" id="newPassword">
                </div>
                <div class="col-12 col-md-6">
                    <label for="confirmPassword" class="form-label">Conferma nuova password</label>
                    <input type="password" class="form-control" name="confirmPassword" id="confirmPassword">
                </div>
            </div>

            <div class="d-flex flex-wrap gap-2 mt-4">
                <button type="submit" class="btn btn-mb-cta px-4 py-2">Salva modifiche</button>
                <a href="${pageContext.request.contextPath}/account" class="btn btn-mb-primario px-4 py-2">Annulla</a>
            </div>
        </form>
    </div>
</main>

<%@ include file="Footer.jsp" %>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
