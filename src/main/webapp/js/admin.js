// admin.js — validazione client del form di login dell'admin.

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
// @type serve solo all'editor per l'autocompletamento su .value/.validity

/** @type {HTMLInputElement} **/
const username = document.getElementById("username");
/** @type {HTMLInputElement} **/
const password = document.getElementById("password");
/** @type {HTMLFormElement} **/
const adminForm = document.getElementById("adminForm");

function usernameValidation() {
    // checkValidity() applica le regole HTML del campo (qui: required)
    const isValid = username.checkValidity();
    // se valido rimuove il messaggio, se non valido lo aggiunge
    setError("Inserire un username", isValid);
    return isValid;
}

function  passwordValidation() {
    const isValid = username.checkValidity();
    setError("Inserire una password", isValid);
    return isValid;
}

username.addEventListener("blur", usernameValidation);
password.addEventListener("blur", passwordValidation);

// submit = controllo finale: rivalido TUTTI i campi, anche quelli
// su cui l'utente non è mai passato (il loro blur non è mai scattato)
adminForm.addEventListener("submit", evt => {
    const okUsername = usernameValidation();
    const okPassword = passwordValidation();

    if (!okUsername || !okPassword) {
        evt.preventDefault();
    }
});