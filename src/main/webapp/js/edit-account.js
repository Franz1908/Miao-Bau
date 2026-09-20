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

async function checkEmailAvailability() {
    // pulisco sempre il messaggio prima di ricontrollare
    setError("E-mail già registrata", true);

    // se il formato non è valido non ha senso chiedere al server: esco
    // (emailValidation ha già mostrato il messaggio di formato giusto)
    if (!emailValidation()) return false;

    try {
        // chiamo la servlet passando l'email come parametro (codificato per @ e .)
        const result = await fetch("emailCheck?email=" + encodeURIComponent(email.value));

        // fetch NON fallisce sugli errori HTTP: controllo io lo stato
        if (!result.ok) {
            throw new Error("HTTP error" + result.status);
        }

        // converto la risposta JSON in oggetto: { available: true/false }
        const data = await result.json();

        // available=false = email già presa: mostro l'errore e blocco
        if (data.available === false) {
            setError("E-mail già registrata", false);
            return false;
        }

        return true;   // email libera
    }
    catch (error) {
        // fallimento silenzioso: se la verifica non è disponibile non blocco
        // la registrazione (il controllo vero lo fa il server al submit)
        console.error("Verifica e-mail non disponibile", error);
        return true;
    }
}

function currentPasswordValidation() {
    // La password corrente serve SOLO se l'utente sta cambiando password.
    // Quindi è obbligatoria a una condizione: c'è una nuova password
    // (newPassword non vuota) ma manca quella corrente (currentPassword vuota).
    if (newPassword.value !== "" && currentPassword.value === "") {
        setError("Inserire la password corrente", false);   // mostro l'errore
        return false;
    }

    // In tutti gli altri casi va bene:
    // - non sto cambiando password (newPassword vuota) → la corrente non serve
    // - sto cambiando e ho compilato la corrente → ok
    // In entrambi pulisco l'eventuale messaggio e considero valido.
    setError("Inserire la password corrente", true);
    return true;
}

function newPasswordValidation() {
    // pulisco sempre tutti i messaggi
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
    //pulisco sempre tutti i messaggi
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

// --- Aggancio agli eventi ---

// blur = validazione "dal vivo": ogni campo si controlla appena l'utente lo lascia
firstName.addEventListener("blur", firstNameValidation);
lastName.addEventListener("blur", lastNameValidation);
email.addEventListener("blur", checkEmailAvailability);
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
