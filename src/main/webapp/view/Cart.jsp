<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.example.miaobau.model.CartBean" %>
<%@ page import="com.example.miaobau.model.CartItem" %>
<%
    CartBean cart = (CartBean) session.getAttribute("cart");
    boolean cartVuoto = (cart == null || cart.getCart().isEmpty());
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Carrello — Miao &amp; Bau</title>

    <!-- Bootstrap 5 (locale) + foglio di stile del brand -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- ================= NAVBAR (frammento) ================= -->
<%@ include file="Navbar.jsp" %>

<!-- ================= CONTENUTO ================= -->
<main class="container my-4">

    <%
        if (cartVuoto) {
            // ---- Carrello vuoto: stato vuoto riutilizzabile ----
            request.setAttribute("esTitle", "Il tuo carrello è vuoto");
            request.setAttribute("esText", "Non hai ancora aggiunto nessun prodotto. "
                    + "Dai un'occhiata al catalogo e riempilo di coccole!");
            request.setAttribute("esImg", ctx + "/img/carrello-vuoto.png");
            request.setAttribute("esLink", ctx + "/catalog");
            request.setAttribute("esCta", "Vai al catalogo");
    %>
    <%@ include file="error/EmptyState.jsp" %>
    <%
    } else {
    %>

    <h1 class="mb-4">Il tuo carrello</h1>

    <div class="row g-4">

        <!-- ---- Colonna sinistra: righe prodotto ---- -->
        <div class="col-12 col-lg-8">
            <div class="d-flex flex-column gap-3">
                <%
                    // ====== INIZIO LOOP ARTICOLI ======
                    for (CartItem item : cart.getCart().values()) {
                        String img = (item.getProduct().getImage() == null) ? "" : item.getProduct().getImage();
                        // Prezzo unitario: scontato se in offerta, altrimenti pieno
                        BigDecimal unitario = item.getProduct().isOnSale()
                                ? item.getProduct().getDiscountedPrice()
                                : item.getProduct().getPrice();
                        // Subtotale riga = prezzo unitario * quantita'
                        BigDecimal subtotale = unitario.multiply(BigDecimal.valueOf(item.getQuantity()));
                        int pid = item.getProduct().getProductID();
                %>
                <div class="mb-cart-row d-flex gap-3 align-items-center">

                    <!-- Immagine prodotto (con segnaposto CSS se manca) -->
                    <div class="mb-img-box mb-cart-img<%= img.isEmpty() ? " mb-img-vuota" : "" %>">
                        <% if (!img.isEmpty()) { %>
                        <img src="${pageContext.request.contextPath}/img/products/<%= img %>"
                             class="mb-card-img"
                             alt="<%= item.getProduct().getName() %>"
                             onerror="this.onerror=null; this.style.display='none'; this.parentNode.classList.add('mb-img-vuota');">
                        <% } %>
                    </div>

                    <!-- Nome, marca, prezzo unitario -->
                    <div class="flex-grow-1">
                        <p class="mb-nome mb-0"><%= item.getProduct().getName() %></p>
                        <p class="mb-marca mb-2"><%= item.getProduct().getBrand() %></p>
                        <% if (item.getProduct().isOnSale()) { %>
                        <span class="mb-prezzo mb-prezzo-sconto" style="font-size:1rem;">&euro; <%= item.getProduct().getDiscountedPrice() %></span>
                        <span class="mb-prezzo-vecchio ms-1">&euro; <%= item.getProduct().getPrice() %></span>
                        <% } else { %>
                        <span class="mb-prezzo" style="font-size:1rem;">&euro; <%= item.getProduct().getPrice() %></span>
                        <% } %>
                        <span class="mb-marca">/ cad.</span>
                    </div>

                    <!-- Selettore quantita' (- valore +) + rimuovi -->
                    <div class="text-center">
                        <div class="mb-qty mb-2">
                            <!-- Diminuisci -->
                            <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                                <input type="hidden" name="action" value="decrease">
                                <input type="hidden" name="productId" value="<%= pid %>">
                                <button type="submit" aria-label="Diminuisci">&minus;</button>
                            </form>
                            <span class="mb-qty-val"><%= item.getQuantity() %></span>
                            <!-- Aumenta -->
                            <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                                <input type="hidden" name="action" value="increase">
                                <input type="hidden" name="productId" value="<%= pid %>">
                                <button type="submit" aria-label="Aumenta">+</button>
                            </form>
                        </div>
                        <br>
                        <!-- Rimuovi -->
                        <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0 d-inline">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="<%= pid %>">
                            <button type="submit" class="mb-remove">Rimuovi</button>
                        </form>
                    </div>

                    <!-- Subtotale riga -->
                    <div class="text-end" style="min-width:90px;">
                        <span class="mb-prezzo"><%= subtotale %> &euro;</span>
                    </div>

                </div>
                <%
                    } // ====== FINE LOOP ARTICOLI ======
                %>
            </div>
        </div>

        <!-- ---- Colonna destra: riepilogo ordine ---- -->
        <div class="col-12 col-lg-4">
            <div class="mb-summary">
                <h4 class="mb-3">Riepilogo</h4>

                <div class="d-flex justify-content-between mb-2">
                    <span>Articoli</span>
                    <span><%= cart.getTotalQuantity() %></span>
                </div>
                <hr style="border-color:#E9ECEF;">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <span class="fw-bold">Totale</span>
                    <span class="mb-prezzo" style="font-size:1.5rem;">&euro; <%= cart.getTotal() %></span>
                </div>

                <!-- Procedi all'ordine -->
                <a href="${pageContext.request.contextPath}/secure/checkout"
                   class="btn btn-mb-cta btn-lg w-100 mb-2">Procedi all'ordine</a>

                <!-- Continua lo shopping -->
                <a href="${pageContext.request.contextPath}/catalog"
                   class="btn btn-mb-primario w-100 mb-3">Continua lo shopping</a>

                <!-- Svuota carrello -->
                <form method="post" action="${pageContext.request.contextPath}/cart" class="text-center m-0">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="mb-remove">Svuota carrello</button>
                </form>
            </div>
        </div>

    </div>

    <%
        }
    %>

</main>

<!-- ================= FOOTER (frammento) ================= -->
<%@ include file="Footer.jsp" %>

<!-- Bootstrap JS (locale) -->
<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
