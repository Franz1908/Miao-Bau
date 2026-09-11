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
const firstNameError = document.getElementById("firstNameError");
const lastNameError = document.getElementById("lastNameError");
const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");
const birthDateError = document.getElementById("birthDateError");

function firstNameValidation() {
    if (firstName.checkValidity()) {
        firstNameError.innerHTML = "";
        firstNameError.classList.remove("text-danger");
        return true;
    }

    firstNameError.innerHTML = "Inserire un nome";
    firstNameError.classList.add("text-danger");
    return false;
}

function lastNameValidation() {
    if (lastName.checkValidity()) {
        lastNameError.innerHTML = "";
        lastNameError.classList.remove("text-danger");
        return true;
    }

    lastNameError.innerHTML = "Inserire un cognome";
    lastNameError.classList.add("text-danger");
    return false;
}

function emailValidation() {
    if (email.checkValidity()) {
        emailError.innerHTML = "";
        emailError.classList.remove("text-danger");
        return true;
    }

    if (email.validity.valueMissing) {
        emailError.innerHTML = "Inserire un'email";
        emailError.classList.add("text-danger");
    } else if (email.validity.patternMismatch || email.validity.typeMismatch) {
        emailError.innerHTML = "Inserire un'email valida";
        emailError.classList.add("text-danger");
    }
    return false;
}

function passwordValidation() {
    if (password.checkValidity()) {
        passwordError.innerHTML = "";
        passwordError.classList.remove("text-danger");
        return true;
    }

    if (password.validity.valueMissing) {
        passwordError.innerHTML = "Inserire una password";
        passwordError.classList.add("text-danger");
    }
    else if (password.validity.patternMismatch) {
        passwordError.innerHTML = "La password deve contenere almeno un numero ed un carattere speciale";
        passwordError.classList.add("text-danger");
    }
    else if (password.validity.tooShort || password.validity.tooLong) {
        passwordError.innerHTML = "La password deve avere minimo 8 caratteri e massimo 16 caratteri";
        passwordError.classList.add("text-danger");
    }
    return false;
}

function birthDateValidation() {
    // 1. facoltativo: vuoto è valido
    if (birthDate.value === "") {
        birthDateError.innerHTML = "";
        birthDateError.classList.remove("text-danger");
        return true;
    }

    // 2. c'è un valore: il browser lo considera una data valida?
    if (!birthDate.checkValidity()) {
        birthDateError.innerHTML = "Inserisci una data valida";
        birthDateError.classList.add("text-danger");
        return false;
    }

    // 3. è una data valida, ma non deve essere futura
    const date = new Date(birthDate.value);
    const today = new Date();

    if (date > today) {
        birthDateError.innerHTML = "Non puoi inserire una data futura";
        birthDateError.classList.add("text-danger");
        return false;
    }

    // 4. tutto ok
    birthDateError.innerHTML = "";
    birthDateError.classList.remove("text-danger");
    return true;
}

firstName.addEventListener("blur", firstNameValidation);
lastName.addEventListener("blur", lastNameValidation);
email.addEventListener("blur", emailValidation);
password.addEventListener("blur", passwordValidation);
birthDate.addEventListener("blur", birthDateValidation);

registerForm.addEventListener("submit", function (evt) {
    const okFirstName = firstNameValidation();
    const okLastName = lastNameValidation();
    const okEmail = emailValidation();
    const okPassword = passwordValidation();
    const okBirthDate = birthDateValidation();

    if (!okFirstName || !okLastName || !okEmail || !okPassword || !okBirthDate) {
        evt.preventDefault();
    }
});

