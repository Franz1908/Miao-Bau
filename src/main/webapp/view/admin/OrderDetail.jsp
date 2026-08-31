<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%@ page import="com.example.miaobau.model.OrderItemBean" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    OrdersBean order = (OrdersBean) request.getAttribute("order");
    AddressBean address = (AddressBean) request.getAttribute("address");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dettaglio ordine &mdash; Admin Miao &amp; Bau</title>
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
    <a href="${pageContext.request.contextPath}/admin/orders" class="text-decoration-none d-inline-block mb-3">&larr; Torna agli ordini</a>

    <div class="d-flex flex-wrap align-items-center gap-3 mb-4">
        <h1 class="mb-0">Ordine n&deg; <%= order.getOrderID() %></h1>
        <span class="text-muted"><%= order.getOrderDate() != null ? order.getOrderDate().format(fmt) : "" %></span>
    </div>

    <div class="row g-4">
        <!-- Cliente -->
        <div class="col-12 col-lg-6">
            <div class="mb-panel h-100">
                <div class="mb-panel-head"><h2 class="mb-panel-title">Cliente</h2></div>
                <div class="mb-data-grid">
                    <div class="mb-data-item">
                        <span class="mb-data-label">Nome</span>
                        <span class="mb-data-value"><%= order.getCustomerFirstName() %> <%= order.getCustomerLastName() %></span>
                    </div>
                    <div class="mb-data-item">
                        <span class="mb-data-label">E-mail</span>
                        <span class="mb-data-value"><%= order.getCustomerEmail() %></span>
                    </div>
                    <div class="mb-data-item">
                        <span class="mb-data-label">Telefono</span>
                        <% if (order.getCustomerPhone() != null) { %>
                            <span class="mb-data-value"><%= order.getCustomerPhone() %></span>
                        <% } else { %>
                            <span class="mb-data-value mb-data-empty">Non disponibile</span>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <!-- Indirizzo -->
        <div class="col-12 col-lg-6">
            <div class="mb-panel h-100">
                <div class="mb-panel-head"><h2 class="mb-panel-title">Indirizzo di spedizione</h2></div>
                <% if (address != null) { %>
                    <p class="mb-address-street"><%= address.getStreet() %>, <%= address.getCivicNumber() %></p>
                    <p class="mb-address-city"><%= address.getPostalCode() %> <%= address.getCity() %> (<%= address.getCountry() %>)</p>
                <% } else { %>
                    <p class="text-muted mb-0">Indirizzo non disponibile</p>
                <% } %>
            </div>
        </div>

        <!-- Prodotti -->
        <div class="col-12">
            <div class="mb-panel">
                <div class="mb-panel-head"><h2 class="mb-panel-title">Prodotti</h2></div>
                <% for (OrderItemBean item : order.getItems()) {
                       BigDecimal lineTotal = item.getUnitPrice() != null
                               ? item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                               : BigDecimal.ZERO; %>
                <div class="mb-order-line">
                    <span>
                        <span class="mb-order-name d-block"><%= item.getProductName() %></span>
                        <span class="mb-order-meta"><%= item.getQuantity() %> &times; &euro; <%= item.getUnitPrice() %> &middot; IVA <%= item.getVatFrozen() %>%</span>
                    </span>
                    <span class="mb-order-price">&euro; <%= lineTotal %></span>
                </div>
                <% } %>
                <div class="d-flex justify-content-between align-items-center mt-3">
                    <span class="fw-bold">Totale ordine</span>
                    <span class="mb-prezzo">&euro; <%= order.getTotalPrice() %></span>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
