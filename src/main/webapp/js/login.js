// login.js — validazione client del form di login.

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
// @type serve solo all'editor per l'autocompletamento su .value/.validity

/** @type {HTMLInputElement} **/
const email = document.getElementById("email");
/** @type {HTMLInputElement} **/
const password = document.getElementById("password");
/** @type {HTMLFormElement} **/
const loginForm = document.getElementById("loginForm");

function emailValidation() {
    // pulisco sempre tutti i messaggi email (isValid=true = rimuovi)
    setError("Inserire un'email", true);
    setError("Inserire un'email valida", true);

    // se rispetta tutte le regole HTML è valida: esco subito
    if (email.checkValidity()) return true;

    // altrimenti capisco perché è invalida e mostro il messaggio giusto
    if (email.validity.valueMissing)                                  // campo vuoto (required)
        setError("Inserire un'email", false);
    else if (email.validity.patternMismatch || email.validity.typeMismatch) // formato errato
        setError("Inserire un'email valida", false);

    return false;
}

function passwordValidation() {
    setError("Inserire una password", true);
    if (password.checkValidity()) return true;
    setError("Inserire una password", false);   // unico caso: vuota
    return false;
}

// --- Aggancio agli eventi ---

// blur = validazione "dal vivo": ogni campo si controlla appena l'utente lo lascia
email.addEventListener("blur", emailValidation);
password.addEventListener("blur", passwordValidation);

// submit = controllo finale: rivalido TUTTI i campi, anche quelli
// su cui l'utente non è mai passato (il loro blur non è mai scattato)
loginForm.addEventListener("submit", function (evt) {

    // chiamo tutte le funzioni PRIMA e salvo i risultati: così ognuna
    // esegue e mostra il proprio errore
    const okEmail = emailValidation();
    const okPassword = passwordValidation();

    if (!okEmail || !okPassword) {
        evt.preventDefault();
    }
});