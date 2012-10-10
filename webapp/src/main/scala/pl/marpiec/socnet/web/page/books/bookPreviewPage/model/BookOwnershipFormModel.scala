package pl.marpiec.socnet.web.page.books.bookPreviewPage.model

import pl.marpiec.socnet.web.wicket.SecureFormModel
import pl.marpiec.util.BeanUtil
import pl.marpiec.socnet.model.bookuserinfo.BookOwnership
import pl.marpiec.socnet.service.bookuserinfo.input.BookOwnershipInput

/**
 * @author Marcin Pieciukiewicz
 */

class BookOwnershipFormModel extends SecureFormModel {
  var owner = false
  var willingToSell = false
  var willingToLend = false
  var wantToBuy = false
  var wantToBorrow = false

  def buildBookOwnershipInput(): BookOwnershipInput = {
    val bookOwnership = new BookOwnershipInput
    BeanUtil.copyProperties(bookOwnership, this)
    bookOwnership
  }

}

object BookOwnershipFormModel {

  def apply(bookOwnership: BookOwnership): BookOwnershipFormModel = {
    val model = new BookOwnershipFormModel
    BeanUtil.copyProperties(model, bookOwnership)
    model
  }
}