<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    List<String> errors = (List<String>) request.getAttribute("errors");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Aggiungi indirizzo &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="Navbar.jsp" %>

<main class="container my-4">
    <a href="${pageContext.request.contextPath}/account" class="text-decoration-none d-inline-block mb-3">&larr; Torna all'account</a>

    <div class="mb-auth-card">
        <div class="mb-auth-head">
            <span class="mb-auth-emoji">&#128205;</span>
            <h1 class="mb-auth-title h3">Aggiungi un indirizzo</h1>
            <p class="mb-auth-sub">Salva un nuovo indirizzo di spedizione.</p>
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

        <form class="mb-form" method="post" action="${pageContext.request.contextPath}/secure/address/new">
            <div class="row g-3">
                <div class="col-12 col-md-8">
                    <label for="street" class="form-label">Via</label>
                    <input type="text" class="form-control" name="street" id="street" required>
                </div>
                <div class="col-12 col-md-4">
                    <label for="civicNumber" class="form-label">Numero civico</label>
                    <input type="text" class="form-control" name="civicNumber" id="civicNumber" required>
                </div>
                <div class="col-12 col-md-4">
                    <label for="postalCode" class="form-label">CAP</label>
                    <input type="text" class="form-control" name="postalCode" id="postalCode"
                           placeholder="84121" pattern="\d{5}" required>
                </div>
                <div class="col-12 col-md-4">
                    <label for="city" class="form-label">Citt&agrave;</label>
                    <input type="text" class="form-control" name="city" id="city" required>
                </div>
                <div class="col-12 col-md-4">
                    <label for="country" class="form-label">Paese</label>
                    <input type="text" class="form-control" name="country" id="country" placeholder="Italia" required>
                </div>
            </div>

            <div class="d-flex flex-wrap gap-2 mt-4">
                <button type="submit" class="btn btn-mb-cta px-4 py-2">Aggiungi indirizzo</button>
                <a href="${pageContext.request.contextPath}/account" class="btn btn-mb-primario px-4 py-2">Annulla</a>
            </div>
        </form>
    </div>
</main>

<%@ include file="Footer.jsp" %>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
