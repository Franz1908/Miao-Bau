<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.AdminBean" %>
<%
    AdminBean adminBean = (AdminBean) request.getSession().getAttribute("admin");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pannello amministratore &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Barra amministratore (riusa .mb-navbar) -->
<nav class="navbar mb-navbar shadow-sm">
    <div class="container">
        <span class="navbar-brand mb-brand mb-0">&#128062; Miao &amp; Bau &middot; Admin</span>
        <span class="navbar-text">Amministratore: <strong><%= adminBean.getUsername() %></strong></span>
    </div>
</nav>

<main class="container my-4">
    <div class="mb-account-hero">
        <div>
            <h1>Ciao <%= adminBean.getUsername() %>!</h1>
            <p>Gestisci il catalogo, gli ordini e gli utenti dello store.</p>
        </div>
        <div class="d-flex flex-wrap gap-2">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">Vai al sito</a>
            <form method="post" action="${pageContext.request.contextPath}/admin/logout" class="m-0">
                <button type="submit" class="btn btn-mb-primario">Esci</button>
            </form>
        </div>
    </div>

    <div class="row row-cols-1 row-cols-md-2 g-3">
        <div class="col">
            <a href="${pageContext.request.contextPath}/admin/catalog" class="mb-quicklink">
                <span class="mb-quicklink-ico">&#128230;</span>
                <span>Gestisci prodotti</span>
                <span class="mb-quicklink-arrow">&rarr;</span>
            </a>
        </div>
        <div class="col">
            <a href="${pageContext.request.contextPath}/admin/product/insert" class="mb-quicklink">
                <span class="mb-quicklink-ico">&#10133;</span>
                <span>Aggiungi prodotto</span>
                <span class="mb-quicklink-arrow">&rarr;</span>
            </a>
        </div>
        <div class="col">
            <a href="${pageContext.request.contextPath}/admin/orders" class="mb-quicklink">
                <span class="mb-quicklink-ico">&#129534;</span>
                <span>Vedi ordini</span>
                <span class="mb-quicklink-arrow">&rarr;</span>
            </a>
        </div>
        <div class="col">
            <a href="${pageContext.request.contextPath}/admin/customers" class="mb-quicklink">
                <span class="mb-quicklink-ico">&#128100;</span>
                <span>Vedi utenti</span>
                <span class="mb-quicklink-arrow">&rarr;</span>
            </a>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
