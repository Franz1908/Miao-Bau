// edit-account.js — validazione client del form di modifica dell'account.

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
// @type serve solo all'editor per l'autocompletamento su .value/.validity

/** @type {HTMLInputElement} **/
const firstName = document.getElementById("firstName");
/** @type {HTMLInputElement} **/
const lastName = document.getElementById("lastName");
/** @type {HTMLInputElement} **/
const email = document.getElementById("email");
/** @type {HTMLInputElement} **/
const currentPassword = document.getElementById("currentPassword");
/** @type {HTMLInputElement} **/
const newPassword = document.getElementById("newPassword");
/** @type {HTMLInputElement} **/
const confirmPassword = document.getElementById("confirmPassword");
/** @type {HTMLInputElement} **/
const birthDate = document.getElementById("birthDate");
/** @type {HTMLInputElement} **/
const telephone = document.getElementById("telephone");
/** @type {HTMLFormElement} **/
const editAccountForm = document.getElementById("editAccountForm");

// --- Una funzione di validazione per campo ---
// Ognuna valida il proprio campo, aggiorna la lista errori
// e restituisce true (valido) / false (non valido) per il submit.

function firstNameValidation() {
    // checkValidity() applica le regole HTML del campo (qui: required)
    const isValid = firstName.checkValidity();
    // se valido rimuove il messaggio, se non valido lo aggiunge
    setError("Inserire un nome", isValid);
    return isValid;
}

function lastNameValidation() {
    const isValid = lastName.checkValidity();
    setError("Inserire un cognome", isValid);
    return isValid;
}

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

function currentPasswordValidation() {
    if (newPassword.value !== "" && currentPassword.value === "") {
        setError("Inserire la password corrente", false);
        return false;
    }

    setError("Inserire la password corrente", true);
    return true;
}

function newPasswordValidation() {
    // pulisco sempre
    setError("La password deve avere minimo 8 caratteri e massimo 16 caratteri", true);
    setError("La password deve contenere almeno un numero ed un carattere speciale", true);

    // campo facoltativo: se vuota, non sto cambiando password → ok
    if (newPassword.value === "") return true;

    // c'è un valore: dev'essere valido
    if (newPassword.checkValidity()) return true;

    if (newPassword.validity.patternMismatch) {
        setError("La password deve contenere almeno un numero ed un carattere speciale", false);
    }
    else if (newPassword.validity.tooShort || newPassword.validity.tooLong) {
        setError("La password deve avere minimo 8 caratteri e massimo 16 caratteri", false);
    }

    return false;
}

function confirmPasswordValidation() {
    //pulisco sempre
    setError("Le password non coincidono", true);

    // campo facoltativo: se vuota, non sto cambiando password → ok
    if (newPassword.value === "") return true;

    // se la conferma password e la nuova password dell'utente sono diverse c'è un errore
    if (confirmPassword.value !== newPassword.value) {
        setError("Le password non coincidono", false);
        return false;
    }

    return true;
}

function birthDateValidation() {
    // pulisco sempre tutti i messaggi data
    setError("Inserisci una data valida", true);
    setError("Non puoi inserire una data futura", true);

    // campo facoltativo: se vuoto è valido, non controllo altro
    if (birthDate.value === "") return true;

    // se compilato controllo se il browser lo riconosce come data ben formata
    if (!birthDate.checkValidity()) {
        setError("Inserisci una data valida", false);
        return false;
    }

    // data valida ma non deve essere futura
    // new Date(valore) = data scelta --- new Date() = adesso
    if (new Date(birthDate.value) > new Date()) {
        setError("Non puoi inserire una data futura", false);
        return false;
    }

    return true;
}

firstName.addEventListener("blur", firstNameValidation);
lastName.addEventListener("blur", lastNameValidation);
email.addEventListener("blur", emailValidation);
currentPassword.addEventListener("blur", currentPasswordValidation);
birthDate.addEventListener("blur", birthDateValidation);

// submit = controllo finale: rivalido TUTTI i campi, anche quelli
// su cui l'utente non è mai passato (il loro blur non è mai scattato)
editAccountForm.addEventListener("submit", evt => {

    // chiamo tutte le funzioni PRIMA e salvo i risultati così ognuna
    // esegue e mostra il proprio errore
    const okFirstName = firstNameValidation();
    const okLastName = lastNameValidation();
    const okEmail = emailValidation();
    const okCurrentPassword = currentPasswordValidation();
    const okBirthDate = birthDateValidation();
    const okNewPassword = newPasswordValidation();
    const okConfirmPassword = confirmPasswordValidation();

    // se anche un solo campo è invalido, blocco l'invio;
    // se sono tutti validi non chiamo preventDefault e il form parte
    if (
        !okFirstName ||
        !okLastName ||
        !okEmail ||
        !okCurrentPassword ||
        !okBirthDate ||
        !okNewPassword ||
        !okConfirmPassword
    ) {
        evt.preventDefault();
    }
})
