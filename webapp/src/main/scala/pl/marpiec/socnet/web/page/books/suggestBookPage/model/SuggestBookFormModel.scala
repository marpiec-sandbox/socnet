package pl.marpiec.socnet.web.page.books.suggestBookPage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel

/**
 * @author Marcin Pieciukiewicz
 */

class SuggestBookFormModel extends SecureFormModel {
  var title: String = _
  var polishTitle: String = _
  var authors: String = _
  var description: String = _
  var isbn: String = _
  var comment: String = _
}
