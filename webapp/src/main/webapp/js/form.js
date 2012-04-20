$(function(){

    jQuery("input").each(function() {
       var jqThis = $(this)
       jqThis.focus(function() {
           jqThis.parents("div.input").addClass("focused")
       })
        jqThis.blur(function() {
            jqThis.parents("div.input").removeClass("focused")
        })

    })

})