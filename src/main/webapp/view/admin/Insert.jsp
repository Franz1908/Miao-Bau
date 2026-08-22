<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CategoryBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%@ page import="java.util.List" %>
<%
    List<CategoryBean> categories = (List<CategoryBean>) application.getAttribute("categories");
    List<SpeciesBean> species = (List<SpeciesBean>) application.getAttribute("species");
    List<String> errorMessage = (List<String>) request.getAttribute("errorMessage");
%>
<html>
<head>
    <title>Aggiunta prodotto</title>
</head>
<body>
    <h1>Aggiungi prodotto</h1>
    <%
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
        <p><%=errorMessage%></p>
    <%
        }
    %>
    <form method="post" action="${pageContext.request.contextPath}/admin/product/insert">

        <label for="name">Nome</label>
        <input type="text" name="name" id="name" required>
        <br>

        <label for="brand">Marca</label>
        <input type="text" name="brand" id="brand" required>
        <br>

        <label for="description">Descrizione</label>
        <textarea name="description" id="description" required></textarea>
        <br>

        <label for="categoryId">Categoria</label>
        <select name="categoryId" id="categoryId" required>
            <% for (CategoryBean category : categories) { %>
            <option value="<%= category.getCategoryID() %>"><%= category.getCategoryName() %></option>
            <% } %>
        </select>
        <br>

        <label for="speciesId">Specie</label>
        <select name="speciesId" id="speciesId" required>
            <% for (SpeciesBean specie : species) { %>
            <option value="<%= specie.getSpeciesID() %>"><%= specie.getSpeciesName() %></option>
            <% } %>
        </select>
        <br>

        <label for="price">Prezzo</label>
        <input type="number" name="price" id="price" step="0.01" min="0" required>
        <br>

        <label for="vat">IVA (%)</label>
        <input type="number" name="vat" id="vat" step="0.01" min="0" required>
        <br>

        <label for="onSale">In sconto</label>
        <input type="checkbox" name="onSale" id="onSale" value="true">
        <br>

        <label for="discountPercentage">Percentuale sconto</label>
        <input type="number" name="discountPercentage" id="discountPercentage" step="0.01" min="0" max="100">
        <br>

        <label for="image">Immagine (nome file)</label>
        <input type="text" name="image" id="image">
        <br>

        <label for="weight">Peso (kg)</label>
        <input type="number" name="weight" id="weight" step="0.01" min="0">
        <br>

        <label for="ingredients">Ingredienti</label>
        <textarea name="ingredients" id="ingredients"></textarea>
        <br>

        <label for="size">Taglia</label>
        <input type="text" name="size" id="size">
        <br>

        <label for="color">Colore</label>
        <input type="text" name="color" id="color">
        <br>

        <label for="material">Materiale</label>
        <input type="text" name="material" id="material">
        <br>

        <button type="submit">Salva prodotto</button>
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/admin/home">Torna indietro</a>
</body>
</html>
