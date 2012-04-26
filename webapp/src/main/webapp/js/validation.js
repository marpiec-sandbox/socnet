var validatorGroups = ["requiredValidator",
    "emailValidator", "urlValidator",
    "numberValidator", "stringValidator",
    "passwordValidator", "passwordRepeatValidator"]


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
    var constraints = eval('(' + jqElement.attr('alt') + ')');
    if(validateNumber(value, constraints)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Wymagana liczba");
    }
}

function stringValidator(jqElement){
    var value = jqElement.val();
    var constraints = eval('(' + jqElement.attr('alt') + ')');
    if(validateString(value, constraints)){
        hideValidationMessage(jqElement);
    } else {
        jqElement.data("valResult", false);
        showValidationMessage(jqElement, "Warto?? wymagana");
    }
}

function getMessageContainer(element){
    return element.parent().find(".validatorMessage");
}

function hideValidationMessage(element){
    getMessageContainer(element).html("");
    element.parent("div").removeClass("invalid");
}

function showValidationMessage(element, message){

    var msg = message;
    if(element.attr("alt") !== undefined) {
       var attr = eval("("+element.attr("alt")+")");
       if(attr.msg !== undefined) {
           msg = attr.msg;
       }
    }

    getMessageContainer(element).html(msg);
    element.parent("div").addClass("invalid");
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
    
    if(isVisible(jqThis)) {
        for(var p=0; p<validators.length; p++){
            validator = validators[p];

            if(isElementValid(jqThis)){
                validator(jqThis);
            }
        }
    }
}

function showFormValidationError(jqForm) {
    jqForm.find(".warningMessage").html("There are validation errors!");
}

function blinkForm(jqForm) {
    jqForm.stop().css({opacity:0}).animate({opacity:1}, 300)
}

function validateForm(jqForm) {
    clearValidationResults(jqForm);

    var selector = "";
    for(var p=0;p<validatorGroups.length;p++) {
        selector += "."+validatorGroups[p] + ", "
    }
    selector = selector.substr(0, selector.length - 2)

    jqForm.find(selector).each(elementValidator);

    if(isFormValidatedOK(jqForm)){
        return true;
    } else {
        showFormValidationError(jqForm);
        blinkForm(jqForm);

        return false;
    }
}

function initValidation(formSelector, submitButtonSelector) {

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

        jqForm.find(".stringValidator").each(function(){
            addValidator(jQuery(this),stringValidator);
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

        jqForm.find(submitButtonSelector).each(function() {
            jQuery(this).attr("onclick", "if (!validateForm(jQuery(this).parents('form'))) return false;" + jQuery(this).attr("onclick"));
        });

        jqForm.submit(function() {
            return validateForm(jQuery(this));
        });

    });



}