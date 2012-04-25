var validatorGroups = ["requiredValidator", "emailValidator", "urlValidator", "numberValidator", "passwordValidator", "passwordRepeatValidator"]


function passwordValidator(jqElement){

    var value = jqElement.val();

    if(validatePassword(value)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Password is too short, min 5 chars");
    }
}

function passwordRepeatValidator(jqElement){

    var value = jqElement.val();
    var originalValue = jQuery(".passwordValidator").val();

    if(validatePasswordRepeat(value, originalValue)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Given passwords are not the same");
    }
}

function emailValidator(jqElement){

    var value = jqElement.val();

    if(isEmpty(value) || validateEmail(value)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Niepoprawny adres email");
    }
}

function urlValidator(jqElement){

    var value = jqElement.val();

    if(isEmpty(value) || validateUrl(value)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Niepoprawny url");
    }
}

function requiredValidator(jqElement){
    var value = jqElement.val();

    if(value.trim() == ""){
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Pole jest wymagane");
    } else {
        hideValidationMessage(jqElement);
    }
}

function numberValidator(jqElement){
    var value = jqElement.val();
    if(validateNumber(value)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Wymagana liczba");
    }
}

function getMessageContainer(element){
    return element.parent().find(".validatorMessage");
}

function hideValidationMessage(element){
    getMessageContainer(element).html("");
    element.parent().removeClass("invalid")
}

function showValidationMessage(element, message){
    getMessageContainer(element).html(message);
    element.parent().addClass("invalid")
}

function isElementValid(jqElement){
    var valResult = jqElement.data("valResult")
    if(valResult==undefined || valResult == true){
        return true;
    } else {
        return false;
    }
}

function isElementsGroupValidated(groupName, jqFormElement){
    var validatedOK = true;
    var elements = jqFormElement.find("."+groupName);
    var jqElement;
    for(var p=0; p<elements.length; p++){
        jqElement = jQuery(elements[p]);
        if(!isElementValid(jqElement)){
            validatedOK = false;
            break;
        }
    }
    return validatedOK;
}

function isFormValidatedOK(jqFormElement){
    var validatedOK = true;

    for(var p=0;p<validatorGroups.length;p++) {
        validatedOK &= isElementsGroupValidated(validatorGroups[p],jqFormElement);
    }

    return validatedOK;
}

function clearValidationResult(jqElement){
    jqElement.data("valResult",true);
}

function clearValidationResultsInGroup(groupName, formElement){
    var elements = formElement.find("."+groupName);
    var jqElement;
    for(var p=0; p<elements.length; p++){
        jqElement = jQuery(elements[p]);
        clearValidationResult(jqElement);
    }
}

function clearValidationResults(formElement){
    for(var p=0;p<validatorGroups.length;p++) {
        clearValidationResultsInGroup(validatorGroups[p], formElement);
    }
}

function addValidator(jqElement, validator){
    var validators = jqElement.data("validators");
    if(validators==undefined){
        validators = new Array();
        jqElement.data("validators", validators);
    }
    validators.push(validator);
}

function elementValidator(){

    var jqThis = jQuery(this);

    var validators = jqThis.data("validators");
    var validator;
    if(validators==undefined){
        validators = new Array();
    }

    clearValidationResult(jqThis);

    for(var p=0; p<validators.length; p++){
        validator = validators[p];

        if(isElementValid(jqThis)){
            validator(jqThis);
        }
    }
}

function initValidation(formSelector) {

    jQuery(function() {

        var jqForm = jQuery(formSelector)

        jqForm.find(".requiredValidator").each(function(){
            addValidator(jQuery(this),requiredValidator);
            jQuery(this).blur(elementValidator);
        });

        jqForm.find(".emailValidator").each(function(){
            addValidator(jQuery(this),emailValidator);
            jQuery(this).blur(elementValidator);
        });

        jqForm.find(".urlValidator").each(function(){
            addValidator(jQuery(this),urlValidator);
            jQuery(this).blur(elementValidator);
        });

        jqForm.find(".numberValidator").each(function(){
            addValidator(jQuery(this),numberValidator);
            jQuery(this).blur(elementValidator);
        });

        jqForm.find(".passwordValidator").each(function(){
            addValidator(jQuery(this),passwordValidator);
            jQuery(this).blur(elementValidator);
        });

        jqForm.find(".passwordRepeatValidator").each(function(){
            addValidator(jQuery(this),passwordRepeatValidator);
            jQuery(this).blur(elementValidator);
        });

        jqForm.submit(function(){
            var jqThis = jQuery(this);
            clearValidationResults(jqThis);

            var selector = "";
            for(var p=0;p<validatorGroups.length;p++) {
                selector += "."+validatorGroups[p] + ", "
            }
            selector = selector.substr(0, selector.length - 2)

            jqThis.find(selector).each(elementValidator);

            if(isFormValidatedOK(jqThis)){
                return true;
            } else {
                return false;
            }
        });
    });
}