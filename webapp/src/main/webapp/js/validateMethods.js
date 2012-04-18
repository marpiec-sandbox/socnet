function validateEmail(email) {
    var reg = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
    return reg.test(email);
}

function validateNumber(number) {
    return !isNaN(number);
}

function validatePassword(password) {
    return password !=null && password.length >=5 && password.length <= 64;
}

function validatePasswordRepeat(passwordRepeat, password) {
    return passwordRepeat !=null && passwordRepeat == password
}