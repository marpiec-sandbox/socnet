function initForm(formSelector) {
    jQuery(function() {
        var jqForm = jQuery(formSelector);
        jqForm.find("input").each(initInput)
        jqForm.find("textarea").each(initInput)
    })
}


function initInput() {
    var jqThis = $(this)
    jqThis.focus(function() {
        jqThis.parents("div.input").addClass("focused")
    })
    jqThis.blur(function() {
        jqThis.parents("div.input").removeClass("focused")
    })
}