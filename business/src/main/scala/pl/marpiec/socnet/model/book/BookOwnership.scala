package pl.marpiec.socnet.model.book

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

class BookOwnership {

  var userId:UID = _
  var owner = false
  var willingToSell = false
  var willingToLend = false
  var wantToBuy = false
  var wantToBorrow = false
  
}
