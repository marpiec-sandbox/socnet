package pl.marpiec.socnet.model

import pl.marpiec.cqrs.Aggregate
import pl.marpiec.util.{BeanUtil, UID}

/**
 * @author Marcin Pieciukiewicz
 */
class UserContacts extends Aggregate(null, 0) {

  var userId: UID = _

  var contactsIds = Set[UID]()

  def copy = {
    BeanUtil.copyProperties(new UserContacts, this)
  }
}
