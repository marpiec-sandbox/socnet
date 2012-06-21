package pl.marpiec.socnet.readdatabase

import pl.marpiec.util.UID
import pl.marpiec.socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsDatabase {
  def getUserContactsByUserId(id: UID): Option[UserContacts]

  def getUserContactsIdByUserId(id: UID): Option[UID]
}
