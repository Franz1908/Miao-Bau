<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String errorMessage = (String) request.getAttribute("loginError"); %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Accedi &mdash; Miao &amp; Bau</title>

    <!-- Bootstrap 5 (locale) + foglio di stile del brand -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- ================= NAVBAR (frammento) ================= -->
<%@ include file="Navbar.jsp" %>

<!-- ================= CONTENUTO ================= -->
<main class="container my-4">
    <section class="mb-auth">
        <!-- Zampette decorative di sfondo -->
        <span class="mb-paw mb-paw-1">&#128062;</span>
        <span class="mb-paw mb-paw-4">&#128062;</span>

        <div class="mb-auth-card">
            <div class="mb-auth-head">
                <span class="mb-auth-emoji">&#128062;</span>
                <h1 class="mb-auth-title h3">Bentornato</h1>
                <p class="mb-auth-sub">Accedi al tuo account Miao &amp; Bau</p>
            </div>

            <!-- Messaggio d'errore dal LoginController (attributo "loginError") -->
            <% if (errorMessage != null) { %>
                <div class="mb-alert mb-alert-error"><%= errorMessage %></div>
            <% } %>

            <form class="mb-form" method="post" action="${pageContext.request.contextPath}/login">
                <div class="mb-3">
                    <label for="email" class="form-label">E-mail</label>
                    <input type="email" class="form-control" name="email" id="email"
                           placeholder="nome@esempio.it" required>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" id="password"
                           placeholder="La tua password" required>
                </div>

                <button type="submit" class="btn btn-mb-cta w-100 py-2">Accedi</button>
            </form>

            <p class="mb-auth-alt">
                Non hai un account?
                <a href="${pageContext.request.contextPath}/register">Registrati</a>
            </p>
        </div>
    </section>
</main>

<!-- ================= FOOTER (frammento) ================= -->
<%@ include file="Footer.jsp" %>

<!-- Bootstrap JS (locale) per navbar responsive e menu a tendina -->
<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
