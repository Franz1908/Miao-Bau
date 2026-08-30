<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CategoryBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%@ page import="java.util.List" %>
<%
    List<CategoryBean> categories = (List<CategoryBean>) application.getAttribute("categories");
    List<SpeciesBean> species = (List<SpeciesBean>) application.getAttribute("species");
    List<String> errorMessage = (List<String>) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Aggiungi prodotto &mdash; Admin Miao &amp; Bau</title>
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
    <a href="${pageContext.request.contextPath}/admin/catalog" class="text-decoration-none d-inline-block mb-3">&larr; Torna al catalogo</a>
    <h1 class="mb-4">Aggiungi prodotto</h1>

    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="mb-alert mb-alert-error">
            <ul>
                <% for (String err : errorMessage) { %>
                    <li><%= err %></li>
                <% } %>
            </ul>
        </div>
    <% } %>

    <form class="mb-form" method="post" action="${pageContext.request.contextPath}/admin/product/insert">
        <div class="row g-4">

            <!-- Dati principali -->
            <div class="col-12 col-lg-7">
                <div class="mb-panel">
                    <h2 class="mb-panel-title mb-3">Dati principali</h2>
                    <div class="row g-3">
                        <div class="col-12 col-md-8">
                            <label for="name" class="form-label">Nome</label>
                            <input type="text" class="form-control" name="name" id="name" maxlength="150" required>
                        </div>
                        <div class="col-12 col-md-4">
                            <label for="brand" class="form-label">Marca</label>
                            <input type="text" class="form-control" name="brand" id="brand" maxlength="50" required>
                        </div>
                        <div class="col-12">
                            <label for="description" class="form-label">Descrizione</label>
                            <textarea class="form-control" name="description" id="description" rows="3" maxlength="2500" required></textarea>
                        </div>
                        <div class="col-12 col-md-6">
                            <label for="categoryId" class="form-label">Categoria</label>
                            <select class="form-select" name="categoryId" id="categoryId" required>
                                <option value="" selected disabled>Seleziona&hellip;</option>
                                <% for (CategoryBean category : categories) { %>
                                <option value="<%= category.getCategoryID() %>"><%= category.getCategoryName() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-12 col-md-6">
                            <label for="speciesId" class="form-label">Specie</label>
                            <select class="form-select" name="speciesId" id="speciesId" required>
                                <option value="" selected disabled>Seleziona&hellip;</option>
                                <% for (SpeciesBean specie : species) { %>
                                <option value="<%= specie.getSpeciesID() %>"><%= specie.getSpeciesName() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="col-12">
                            <label for="image" class="form-label">Immagine (nome file)</label>
                            <input type="text" class="form-control" name="image" id="image" placeholder="es. crocchette_pollo.jpg">
                            <div class="form-text">Il file va caricato in <code>img/products/</code>.</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Prezzo & sconto -->
            <div class="col-12 col-lg-5">
                <div class="mb-panel">
                    <h2 class="mb-panel-title mb-3">Prezzo &amp; sconto</h2>
                    <div class="row g-3">
                        <div class="col-6">
                            <label for="price" class="form-label">Prezzo (&euro;)</label>
                            <input type="number" class="form-control" name="price" id="price" step="0.01" min="0" required>
                        </div>
                        <div class="col-6">
                            <label for="vat" class="form-label">IVA (%)</label>
                            <input type="number" class="form-control" name="vat" id="vat" step="0.01" min="0" max="100" required>
                        </div>
                        <div class="col-12">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="onSale" id="onSale" value="true">
                                <label class="form-check-label" for="onSale">Prodotto in sconto</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <label for="discountPercentage" class="form-label">Percentuale sconto (%)</label>
                            <input type="number" class="form-control" name="discountPercentage" id="discountPercentage" step="0.01" min="0" max="100" disabled>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Dettagli opzionali -->
            <div class="col-12">
                <div class="mb-panel">
                    <h2 class="mb-panel-title mb-1">Dettagli</h2>
                    <p class="form-text mb-3">Compila i campi pertinenti: peso e ingredienti per i cibi, taglia/colore/materiale per gli accessori.</p>
                    <div class="row g-3">
                        <div class="col-12 col-md-4">
                            <label for="weight" class="form-label">Peso (kg)</label>
                            <input type="number" class="form-control" name="weight" id="weight" step="0.01" min="0">
                        </div>
                        <div class="col-12 col-md-4">
                            <label for="size" class="form-label">Taglia</label>
                            <input type="text" class="form-control" name="size" id="size">
                        </div>
                        <div class="col-12 col-md-4">
                            <label for="color" class="form-label">Colore</label>
                            <input type="text" class="form-control" name="color" id="color">
                        </div>
                        <div class="col-12 col-md-8">
                            <label for="ingredients" class="form-label">Ingredienti</label>
                            <textarea class="form-control" name="ingredients" id="ingredients" rows="2" maxlength="2500"></textarea>
                        </div>
                        <div class="col-12 col-md-4">
                            <label for="material" class="form-label">Materiale</label>
                            <input type="text" class="form-control" name="material" id="material">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="d-flex flex-wrap gap-2 mt-4">
            <button type="submit" class="btn btn-mb-cta px-4 py-2">Salva prodotto</button>
            <a href="${pageContext.request.contextPath}/admin/catalog" class="btn btn-mb-primario px-4 py-2">Annulla</a>
        </div>
    </form>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Abilita la percentuale di sconto solo se "in sconto" e' spuntato.
    (function () {
        var chk = document.getElementById('onSale');
        var disc = document.getElementById('discountPercentage');
        function sync() { disc.disabled = !chk.checked; if (!chk.checked) disc.value = ''; }
        chk.addEventListener('change', sync);
        sync();
    })();
</script>
</body>
</html>
