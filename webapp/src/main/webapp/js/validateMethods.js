function validateEmail(email) {
    var reg = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
    return reg.test(email);
}

function validateUrl(url) {
    var reg = /^(https:\/\/|http:\/\/)?[a-z0-9-\.]+\.[a-z]{2,4}\/?([^\s<>\#%"\,\{\}\\|\\\^\[\]`]+)?$/;
    return reg.test(url);
}


function validateNumber(number, constraints) {
    if(isNaN(number)) {
        return false;
    }

    if(constraints !== undefined) {
        if(constraints.min != undefined && number < constraints.min) {
            return false;
        }
        if(constraints.min != undefined && number > constraints.max) {
            return false;
        }
    }
    return true;
}

function validatePassword(password) {
    return password !=null && password.length >=5 && password.length <= 64;
}

function validatePasswordRepeat(passwordRepeat, password) {
    return passwordRepeat !=null && passwordRepeat == password
}

function isEmpty(value) {
    return !isNotEmpty(value)
}

function isNotEmpty(value) {
    return value && value.length > 0
}