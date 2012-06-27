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
