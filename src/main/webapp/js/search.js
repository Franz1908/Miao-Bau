// search.js — ricerca prodotti con suggerimenti live (autocomplete via AJAX).

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
/** @type {HTMLInputElement} **/
const searchBar = document.getElementById("searchBar");           // campo di ricerca
/** @type {HTMLElement} **/
const searchResults = document.getElementById("searchResults");        // box tendina (parte con d-none)
/** @type {HTMLUListElement} **/
const searchResultsList = document.getElementById("searchResultsList");  // lista <ul> da riempire

// reagisco mentre l'utente scrive
searchBar.addEventListener("keyup", async () => {
    // svuoto sempre la lista prima: ogni ricerca ricostruisce da zero,
    // così i risultati vecchi non si accumulano
    searchResultsList.innerHTML = "";

    // testo troppo corto: non interrogo il server, nascondo la tendina ed esco
    if (searchBar.value.length < 2) {
        searchResults.classList.add("d-none");
        return;
    }

    try {
        // chiamo la servlet passando il testo come parametro
        const result = await fetch("search?q=" + encodeURIComponent(searchBar.value));

        // fetch NON fallisce sugli errori HTTP, controllo io lo stato
        if (!result.ok) {
            throw new Error("HTTP error" + result.status);
        }

        // la risposta è un array di oggetti { id, name }
        /** @type {Array} **/
        const data = await result.json();

        // se non c'è risultato allora mostro un messaggio
        if (data.length === 0) {
            const li = document.createElement("li");
            li.textContent = "Nessun prodotto trovato";
            li.className = "list-group-item";
            searchResultsList.appendChild(li);
            searchResults.classList.remove("d-none");
            return;
        }

        // per ogni prodotto creo una voce cliccabile nella tendina
        data.forEach((product) => {
            const li = document.createElement("li");
            li.className = "list-group-item";
            li.textContent = product.name;
            // al click porto l'utente alla scheda del prodotto
            li.addEventListener("click", () => {
                window.location.href = "product?productId=" + product.id;
            });
            searchResultsList.appendChild(li);
        })

        // risultati pronti: mostro la tendina
        searchResults.classList.remove("d-none");
    }
    catch (error) {
        // fallimento silenzioso, se la ricerca non è disponibile non disturbo l'utente
        console.error("Ricerca prodotto non disponibile", error);
    }

})
