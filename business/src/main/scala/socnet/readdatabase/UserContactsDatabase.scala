package socnet.readdatabase

import pl.marpiec.util.UID
import socnet.model.UserContacts

/**
 * @author Marcin Pieciukiewicz
 */

trait UserContactsDatabase {
  def getUserContactsByUserId(id: UID):Option[UserContacts]

  def getUserContactsIdByUserId(id: UID):Option[UID]
}
