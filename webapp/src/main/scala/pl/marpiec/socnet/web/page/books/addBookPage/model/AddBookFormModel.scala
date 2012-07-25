package pl.marpiec.socnet.web.page.books.addBookPage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel

/**
 * @author Marcin Pieciukiewicz
 */

class AddBookFormModel extends SecureFormModel {
  var title:String = _
  var polishTitle:String = _
  var authors:String = _
  var description:String = _
  var isbn:String = _
}
