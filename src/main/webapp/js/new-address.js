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

function streetValidation() {
    const isValid = street.checkValidity();
    setError("Inserire una via", isValid);
    return isValid;
}

function civicNumberValidation() {
    const isValid = civicNumber.checkValidity();
    setError("Inserire un numero civico", isValid);
    return isValid;
}

function postalCodeValidation() {
    setError("Inserire un CAP", true);
    setError("Inserire un CAP valido", true);

    if (postalCode.checkValidity()) return true;

    if (postalCode.validity.valueMissing) {
        setError("Inserire un CAP", false);
    }
    else if (postalCode.validity.patternMismatch) {
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

street.addEventListener("blur", streetValidation);
civicNumber.addEventListener("blur", civicNumberValidation);
postalCode.addEventListener("blur", postalCodeValidation);
city.addEventListener("blur", cityValidation);
country.addEventListener("blur", countryValidation);

newAddressForm.addEventListener("submit", evt => {
    const okStreet = streetValidation();
    const okCivicNumber = civicNumberValidation();
    const okPostalCode = postalCodeValidation();
    const okCity = cityValidation();
    const okCountry = countryValidation();

    if (!okStreet || !okCivicNumber || !okPostalCode || !okCity || !okCountry) {
        evt.preventDefault();
    }
})