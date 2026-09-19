// new-address.js — validazione client del form di inserimento dell'indirizzo.

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
// @type serve solo all'editor per l'autocompletamento su .value/.validity

/** @type {HTMLInputElement} **/
const street = document.getElementById("street");
/** @type {HTMLInputElement} **/
const civicNumber = document.getElementById("civicNumber");
/** @type {HTMLInputElement} **/
const postalCode = document.getElementById("postalCode");
/** @type {HTMLInputElement} **/
const city = document.getElementById("city");
/** @type {HTMLInputElement} **/
const country = document.getElementById("country");
/** @type {HTMLFormElement} **/
const newAddressForm = document.getElementById("newAddressForm");

// --- Una funzione di validazione per campo ---
// Ognuna valida il proprio campo, aggiorna la lista errori
// e restituisce true (valido) / false (non valido) per il submit.

function streetValidation() {
    // checkValidity() applica le regole HTML del campo (qui: required)
    const isValid = street.checkValidity();
    // se valido rimuove il messaggio, se non valido lo aggiunge
    setError("Inserire una via", isValid);
    return isValid;
}

function civicNumberValidation() {
    const isValid = civicNumber.checkValidity();
    setError("Inserire un numero civico", isValid);
    return isValid;
}

function postalCodeValidation() {
    // pulisco sempre tutti i messaggi (isValid=true = rimuovi)
    setError("Inserire un CAP", true);
    setError("Inserire un CAP valido", true);

    // se rispetta tutte le regole HTML è valido: esco subito
    if (postalCode.checkValidity()) return true;

    // altrimenti capisco perché è invalida e mostro il messaggio giusto
    if (postalCode.validity.valueMissing) {         // campo vuoto (required)
        setError("Inserire un CAP", false);
    }
    else if (postalCode.validity.patternMismatch) {     // formato errato
        setError("Inserire un CAP valido", false);
    }

    return false;
}

function cityValidation() {
    const isValid = city.checkValidity();
    setError("Inserire una città", isValid);
    return isValid;
}

function countryValidation() {
    const isValid = country.checkValidity();
    setError("Inserire un paese", isValid);
    return isValid;
}

// --- Aggancio agli eventi ---

// blur = validazione "dal vivo": ogni campo si controlla appena l'utente lo lascia
street.addEventListener("blur", streetValidation);
civicNumber.addEventListener("blur", civicNumberValidation);
postalCode.addEventListener("blur", postalCodeValidation);
city.addEventListener("blur", cityValidation);
country.addEventListener("blur", countryValidation);

// submit = controllo finale: rivalido TUTTI i campi, anche quelli
// su cui l'utente non è mai passato (il loro blur non è mai scattato)
newAddressForm.addEventListener("submit", evt => {

    // chiamo tutte le funzioni PRIMA e salvo i risultati: così ognuna
    // esegue e mostra il proprio errore
    const okStreet = streetValidation();
    const okCivicNumber = civicNumberValidation();
    const okPostalCode = postalCodeValidation();
    const okCity = cityValidation();
    const okCountry = countryValidation();

    // se anche un solo campo è invalido, blocco l'invio;
    // se sono tutti validi non chiamo preventDefault e il form parte
    if (!okStreet || !okCivicNumber || !okPostalCode || !okCity || !okCountry) {
        evt.preventDefault();
    }
})