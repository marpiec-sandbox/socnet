package pl.marpiec.cqrs

import pl.marpiec.util.UID
import java.sql.{DriverManager, Connection}
import collection.Seq

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object UidGeneratorDbImpl {
  val UID_POOL_SIZE = 100
  val SELECT_UID = "SELECT uid FROM uids WHERE uidName = 'DEFAULT'"
  val UPDATE_UID = "UPDATE uids SET uid = uid + "+UID_POOL_SIZE+" WHERE uidName = 'DEFAULT'"
  val INSERT_UID = "INSERT INTO uids (id, uidName, uid) VALUES(NEXTVAL('uids_seq'), 'DEFAULT', "+(UID_POOL_SIZE + 1)+")"
}

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

    val selectUid = connection.prepareStatement(UidGeneratorDbImpl.SELECT_UID)
    val updateUid = connection.prepareStatement(UidGeneratorDbImpl.UPDATE_UID)

    val uidFromDb = selectUid.executeQuery()
    var uid:Long = 1
    if (uidFromDb.next()) {
      uid = uidFromDb.getLong(1)
      updateUid.executeUpdate()
    } else {
      val insertUid = connection.prepareStatement(UidGeneratorDbImpl.INSERT_UID)
      insertUid.executeUpdate()
    }

    availbleUids =  Seq.iterate[UID](new UID(uid), UidGeneratorDbImpl.UID_POOL_SIZE)((u => new UID(u.uid + 1))).toList

  }
}
