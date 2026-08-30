<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%
    // Dati passati dalla Servlet
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    List<SpeciesBean> speciesFiltri = (List<SpeciesBean>) application.getAttribute("species");
    String title = (String) request.getAttribute("title");
    if (title == null) title = "Catalogo prodotti";
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= title %> — Miao &amp; Bau</title>

    <!-- Bootstrap 5 (locale) + foglio di stile del brand -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- ================= NAVBAR (frammento) ================= -->
<%@ include file="Navbar.jsp" %>

<!-- ================= CONTENUTO ================= -->
<main class="container my-4">

    <!-- Titolo della pagina -->
    <h1 class="mb-1"><%= title %></h1>
    <p class="text-muted mb-4">Prodotti naturali per i tuoi amici a quattro zampe.</p>

    <!-- ---- Filtri: pillole per specie ---- -->
    <div class="d-flex flex-wrap gap-2 mb-4">
        <!-- "Tutti" riporta al catalogo completo -->
        <a href="${pageContext.request.contextPath}/catalog" class="mb-filtro attivo">Tutti</a>
        <%
            // Una pillola per ogni specie (Cani, Gatti, Altri animali...)
            if (speciesFiltri != null) {
                for (SpeciesBean specie : speciesFiltri) {
        %>
        <a href="${pageContext.request.contextPath}/catalog?speciesId=<%= specie.getSpeciesID() %>" class="mb-filtro">
            <%= specie.getSpeciesName() %>
        </a>
        <%
                }
            }
        %>
    </div>

    <!-- ---- Griglia prodotti ----
         row-cols-* controlla quante card per riga a seconda dello schermo:
         1 su mobile, 2 su tablet, 3 su desktop, 4 su schermi grandi. -->
    <%
        if (products == null || products.isEmpty()) {
    %>
    <%
        // Catalogo vuoto: mostra lo stato vuoto riutilizzabile (EmptyState.jsp)
        request.setAttribute("esTitle", "Nessun prodotto disponibile");
        request.setAttribute("esText", "Al momento non ci sono prodotti in questa sezione. Torna a trovarci presto!");
        request.setAttribute("esImg", request.getContextPath() + "/img/catalogo-vuoto.png");
    %>
    <%@ include file="error/EmptyState.jsp" %>
    <%
    } else {
    %>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 row-cols-xl-4 g-4">
        <%
            // ====== INIZIO LOOP PRODOTTI ======
            for (ProductBean product : products) {
                // Nome immagine dal bean (vuoto se non impostato)
                String img = (product.getImage() == null) ? "" : product.getImage();
        %>
        <div class="col">
            <div class="mb-card h-100 d-flex flex-column">

                <!-- Immagine prodotto.
                     Se il nome immagine e' vuoto mostro subito il
                     segnaposto CSS. Se l'immagine esiste ma non carica, l'onerror
                     nasconde l'<img> e attiva il segnaposto CSS -->
                <div class="mb-img-box<%= img.isEmpty() ? " mb-img-vuota" : "" %>">
                    <% if (!img.isEmpty()) { %>
                    <img src="${pageContext.request.contextPath}/img/products/<%= img %>"
                         class="mb-card-img"
                         alt="<%= product.getName() %>"
                         onerror="this.onerror=null; this.style.display='none'; this.parentNode.classList.add('mb-img-vuota');">
                    <% } %>
                </div>

                <div class="p-3 d-flex flex-column flex-grow-1">

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
                    <p class="mb-nome mb-0"><%= product.getName() %></p>
                    <p class="mb-marca mb-2"><%= product.getBrand() %></p>

                    <!-- Descrizione breve -->
                    <p class="mb-descrizione flex-grow-1"><%= product.getDescription() %></p>

                    <!-- Prezzo -->
                    <div class="mb-3">
                        <% if (product.isOnSale()) { %>
                        <span class="mb-prezzo mb-prezzo-sconto">&euro; <%= product.getDiscountedPrice() %></span>
                        <span class="mb-prezzo-vecchio ms-1">&euro; <%= product.getPrice() %></span>
                        <% } else { %>
                        <span class="mb-prezzo">&euro; <%= product.getPrice() %></span>
                        <% } %>
                    </div>

                    <!-- Azioni: dettaglio + aggiungi al carrello -->
                    <div class="d-flex gap-2 mt-auto">
                        <a href="${pageContext.request.contextPath}/product?productId=<%= product.getProductID() %>"
                           class="btn btn-outline-secondary flex-fill">Dettagli</a>
                        <form method="post" action="${pageContext.request.contextPath}/cart" class="flex-fill m-0">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit" class="btn btn-mb-cta w-100">Aggiungi</button>
                        </form>
                    </div>

                </div>
            </div>
        </div>
        <%
            } // ====== FINE LOOP PRODOTTI ======
        %>
    </div>
    <%
        }
    %>

</main>

<!-- ================= FOOTER (frammento) ================= -->
<%@ include file="Footer.jsp" %>

<!-- Bootstrap JS (locale) per menu a tendina e navbar responsive -->
<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
