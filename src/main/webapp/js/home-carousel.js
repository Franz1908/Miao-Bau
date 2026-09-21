// home-carousel.js — scorrimento dei caroselli della homepage tramite le frecce.

// Prendo tutti i pulsanti freccia presenti in pagina (‹ e › di ogni carosello).
// Ogni pulsante ha due attributi data-*:
//   data-target = id del contenitore scorrevole da muovere (es. "carSale")
//   data-dir    = direzione: -1 = sinistra (indietro), 1 = destra (avanti)
document.querySelectorAll('.mb-scroll-btn').forEach(function (btn) {

    // A ogni click sulla freccia...
    btn.addEventListener('click', function () {

        // Recupero il carosello collegato a questa freccia.
        var track = document.getElementById(btn.getAttribute('data-target'));
        if (!track) return;   // sicurezza: se non esiste, non faccio nulla

        // Prendo la prima card per misurarne la larghezza:
        // così scorro di circa una card alla volta.
        var item = track.querySelector(':scope > div');

        // Passo di scorrimento = larghezza di una card + 24px di gap.
        // Se per qualche motivo la card non c'è, uso l'80% della larghezza visibile.
        var step = (item ? item.getBoundingClientRect().width : track.clientWidth * 0.8) + 24;

        // Scorro il carosello nella direzione indicata (data-dir), con animazione fluida.
        track.scrollBy({
            left: parseInt(btn.getAttribute('data-dir'), 10) * step,
            behavior: 'smooth'
        });
    });
});
