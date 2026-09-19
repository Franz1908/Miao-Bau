// admin-insert.js — validazione client del form di inserimento di un nuovo prodotto.

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
// @type serve solo all'editor per l'autocompletamento su .value/.validity

/** @type {HTMLInputElement} **/
const name = document.getElementById("name");
/** @type {HTMLInputElement} **/
const brand = document.getElementById("brand");
/** @type {HTMLInputElement} **/
const description = document.getElementById("description");
/** @type {HTMLInputElement} **/
const category = document.getElementById("categoryId");
/** @type {HTMLInputElement} **/
const specie = document.getElementById("speciesId");
/** @type {HTMLInputElement} **/
const image = document.getElementById("image");
/** @type {HTMLInputElement} **/
const price = document.getElementById("price");
/** @type {HTMLInputElement} **/
const vat = document.getElementById("vat");
/** @type {HTMLInputElement} **/
const onSale = document.getElementById("onSale");
/** @type {HTMLInputElement} **/
const discountPercentage = document.getElementById("discountPercentage");
/** @type {HTMLInputElement} **/
const weight = document.getElementById("weight");
/** @type {HTMLFormElement} **/
const productInsertForm = document.getElementById("productInsertForm");


// --- Una funzione di validazione per campo ---
// Ognuna valida il proprio campo, aggiorna la lista errori
// e restituisce true (valido) / false (non valido) per il submit.

function nameValidation() {
    // pulisco entrambi i possibili messaggi prima di ricontrollare
    setError("Inserire il nome del prodotto", true);
    setError("Il nome è troppo lungo. Max 150 caratteri", true);

    // se rispetta le regole HTML (required, maxlength) è valido: esco
    if (name.checkValidity()) return true;

    // altrimenti distinguo il tipo di errore per mostrare il messaggio giusto
    if (name.validity.valueMissing) {           // campo vuoto (required)
        setError("Inserire il nome del prodotto", false);
    }
    else if (name.validity.tooLong) {           // oltre maxlength
        setError("Il nome è troppo lungo. Max 150 caratteri", false)
    }

    return false;
}

function brandValidation() {
    // pulisco entrambi i messaggi
    setError("Inserire la marca del prodotto", true);
    setError("La marca del prodotto è troppo lunga. Max 50 caratteri", true);

    if (brand.checkValidity()) return true;

    if (brand.validity.valueMissing) {          // vuoto (required)
        setError("Inserire la marca del prodotto", false);
    }
    else if (brand.validity.tooLong) {          // oltre maxlength
        setError("La marca del prodotto è troppo lunga. Max 50 caratteri", false);
    }

    return false;
}

function descriptionValidation() {
    // pulisco entrambi i messaggi
    setError("Inserire la descrizione del prodotto", true);
    setError("La descrizione è troppo lunga. Max 2500 caratteri", true);

    if (description.checkValidity()) return true;

    if (description.validity.valueMissing) {    // vuota (required)
        setError("Inserire la descrizione del prodotto", false);
    }
    else if (description.validity.tooLong) {    // oltre maxlength
        setError("La descrizione è troppo lunga. Max 2500 caratteri", false);
    }

    return false;
}

// Categoria e specie sono <select> con una sola regola (required)
function categoryValidation() {
    // checkValidity() applica le regole HTML del campo (qui: required)
    const isValid = category.checkValidity();
    // se valido rimuove il messaggio, se non valido lo aggiunge
    setError("Inserire la categoria del prodotto", isValid);
    return isValid;
}

function specieValidation() {
    const isValid = specie.checkValidity();
    setError("Inserire una specie per il prodotto", isValid);
    return isValid;
}

function priceValidation() {
    // pulisco entrambi i messaggi
    setError("Inserire il prezzo del prodotto", true);
    setError("Inserire un prezzo valido per il prodotto", true);

    if (price.checkValidity()) return true;

    if (price.validity.valueMissing)            // vuoto (required)
        setError("Inserire il prezzo del prodotto", false);
    // sotto il minimo, non numerico, o non multiplo dello step
    else if (price.validity.rangeUnderflow || price.validity.badInput || price.validity.stepMismatch)
        setError("Inserire un prezzo valido per il prodotto", false);

    return false;
}

