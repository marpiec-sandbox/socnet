package pl.marpiec.socnet.web.page.books

import pl.marpiec.socnet.web.authorization.SecureWebPage
import pl.marpiec.socnet.web.application.SocnetRoles

/**
 * @author Marcin Pieciukiewicz
 */

class YourBooksPage extends SecureWebPage(SocnetRoles.USER) {

}
