<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CartBean" %>
<%@ page import="com.example.miaobau.model.CartItem" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.List" %>
<%
    CartBean cart = (CartBean) session.getAttribute("cart");
    List<AddressBean> addresses = (List<AddressBean>) request.getAttribute("addresses");
    List<String> errors = (List<String>) request.getAttribute("errors");
    boolean hasAddresses = addresses != null && !addresses.isEmpty();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Riepilogo ordine &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="Navbar.jsp" %>

<main class="container my-4">
    <h1 class="mb-1">Riepilogo ordine</h1>
    <p class="text-muted mb-4">Controlla i prodotti e scegli l'indirizzo di spedizione.</p>

    <%
        if (errors != null && !errors.isEmpty()) {
    %>
    <div class="mb-alert mb-alert-error">
        <ul>
            <% for (String error : errors) { %>
            <li><%= error %></li>
            <% } %>
        </ul>
    </div>
    <%
        }
    %>

    <form method="post" action="${pageContext.request.contextPath}/secure/checkout" class="mb-form" id="checkoutForm">
        <div class="row g-4">

            <!-- ---- Indirizzo di spedizione ---- -->
            <div class="col-12 col-lg-7">
                <div class="mb-panel">
                    <div class="mb-panel-head">
                        <h2 class="mb-panel-title">Indirizzo di spedizione</h2>
                    </div>

                    <div class="d-flex flex-column gap-2">
                        <%
                            if (hasAddresses) {
                                boolean first = true;
                                for (AddressBean address : addresses) {
                        %>
                        <label class="mb-addr-option">
                            <input type="radio" name="addressChoice"
                                   value="<%= address.getAddressID() %>" <%= first ? "checked" : "" %>>
                            <span>
                                <span class="mb-addr-main d-block"><%= address.getStreet() %>, <%= address.getCivicNumber() %></span>
                                <span class="mb-addr-sub"><%= address.getPostalCode() %> <%= address.getCity() %> (<%= address.getCountry() %>)</span>
                            </span>
                        </label>
                        <%
                                    first = false;
                                }
                            }
                        %>
                        <label class="mb-addr-option">
                            <input type="radio" name="addressChoice" value="new" <%= hasAddresses ? "" : "checked" %>>
                            <span class="mb-addr-main">Usa un nuovo indirizzo</span>
                        </label>
                    </div>

                    <!-- ---- Campi nuovo indirizzo ---- -->
                    <div class="mb-newaddr mt-3" id="newAddrBox">
                        <div class="row g-3">
                            <div class="col-12 col-md-8">
                                <label for="street" class="form-label">Via</label>
                                <input type="text" class="form-control" name="street" id="street">
                            </div>
                            <div class="col-12 col-md-4">
                                <label for="civicNumber" class="form-label">Numero civico</label>
                                <input type="text" class="form-control" name="civicNumber" id="civicNumber">
                            </div>
                            <div class="col-12 col-md-4">
                                <label for="postalCode" class="form-label">CAP</label>
                                <input type="text" class="form-control" name="postalCode" id="postalCode" placeholder="84121">
                            </div>
                            <div class="col-12 col-md-4">
                                <label for="city" class="form-label">Citt&agrave;</label>
                                <input type="text" class="form-control" name="city" id="city">
                            </div>
                            <div class="col-12 col-md-4">
                                <label for="country" class="form-label">Paese</label>
                                <input type="text" class="form-control" name="country" id="country" placeholder="Italia">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ---- Riepilogo prodotti + totale ---- -->
            <div class="col-12 col-lg-5">
                <div class="mb-summary">
                    <h2 class="mb-panel-title mb-3">Il tuo ordine</h2>
                    <%
                        for (CartItem item : cart.getCart().values()) {
                            BigDecimal unit = item.getProduct().isOnSale()
                                    ? item.getProduct().getDiscountedPrice()
                                    : item.getProduct().getPrice();
                            BigDecimal lineTotal = unit.multiply(BigDecimal.valueOf(item.getQuantity()));
                    %>
                    <div class="mb-order-line">
                        <span>
                            <span class="mb-order-name d-block"><%= item.getProduct().getName() %></span>
                            <span class="mb-order-meta"><%= item.getQuantity() %> &times; &euro; <%= unit %></span>
                        </span>
                        <span class="mb-order-price">&euro; <%= lineTotal %></span>
                    </div>
                    <%
                        }
                    %>

                    <div class="d-flex justify-content-between align-items-center mt-3 mb-3">
                        <span class="fw-bold">Totale</span>
                        <span class="mb-prezzo">&euro; <%= cart.getTotal() %></span>
                    </div>

                    <button type="submit" class="btn btn-mb-cta w-100 py-2">Conferma ordine</button>
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-mb-primario w-100 mt-2">Torna al carrello</a>
                </div>
            </div>

        </div>
    </form>
</main>

<%@ include file="Footer.jsp" %>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Mostra i campi del nuovo indirizzo solo se e' selezionata l'opzione "new".
    (function () {
        var form = document.getElementById('checkoutForm');
        if (!form) return;
        var box = document.getElementById('newAddrBox');
        function sync() {
            var sel = form.querySelector('input[name="addressChoice"]:checked');
            var isNew = sel && sel.value === 'new';
            box.style.display = isNew ? '' : 'none';
            box.querySelectorAll('input').forEach(function (i) { i.disabled = !isNew; });
        }
        form.querySelectorAll('input[name="addressChoice"]').forEach(function (r) {
            r.addEventListener('change', sync);
        });
        sync();
    })();
</script>
</body>
</html>
