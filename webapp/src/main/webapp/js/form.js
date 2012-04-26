function initForm(formSelector) {
    jQuery(function() {
        var jqForm = jQuery(formSelector);
        jqForm.find("input").each(initInput)
        jqForm.find("textarea").each(initInput)
        jqForm.find("select").each(initInput)
    })
}


function initInput() {
    var jqThis = $(this)
    jqThis.focus(function() {
        jqThis.parent().addClass("focused")
        jqThis.parents("div.formRow").addClass("focused")
    })
    jqThis.blur(function() {
        jqThis.parent().removeClass("focused")
        jqThis.parents("div.formRow").removeClass("focused")
    })
}