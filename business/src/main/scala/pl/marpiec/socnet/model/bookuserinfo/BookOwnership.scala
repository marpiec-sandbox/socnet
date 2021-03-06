package pl.marpiec.socnet.model.bookuserinfo

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class BookOwnership {

  var userId: UID = _
  var knowThisBook = false
  var owner = false
  var description: String = _
  var willingToSell = false
  var willingToLend = false
  var wantToBuy = false
  var wantToBorrow = false

  def isInterestedInBook: Boolean = {
    knowThisBook || owner || willingToSell || willingToLend || wantToBuy || wantToBorrow
  }

}