function vatValidation() {
    // pulisco entrambi i messaggi
    setError("Inserire un'IVA per il prodotto", true);
    setError("Inserire un'IVA valida per il prodotto", true);

    if (vat.checkValidity()) return true;

    if (vat.validity.valueMissing) {            // vuoto (required)
        setError("Inserire un'IVA per il prodotto", false);
    }
    // non numerico, o fuori dall'intervallo 0–100
    else if (vat.validity.badInput || vat.validity.rangeUnderflow || vat.validity.rangeOverflow) {
        setError("Inserire un'IVA valida per il prodotto", false);
    }

    return false;
}

function discountPercentageValidation() {
    // pulisco entrambi i messaggi
    setError("Inserire uno sconto per il prodotto", true);
    setError("Inserire uno sconto valido per il prodotto", true);

    // lo sconto si valida SOLO se il prodotto è in sconto
    // se non lo è, il campo è disabilitato e non c'è nulla da controllare
    if (onSale.checked) {
        if (discountPercentage.checkValidity()) return true;

        if (discountPercentage.validity.valueMissing) {   // vuoto ma richiesto (in sconto)
            setError("Inserire uno sconto per il prodotto", false);
        }
        // non numerico o fuori dall'intervallo 0–100
        else if (discountPercentage.validity.badInput || discountPercentage.validity.rangeOverflow || discountPercentage.validity.rangeUnderflow) {
            setError("Inserire uno sconto valido per il prodotto", false);
        }

        return false;
    }

    // non in sconto: sempre valido
    return true;
}

function weightValidation() {
    // pulisco il messaggio
    setError("Inserire un peso valido per il prodotto", true);
    // campo facoltativo: se vuoto è valido, non controllo altro
    if (weight.value === "") return true;
    // compilato e valido
    if (weight.checkValidity()) return true;
    // compilato ma invalido (sotto il minimo o non numerico)
    setError("Inserire un peso valido per il prodotto", false);
    return false;
}


// --- Sincronizzazione del campo sconto con la checkbox "in sconto" ---
function syncDiscount() {
    // disabilita il campo discountPercentage se il prodotto non è in sconto
    discountPercentage.disabled = !onSale.checked;
    // pulisce il valore che era stato inserito nel campo discountPercentage se onSale
    // viene disabilitato in un secondo momento
    if (!onSale.checked) discountPercentage.value = "";
}

onSale.addEventListener("change", syncDiscount);
// chiamata iniziale: imposta lo stato giusto al caricamento
syncDiscount();


// --- Aggancio agli eventi

// blur = validazione "dal vivo": ogni campo si controlla appena l'utente lo lascia
name.addEventListener("blur", nameValidation);
brand.addEventListener("blur", brandValidation);
description.addEventListener("blur", descriptionValidation);
category.addEventListener("blur", categoryValidation);
specie.addEventListener("blur", specieValidation);
price.addEventListener("blur", priceValidation);
vat.addEventListener("blur", vatValidation);
discountPercentage.addEventListener("blur", discountPercentageValidation);
weight.addEventListener("blur", weightValidation);


// submit = controllo finale: rivalido TUTTI i campi, anche quelli
// su cui l'utente non è mai passato (il loro blur non è mai scattato).
productInsertForm.addEventListener("submit", evt => {

    // chiamo tutte le funzioni PRIMA e salvo i risultati così ognuna
    // esegue e mostra il proprio errore
    const okName = nameValidation();
    const okBrand = brandValidation();
    const okDescription = descriptionValidation();
    const okCategory = categoryValidation();
    const okSpecie = specieValidation();
    const okPrice = priceValidation();
    const okVat = vatValidation();
    const okDiscountPercentage = discountPercentageValidation();
    const okWeight = weightValidation();

    // se anche un solo campo è invalido, blocco l'invio;
    // se sono tutti validi non chiamo preventDefault e il form parte
    if (!okName || !okBrand || ! okDescription || !okCategory || !okSpecie || !okPrice || !okVat || !okDiscountPercentage || !okWeight) {
        evt.preventDefault();
    }
})