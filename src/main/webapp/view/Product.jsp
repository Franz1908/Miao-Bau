<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%
    ProductBean product = (ProductBean) request.getAttribute("product");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>
        <%= (product != null) ? product.getName() : "Prodotto" %> — Miao &amp; Bau
    </title>

    <!-- Bootstrap 5 (locale) + foglio di stile del brand -->
    <link rel="stylesheet" href="<%= ctx %>/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>

<!-- ================= NAVBAR (frammento) ================= -->
<%@ include file="Navbar.jsp" %>

<!-- ================= CONTENUTO ================= -->
<main class="container my-4">

    <%
        if (product == null) {
    %>
    <%
        // Prodotto non disponibile: mostra lo stato vuoto con bottone al catalogo
        request.setAttribute("esTitle", "Prodotto non disponibile");
        request.setAttribute("esText", "Il prodotto che cerchi non è più disponibile o non esiste. Dai un'occhiata agli altri prodotti del catalogo.");
        request.setAttribute("esImg", ctx + "/img/catalogo-vuoto.png");
        request.setAttribute("esLink", ctx + "/catalog");
        request.setAttribute("esCta", "Torna al catalogo");
    %>
    <%@ include file="error/EmptyState.jsp" %>
    <%
    } else {
    %>

    <!-- Breadcrumb semplice per tornare al catalogo -->
    <nav class="mb-3">
        <a href="<%= ctx %>/catalog" style="text-decoration:none; color:#2D6A4F; font-weight:600;">
            &larr; Torna al catalogo
        </a>
    </nav>

    <div class="mb-card p-3 p-md-4">
        <div class="row g-4 align-items-center">

            <!-- Colonna immagine -->
            <div class="col-12 col-md-6">
                <%
                    String img = (product.getImage() == null) ? "" : product.getImage();
                %>
                <div class="mb-img-box<%= img.isEmpty() ? " mb-img-vuota" : "" %>"
                     style="height:360px; border-radius:.75rem;">
                    <% if (!img.isEmpty()) { %>
                    <img src="<%= ctx %>/images/<%= img %>"
                         class="mb-card-img"
                         alt="<%= product.getName() %>"
                         onerror="this.onerror=null; this.style.display='none'; this.parentNode.classList.add('mb-img-vuota');">
                    <% } %>
                </div>
            </div>

            <!-- Colonna dettagli -->
            <div class="col-12 col-md-6">

                <!-- Badge categoria / sconto -->
                <div class="mb-2">
                    <span class="badge mb-badge mb-badge-bio">Bio</span>
                    <% if (product.isOnSale()) { %>
                    <span class="badge mb-badge-sconto">
                                    -<%= product.getDiscountPercentage() %>%
                                </span>
                    <% } %>
                </div>

                <!-- Nome e marca -->
                <h1 class="mb-1"><%= product.getName() %></h1>
                <p class="mb-marca mb-3" style="color:#6c757d;">
                    Marca: <%= product.getBrand() %>
                </p>

                <!-- Descrizione -->
                <p class="mb-descrizione mb-4">
                    <%= product.getDescription() %>
                </p>

                <!-- Prezzo -->
                <div class="mb-4">
                    <% if (product.isOnSale()) { %>
                    <span class="mb-prezzo mb-prezzo-sconto" style="font-size:1.8rem;">
                                    &euro; <%= product.getDiscountedPrice() %>
                                </span>
                    <span class="mb-prezzo-vecchio ms-2">
                                    &euro; <%= product.getPrice() %>
                                </span>
                    <% } else { %>
                    <span class="mb-prezzo" style="font-size:1.8rem;">
                                    &euro; <%= product.getPrice() %>
                                </span>
                    <% } %>
                </div>

                <!-- Form: quantità + aggiungi al carrello -->
                <form method="post" action="<%= ctx %>/cart"
                      class="d-flex align-items-end gap-3">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                    <div>
                        <label for="quantity" class="form-label mb-1">Quantità</label>
                        <input type="number" id="quantity" name="quantity"
                               class="form-control" style="width:100px;"
                               min="1" value="1" step="1">
                    </div>
                    <button type="submit" class="btn btn-mb-cta btn-lg">
                        Aggiungi al carrello
                    </button>
                </form>

            </div>
        </div>

        <!-- ---- Scheda tecnica: mostra solo i campi valorizzati ---- -->
        <hr class="my-4" style="border-color:#E9ECEF;">
        <h4 class="mb-3">Scheda tecnica</h4>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

            <% if (product.getWeight() != null) { %>
            <div class="col">
                <div class="p-3" style="background:#FAF6F0; border-radius:.6rem;">
                    <span class="mb-marca d-block">Peso</span>
                    <strong><%= product.getWeight() %> kg</strong>
                </div>
            </div>
            <% } %>

            <% if (product.getSize() != null && !product.getSize().isEmpty()) { %>
            <div class="col">
                <div class="p-3" style="background:#FAF6F0; border-radius:.6rem;">
                    <span class="mb-marca d-block">Taglia</span>
                    <strong><%= product.getSize() %></strong>
                </div>
            </div>
            <% } %>

            <% if (product.getColor() != null && !product.getColor().isEmpty()) { %>
            <div class="col">
                <div class="p-3" style="background:#FAF6F0; border-radius:.6rem;">
                    <span class="mb-marca d-block">Colore</span>
                    <strong><%= product.getColor() %></strong>
                </div>
            </div>
            <% } %>

            <% if (product.getMaterial() != null && !product.getMaterial().isEmpty()) { %>
            <div class="col">
                <div class="p-3" style="background:#FAF6F0; border-radius:.6rem;">
                    <span class="mb-marca d-block">Materiale</span>
                    <strong><%= product.getMaterial() %></strong>
                </div>
            </div>
            <% } %>

            <% if (product.getIngredients() != null && !product.getIngredients().isEmpty()) { %>
            <div class="col">
                <div class="p-3" style="background:#FAF6F0; border-radius:.6rem;">
                    <span class="mb-marca d-block">Ingredienti</span>
                    <strong><%= product.getIngredients() %></strong>
                </div>
            </div>
            <% } %>

        </div>
    </div>

    <%
        }
    %>

</main>

<!-- ================= FOOTER (frammento) ================= -->
<%@ include file="Footer.jsp" %>

<!-- Bootstrap JS (locale) -->
<script src="<%= ctx %>/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
