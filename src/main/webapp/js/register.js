// register.js — validazione client del form di registrazione.

// --- Riferimenti agli elementi del DOM (presi una volta sola) ---
// @type serve solo all'editor per l'autocompletamento su .value/.validity

/** @type {HTMLInputElement} **/
const firstName = document.getElementById("firstName");
/** @type {HTMLInputElement} **/
const lastName = document.getElementById("lastName");
/** @type {HTMLInputElement} **/
const email = document.getElementById("email");
/** @type {HTMLInputElement} **/
const password = document.getElementById("password");
/** @type {HTMLInputElement} **/
const birthDate = document.getElementById("birthDate");
/** @type {HTMLInputElement} **/
const telephone = document.getElementById("telephone");
/** @type {HTMLFormElement} **/
const registerForm = document.getElementById("registerForm");

// Contenitore unico degli errori client + la sua lista <ul>,
const clientErrors = document.getElementById("clientErrors");
const clientErrorsList = document.getElementById("clientErrorsList");


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


function passwordValidation() {
    // pulisco sempre tutti i messaggi password prima di ricontrollare
    setError("Inserire una password", true);
    setError("La password deve contenere almeno un numero ed un carattere speciale", true);
    setError("La password deve avere minimo 8 caratteri e massimo 16 caratteri", true);

    if (password.checkValidity()) return true;

    if (password.validity.valueMissing)                 // vuota (required)
        setError("Inserire una password", false);
    else if (password.validity.patternMismatch)         // non rispetta il pattern
        setError("La password deve contenere almeno un numero ed un carattere speciale", false);
    else if (password.validity.tooShort || password.validity.tooLong) // lunghezza fuori range
        setError("La password deve avere minimo 8 caratteri e massimo 16 caratteri", false);

    return false;
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


// --- Helper: aggiunge o rimuove un messaggio nella lista errori ---
// message = testo dell'errore
// isValid = true  -> campo a posto  -> RIMUOVO quel messaggio
//           false -> campo errato   -> AGGIUNGO quel messaggio
function setError(message, isValid) {

    if (isValid) {
        // cerco tra le voci <li> quella con questo testo e la tolgo
        const errors = clientErrorsList.querySelectorAll("li");
        errors.forEach(li => {
            if (li.textContent === message) {   // identifico la voce dal suo testo
                li.remove();                    // la rimuovo dalla lista
            }
        });

    }
    else {

        // Evita di aggiungere lo stesso errore più volte
        const errors = clientErrorsList.querySelectorAll("li");
        const alreadyExists = Array.from(errors).some(li => {
            return li.textContent === message;
        });

        if (!alreadyExists) {
            const li = document.createElement("li"); // creo una nuova voce
            li.textContent = message;                // ci metto il testo dell'errore
            clientErrorsList.appendChild(li);        // la aggiungo alla lista
        }
    }

    // Mostra/nasconde il contenitore degli errori
    // (visibile solo se la lista ha almeno una voce)
    clientErrors.style.display = clientErrorsList.children.length > 0 ? "" : "none";
}


// --- Aggancio agli eventi ---

// blur = validazione "dal vivo": ogni campo si controlla appena l'utente lo lascia
firstName.addEventListener("blur", firstNameValidation);
lastName.addEventListener("blur", lastNameValidation);
email.addEventListener("blur", emailValidation);
password.addEventListener("blur", passwordValidation);
birthDate.addEventListener("blur", birthDateValidation);


// submit = controllo finale: rivalido TUTTI i campi, anche quelli
// su cui l'utente non è mai passato (il loro blur non è mai scattato)
registerForm.addEventListener("submit", function (evt) {

    // chiamo tutte le funzioni PRIMA e salvo i risultati: così ognuna
    // esegue e mostra il proprio errore (niente corto circuito dell'||)
    const okFirstName = firstNameValidation();
    const okLastName = lastNameValidation();
    const okEmail = emailValidation();
    const okPassword = passwordValidation();
    const okBirthDate = birthDateValidation();

    // se anche un solo campo è invalido, blocco l'invio;
    // se sono tutti validi non chiamo preventDefault e il form parte
    if (
        !okFirstName ||
        !okLastName ||
        !okEmail ||
        !okPassword ||
        !okBirthDate
    ) {
        evt.preventDefault();
    }
});