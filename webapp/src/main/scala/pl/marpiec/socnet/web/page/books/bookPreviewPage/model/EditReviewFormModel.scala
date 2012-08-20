package pl.marpiec.socnet.web.page.books.bookPreviewPage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.socnet.constant.Rating

/**
 * @author Marcin Pieciukiewicz
 */

class EditReviewFormModel extends SecureFormModel {
  var reviewText:String = ""
  var rating: Rating = null
}
