<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%
    if (request.getAttribute("discountedProducts") == null) {
        // fase 1: sono il welcome file, non ho i dati → vai al controller
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
    List<ProductBean> discountedProducts = (List<ProductBean>) request.getAttribute("discountedProducts");
    List<ProductBean> popularProducts = (List<ProductBean>) request.getAttribute("popularProducts");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Miao &amp; Bau &mdash; Cibo e accessori bio per i tuoi animali</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="/view/Navbar.jsp" %>

<main class="container my-4">

    <!-- ================= HERO (riusa .mb-empty / .mb-empty-card) ================= -->
    <section class="mb-empty mb-5">
        <span class="mb-paw mb-paw-1">&#128062;</span>
        <span class="mb-paw mb-paw-2">&#128062;</span>
        <span class="mb-paw mb-paw-3">&#128062;</span>
        <span class="mb-paw mb-paw-4">&#128062;</span>
        <div class="mb-empty-card">
            <h1 class="mb-empty-title display-6">Tutto il meglio per i tuoi amici a quattro zampe</h1>
            <p class="mb-empty-text">Cibo naturale e accessori selezionati per cani, gatti e non solo. Prodotti bio, spedizioni veloci.</p>
            <a href="${pageContext.request.contextPath}/catalog" class="btn btn-mb-cta px-4 py-2">Esplora il catalogo</a>
        </div>
    </section>

    <!-- ================= CAROSELLO: IN OFFERTA ================= -->
    <section class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="h4 fw-bold m-0">In offerta</h2>
            <a href="${pageContext.request.contextPath}/catalog?filter=sale" class="fw-bold text-decoration-none">Visualizza tutti &rarr;</a>
        </div>

        <% if (discountedProducts != null && !discountedProducts.isEmpty()) { %>
        <div class="position-relative">
            <button type="button"
                    class="btn btn-light rounded-circle shadow-sm position-absolute top-50 start-0 translate-middle-y d-none d-md-flex align-items-center justify-content-center mb-scroll-btn"
                    style="width:2.75rem;height:2.75rem;z-index:2;margin-left:-.5rem;"
                    aria-label="Precedente" data-target="carSale" data-dir="-1">&#8249;</button>
            <button type="button"
                    class="btn btn-light rounded-circle shadow-sm position-absolute top-50 end-0 translate-middle-y d-none d-md-flex align-items-center justify-content-center mb-scroll-btn"
                    style="width:2.75rem;height:2.75rem;z-index:2;margin-right:-.5rem;"
                    aria-label="Successivo" data-target="carSale" data-dir="1">&#8250;</button>

            <div class="d-flex flex-nowrap overflow-auto gap-4 pb-2" id="carSale" style="scroll-snap-type:x mandatory;scrollbar-width:none;">
                <% for (ProductBean product : discountedProducts) { %>
                    <div class="flex-shrink-0" style="width:16rem;scroll-snap-align:start;">
                        <% String img = (product.getImage() == null) ? "" : product.getImage(); %>
                        <div class="mb-card h-100 d-flex flex-column">
                            <div class="mb-img-box<%= img.isEmpty() ? " mb-img-vuota" : "" %>">
                                <% if (!img.isEmpty()) { %>
                                <img src="${pageContext.request.contextPath}/img/products/<%= img %>"
                                     class="mb-card-img" alt="<%= product.getName() %>"
                                     onerror="this.onerror=null; this.style.display='none'; this.parentNode.classList.add('mb-img-vuota');">
                                <% } %>
                            </div>
                            <div class="p-3 d-flex flex-column flex-grow-1">
                                <div class="mb-2">
                                    <span class="badge mb-badge mb-badge-bio">Bio</span>
                                    <% if (product.isOnSale()) { %>
                                    <span class="badge mb-badge-sconto">-<%= product.getDiscountPercentage() %>%</span>
                                    <% } %>
                                </div>
                                <p class="mb-nome mb-0"><%= product.getName() %></p>
                                <p class="mb-marca mb-2"><%= product.getBrand() %></p>
                                <p class="mb-descrizione flex-grow-1"><%= product.getDescription() %></p>
                                <div class="mb-3">
                                    <% if (product.isOnSale()) { %>
                                    <span class="mb-prezzo mb-prezzo-sconto">&euro; <%= product.getDiscountedPrice() %></span>
                                    <span class="mb-prezzo-vecchio ms-1">&euro; <%= product.getPrice() %></span>
                                    <% } else { %>
                                    <span class="mb-prezzo">&euro; <%= product.getPrice() %></span>
                                    <% } %>
                                </div>
                                <div class="d-flex gap-2 mt-auto">
                                    <a href="${pageContext.request.contextPath}/product?productId=<%= product.getProductID() %>" class="btn btn-outline-secondary flex-fill">Dettagli</a>
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
                <% } %>
            </div>
        </div>
        <% } else { %>
            <p class="text-muted">Nessun prodotto in offerta al momento.</p>
        <% } %>
    </section>

    <!-- ================= CAROSELLO: PIU' POPOLARI ================= -->
    <section class="mb-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="h4 fw-bold m-0">I pi&ugrave; popolari</h2>
            <a href="${pageContext.request.contextPath}/catalog?filter=popular" class="fw-bold text-decoration-none">Visualizza tutti &rarr;</a>
        </div>

        <% if (popularProducts != null && !popularProducts.isEmpty()) { %>
        <div class="position-relative">
            <button type="button"
                    class="btn btn-light rounded-circle shadow-sm position-absolute top-50 start-0 translate-middle-y d-none d-md-flex align-items-center justify-content-center mb-scroll-btn"
                    style="width:2.75rem;height:2.75rem;z-index:2;margin-left:-.5rem;"
                    aria-label="Precedente" data-target="carPopular" data-dir="-1">&#8249;</button>
            <button type="button"
                    class="btn btn-light rounded-circle shadow-sm position-absolute top-50 end-0 translate-middle-y d-none d-md-flex align-items-center justify-content-center mb-scroll-btn"
                    style="width:2.75rem;height:2.75rem;z-index:2;margin-right:-.5rem;"
                    aria-label="Successivo" data-target="carPopular" data-dir="1">&#8250;</button>

            <div class="d-flex flex-nowrap overflow-auto gap-4 pb-2" id="carPopular" style="scroll-snap-type:x mandatory;scrollbar-width:none;">
                <% for (ProductBean product : popularProducts) { %>
                    <div class="flex-shrink-0" style="width:16rem;scroll-snap-align:start;">
                        <% String img = (product.getImage() == null) ? "" : product.getImage(); %>
                        <div class="mb-card h-100 d-flex flex-column">
                            <div class="mb-img-box<%= img.isEmpty() ? " mb-img-vuota" : "" %>">
                                <% if (!img.isEmpty()) { %>
                                <img src="${pageContext.request.contextPath}/img/products/<%= img %>"
                                     class="mb-card-img" alt="<%= product.getName() %>"
                                     onerror="this.onerror=null; this.style.display='none'; this.parentNode.classList.add('mb-img-vuota');">
                                <% } %>
                            </div>
                            <div class="p-3 d-flex flex-column flex-grow-1">
                                <div class="mb-2">
                                    <span class="badge mb-badge mb-badge-bio">Bio</span>
                                    <% if (product.isOnSale()) { %>
                                    <span class="badge mb-badge-sconto">-<%= product.getDiscountPercentage() %>%</span>
                                    <% } %>
                                </div>
                                <p class="mb-nome mb-0"><%= product.getName() %></p>
                                <p class="mb-marca mb-2"><%= product.getBrand() %></p>
                                <p class="mb-descrizione flex-grow-1"><%= product.getDescription() %></p>
                                <div class="mb-3">
                                    <% if (product.isOnSale()) { %>
                                    <span class="mb-prezzo mb-prezzo-sconto">&euro; <%= product.getDiscountedPrice() %></span>
                                    <span class="mb-prezzo-vecchio ms-1">&euro; <%= product.getPrice() %></span>
                                    <% } else { %>
                                    <span class="mb-prezzo">&euro; <%= product.getPrice() %></span>
                                    <% } %>
                                </div>
                                <div class="d-flex gap-2 mt-auto">
                                    <a href="${pageContext.request.contextPath}/product?productId=<%= product.getProductID() %>" class="btn btn-outline-secondary flex-fill">Dettagli</a>
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
                <% } %>
            </div>
        </div>
        <% } else { %>
            <p class="text-muted">Ancora nessun prodotto popolare: torna dopo i primi ordini!</p>
        <% } %>
    </section>

    <!-- ================= SFOGLIA PER ANIMALE (Cani / Gatti) ================= -->
    <section class="mb-5">
        <h2 class="h4 fw-bold mb-3">Sfoglia per animale</h2>
        <div class="row g-4">

            <!-- ---- CANI (species_id = 1) ---- -->
            <div class="col-12 col-md-6">
                <a href="${pageContext.request.contextPath}/catalog?speciesId=1"
                   class="mb-card d-block text-decoration-none position-relative overflow-hidden"
                   style="min-height:240px;background:#EAF3EC;">
                    <img src="${pageContext.request.contextPath}/img/categoria-cani.jpg"
                         alt="Prodotti per cani"
                         style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;"
                         onerror="this.onerror=null;this.style.display='none';">
                    <span style="position:absolute;inset:0;background:linear-gradient(90deg,rgba(0,0,0,.45) 0%,rgba(0,0,0,.05) 60%,transparent 100%);"></span>
                    <div class="position-absolute top-50 start-0 translate-middle-y ps-4 pe-3">
                        <span class="d-block text-white" style="font-family:'Nunito',sans-serif;font-weight:800;font-size:2.25rem;line-height:1;">CANI</span>
                        <span class="text-white">Scopri tutti i prodotti &rarr;</span>
                    </div>
                </a>
            </div>

            <!-- ---- GATTI (species_id = 2) ---- -->
            <div class="col-12 col-md-6">
                <a href="${pageContext.request.contextPath}/catalog?speciesId=2"
                   class="mb-card d-block text-decoration-none position-relative overflow-hidden"
                   style="min-height:240px;background:#FBEDE6;">
                    <img src="${pageContext.request.contextPath}/img/categoria-gatti.jpg"
                         alt="Prodotti per gatti"
                         style="position:absolute;inset:0;width:100%;height:100%;object-fit:cover;"
                         onerror="this.onerror=null;this.style.display='none';">
                    <span style="position:absolute;inset:0;background:linear-gradient(90deg,rgba(0,0,0,.45) 0%,rgba(0,0,0,.05) 60%,transparent 100%);"></span>
                    <div class="position-absolute top-50 start-0 translate-middle-y ps-4 pe-3">
                        <span class="d-block text-white" style="font-family:'Nunito',sans-serif;font-weight:800;font-size:2.25rem;line-height:1;">GATTI</span>
                        <span class="text-white">Scopri tutti i prodotti &rarr;</span>
                    </div>
                </a>
            </div>

        </div>
    </section>

</main>

<%@ include file="/view/Footer.jsp" %>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Scorrimento dei caroselli con le frecce (di ~una card + gap alla volta).
    document.querySelectorAll('.mb-scroll-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var track = document.getElementById(btn.getAttribute('data-target'));
            if (!track) return;
            var item = track.querySelector(':scope > div');
            var step = (item ? item.getBoundingClientRect().width : track.clientWidth * 0.8) + 24;
            track.scrollBy({ left: parseInt(btn.getAttribute('data-dir'), 10) * step, behavior: 'smooth' });
        });
    });
</script>
</body>
</html>
