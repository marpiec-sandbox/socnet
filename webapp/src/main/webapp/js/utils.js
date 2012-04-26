function isVisible(jqElement) {
    var elem = jqElement
    do {
        if(elem.css('visibility')=='hidden' || elem.css('display')=='none') {
            return false;
        }
        elem = elem.parent();
    } while (!elem.is("body"))

    return true;
}