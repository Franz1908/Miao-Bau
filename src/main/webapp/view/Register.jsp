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
    <title>Registrati &mdash; Miao &amp; Bau</title>

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
        <span class="mb-paw mb-paw-2">&#128062;</span>
        <span class="mb-paw mb-paw-3">&#128062;</span>
        <span class="mb-paw mb-paw-4">&#128062;</span>

        <div class="mb-auth-card mb-auth-wide">
            <div class="mb-auth-head">
                <span class="mb-auth-emoji">&#128062;</span>
                <h1 class="mb-auth-title h3">Crea il tuo account</h1>
                <p class="mb-auth-sub">Unisciti a Miao &amp; Bau e coccola i tuoi amici a quattro zampe</p>
            </div>

            <!-- Elenco errori dal RegisterController (attributo "errors") -->
            <% if (errors != null && !errors.isEmpty()) { %>
                <div class="mb-alert mb-alert-error">
                    <ul>
                        <% for (String error : errors) { %>
                            <li><%= error %></li>
                        <% } %>
                    </ul>
                </div>
            <% } %>

            <form class="mb-form" method="post" action="${pageContext.request.contextPath}/register">
                <div class="row g-3">
                    <div class="col-12 col-md-6">
                        <label for="firstName" class="form-label">Nome</label>
                        <input type="text" class="form-control" name="firstName" id="firstName" required>
                    </div>
                    <div class="col-12 col-md-6">
                        <label for="lastName" class="form-label">Cognome</label>
                        <input type="text" class="form-control" name="lastName" id="lastName" required>
                    </div>

                    <div class="col-12">
                        <label for="email" class="form-label">E-mail</label>
                        <input type="email" class="form-control" name="email" id="email"
                               placeholder="nome@esempio.it" required>
                    </div>

                    <div class="col-12">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" name="password" id="password" required>
                        <div class="form-text">
                            Minimo 8 caratteri (max 16), con almeno un numero e un carattere speciale.
                        </div>
                    </div>

                    <div class="col-12 col-md-6">
                        <label for="birthDate" class="form-label">Data di nascita <span class="text-muted">(facoltativa)</span></label>
                        <input type="date" class="form-control" name="birthDate" id="birthDate">
                    </div>
                    <div class="col-12 col-md-6">
                        <label for="telephone" class="form-label">Telefono <span class="text-muted">(facoltativo)</span></label>
                        <input type="tel" class="form-control" name="telephone" id="telephone"
                               placeholder="es. 333 1234567">
                    </div>
                </div>

                <button type="submit" class="btn btn-mb-cta w-100 py-2 mt-4">Registrati</button>
            </form>

            <p class="mb-auth-alt">
                Hai gi&agrave; un account?
                <a href="${pageContext.request.contextPath}/login">Accedi</a>
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
