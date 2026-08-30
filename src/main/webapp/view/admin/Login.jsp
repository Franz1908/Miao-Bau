<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String AdminErrorMessage = (String) request.getAttribute("AdminErrorMessage");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Accesso amministratore &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<main class="container my-4">
    <section class="mb-auth">
        <span class="mb-paw mb-paw-1">&#128062;</span>
        <span class="mb-paw mb-paw-4">&#128062;</span>

        <div class="mb-auth-card">
            <div class="mb-auth-head">
                <span class="mb-auth-emoji">&#128272;</span>
                <h1 class="mb-auth-title h3">Area amministratore</h1>
                <p class="mb-auth-sub">Accedi con le tue credenziali di gestione.</p>
            </div>

            <% if (AdminErrorMessage != null) { %>
                <div class="mb-alert mb-alert-error"><%= AdminErrorMessage %></div>
            <% } %>

            <form class="mb-form" method="post" action="${pageContext.request.contextPath}/admin/login">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" id="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" id="password" required>
                </div>
                <button type="submit" class="btn btn-mb-cta w-100 py-2">Accedi</button>
            </form>

            <p class="mb-auth-alt">
                <a href="${pageContext.request.contextPath}/home">&larr; Torna al sito</a>
            </p>
        </div>
    </section>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
