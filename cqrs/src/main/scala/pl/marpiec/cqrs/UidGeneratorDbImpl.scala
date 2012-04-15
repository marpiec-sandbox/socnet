package pl.marpiec.cqrs

import pl.marpiec.util.UID
import java.sql.{DriverManager, Connection}
import collection.Seq

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

class UidGeneratorDbImpl extends UidGenerator {

  private val connection: Connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");

  var availbleUids = List[UID]()

  override def nextUid:UID = {

    synchronized {
      if (availbleUids.isEmpty) {
        loadNewUids()
      }

      val uid = availbleUids.head
      availbleUids = availbleUids.tail
      uid

    }
  }

  def loadNewUids() {

    val selectUid = connection.prepareStatement("SELECT uid FROM uids WHERE uidName = 'DEFAULT'")
    val updateUid = connection.prepareStatement("UPDATE uids SET uid = uid + 100 WHERE uidName = 'DEFAULT'")

    val uidFromDb = selectUid.executeQuery()
    var uid:Long = 1
    if (uidFromDb.next()) {
      uid = uidFromDb.getLong(1)
      updateUid.executeUpdate()
    } else {
      val insertUid = connection.prepareStatement("INSERT INTO uids (uidName, uid) VALUES('DEFAULT', 101)")
      insertUid.executeUpdate()
    }

    availbleUids =  Seq.iterate[UID](new UID(uid), 100)((u => u.nextUid)).toList

  }
}
