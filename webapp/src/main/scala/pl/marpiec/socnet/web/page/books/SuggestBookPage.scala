package pl.marpiec.socnet.web.page.books

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles
import suggestBookPage.SuggestBookForm

/**
 * @author Marcin Pieciukiewicz
 */

class SuggestBookPage extends SecureWebPage(SocnetRoles.TRUSTED_USER) {

  add(new SuggestBookForm("suggestBookForm"))
}
