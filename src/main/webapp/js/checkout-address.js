// checkout-address.js — gestione del blocco "nuovo indirizzo" nel checkout.
//
// Fa due cose:
//   1) mostra/nasconde i campi del nuovo indirizzo in base al radio scelto;
//   2) al submit valida quei campi SOLO se è selezionata l'opzione "new".
//
// Riusa le stesse funzioni di validazione per campo definite in new-address.js
// (streetValidation, civicNumberValidation, ...): gli id dei campi sono identici.

// --- Riferimenti agli elementi del DOM ---
const checkoutForm = document.getElementById("checkoutForm");
const newAddrBox = document.getElementById("newAddrBox");

// Mostra i campi solo se il radio "new" è selezionato ---
function syncNewAddress() {
    // radio attualmente selezionato tra le opzioni indirizzo
    const selected = checkoutForm.querySelector('input[name="addressChoice"]:checked');
    const isNew = selected && selected.value === "new";

    // mostro/nascondo il riquadro
    newAddrBox.style.display = isNew ? "" : "none";

    // disabilito gli input quando sono nascosti: così non vengono inviati
    // e non fanno scattare la validazione HTML (required) inutilmente
    newAddrBox.querySelectorAll("input").forEach(input => {
        input.disabled = !isNew;
    });
}

// ricontrollo ad ogni cambio di scelta + una volta al caricamento
checkoutForm.querySelectorAll('input[name="addressChoice"]').forEach(radio => {
    radio.addEventListener("change", syncNewAddress);
});
syncNewAddress();

// --- Validazione al submit, solo se si sta inserendo un nuovo indirizzo ---
checkoutForm.addEventListener("submit", evt => {
    const selected = checkoutForm.querySelector('input[name="addressChoice"]:checked');
    const isNew = selected && selected.value === "new";

    // se l'utente usa un indirizzo già salvato non c'è nulla da validare
    if (!isNew) return;

    // rivalido TUTTI i campi del nuovo indirizzo (riuso le funzioni di new-address.js)
    const okStreet = streetValidation();
    const okCivicNumber = civicNumberValidation();
    const okPostalCode = postalCodeValidation();
    const okCity = cityValidation();
    const okCountry = countryValidation();

    // se anche un solo campo è invalido, blocco l'invio del form
    if (!okStreet || !okCivicNumber || !okPostalCode || !okCity || !okCountry) {
        evt.preventDefault();
    }
});
