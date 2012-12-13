function ensureMinimumContentHeight() {
    jQuery(".pageContent").each(function() {
        var jqContent = jQuery(this);
        var currentMinHeight = parseInt(jqContent.css("minHeight"));
        var windowHeight = jQuery(window).height();
        var headerHeight = jQuery(".mainHeader").height();
        var footerHeight = jQuery(".mainFooter").height();
        var newMinHeight = Math.max(currentMinHeight, windowHeight - headerHeight - footerHeight - 1);

        jQuery(this).css({minHeight: newMinHeight});
    });
}