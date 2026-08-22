<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CategoryBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%@ page import="java.util.List" %>
<%
    ProductBean product = (ProductBean) request.getAttribute("product");
    List<CategoryBean> categories = (List<CategoryBean>) application.getAttribute("categories");
    List<SpeciesBean> species = (List<SpeciesBean>) application.getAttribute("species");
%>
<html>
<head>
    <title>Modifica prodotto</title>
</head>
<body>
    <h1>Modifica prodotto</h1>

    <%
        if (product == null) {
    %>
        <p>Ci dispiace, il prodotto cercato è inesistente</p>
    <%
        } else {
    %>

        <form method="post" action="${pageContext.request.contextPath}/admin/product/update">

            <input type="hidden" name="productId" value="<%= product.getProductID() %>">

            <label for="name">Nome</label>
            <input type="text" name="name" id="name" value="<%= product.getName() %>" required>
            <br>

            <label for="brand">Marca</label>
            <input type="text" name="brand" id="brand" value="<%= product.getBrand() %>" required>
            <br>

            <label for="description">Descrizione</label>
            <textarea name="description" id="description" required><%= product.getDescription() %></textarea>
            <br>

            <label for="categoryId">Categoria</label>
            <select name="categoryId" id="categoryId" required>
                <% for (CategoryBean category : categories) { %>
                    <option value="<%= category.getCategoryID() %>"
                        <%= category.getCategoryID() == product.getCategoryID() ? "selected" : "" %>>
                        <%= category.getCategoryName() %>
                    </option>
                <% } %>
            </select>
            <br>

            <label for="speciesId">Specie</label>
            <select name="speciesId" id="speciesId" required>
                <% for (SpeciesBean specie : species) { %>
                    <option value="<%= specie.getSpeciesID() %>"
                        <%= specie.getSpeciesID() == product.getSpeciesID() ? "selected" : "" %>>
                        <%= specie.getSpeciesName() %>
                    </option>
                <% } %>
            </select>
            <br>

            <label for="price">Prezzo</label>
            <input type="number" name="price" id="price" step="0.01" min="0" value="<%= product.getPrice() %>" required>
            <br>

            <label for="vat">IVA (%)</label>
            <input type="number" name="vat" id="vat" step="0.01" min="0" value="<%= product.getVat() %>" required>
            <br>

            <label for="onSale">In sconto</label>
            <input type="checkbox" name="onSale" id="onSale" value="true" <%= product.isOnSale() ? "checked" : "" %>>
            <br>

            <label for="discountPercentage">Percentuale sconto</label>
            <input type="number" name="discountPercentage" id="discountPercentage" step="0.01" min="0" max="100"
                   value="<%= product.getDiscountPercentage() != null ? product.getDiscountPercentage() : "" %>">
            <br>

            <label for="image">Immagine (nome file)</label>
            <input type="text" name="image" id="image"
                   value="<%= product.getImage() != null ? product.getImage() : "" %>">
            <br>

            <label for="weight">Peso (kg)</label>
            <input type="number" name="weight" id="weight" step="0.01" min="0"
                   value="<%= product.getWeight() != null ? product.getWeight() : "" %>">
            <br>

            <label for="ingredients">Ingredienti</label>
            <textarea name="ingredients" id="ingredients"><%= product.getIngredients() != null ? product.getIngredients() : "" %></textarea>
            <br>

            <label for="size">Taglia</label>
            <input type="text" name="size" id="size"
                   value="<%= product.getSize() != null ? product.getSize() : "" %>">
            <br>

            <label for="color">Colore</label>
            <input type="text" name="color" id="color"
                   value="<%= product.getColor() != null ? product.getColor() : "" %>">
            <br>

            <label for="material">Materiale</label>
            <input type="text" name="material" id="material"
                   value="<%= product.getMaterial() != null ? product.getMaterial() : "" %>">
            <br>

            <button type="submit">Salva modifiche</button>
        </form>
    <%
        }
    %>
</body>
</html>
