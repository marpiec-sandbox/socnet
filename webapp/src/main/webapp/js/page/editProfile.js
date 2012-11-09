function showProfileEntryDeletePanel(link) {
    var jqThis = jQuery(link);
    var deleteButtonPanel = jqThis.parents(".profileElementPreviewPanel").find(".deleteButtonPanel")
    deleteButtonPanel.show();
}

function cancelProfileEntryDelete(link) {
    var jqThis = jQuery(link);
    var deleteButtonPanel = jqThis.parents(".profileElementPreviewPanel").find(".deleteButtonPanel")
    deleteButtonPanel.hide();
}


function showNewDisplayedElement(elementId) {

    var jqElement = jQuery("#"+elementId);
    var orgHeight = jqElement.height();
    jqElement.css({opacity: 0, height: 0}).animate({height:orgHeight}).animate({opacity: 1}, 500, null, function() {
        jQuery(this).css({height: null});
    });
}