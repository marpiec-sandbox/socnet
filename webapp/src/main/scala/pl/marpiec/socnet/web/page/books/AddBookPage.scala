package pl.marpiec.socnet.web.page.books

import addBookPage.AddBookForm
import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles

/**
 * @author Marcin Pieciukiewicz
 */

class AddBookPage extends SecureWebPage(SocnetRoles.TRUSTED_USER) {

  add(new AddBookForm("addBookForm"))
}
