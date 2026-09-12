// Contenitore unico degli errori client + la sua lista <ul>,
const clientErrors = document.getElementById("clientErrors");
const clientErrorsList = document.getElementById("clientErrorsList");

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