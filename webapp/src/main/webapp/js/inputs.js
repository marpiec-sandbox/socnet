function createAutolabeledInput(inputSelector, initText) {

    var jQueryInput = jQuery(inputSelector)

    jQueryInput.val(initText);
    jQueryInput.addClass("inputLabel")
    jQueryInput.focus(function() {
        if(jQuery(this).val() == initText) {
            jQuery(this).removeClass("inputLabel")
            jQuery(this).val("")
        }
    })
    jQueryInput.blur(function() {
        if(jQuery(this).val() == "") {
            jQuery(this).addClass("inputLabel")
            jQuery(this).val(initText)
        }
    })
}