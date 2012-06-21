package pl.marpiec.cqrs

import pl.marpiec.util.UID
import collection.Seq
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

@Component("uidGenerator")
class UidGeneratorDbImpl @Autowired()(val jdbcTemplate: JdbcTemplate) extends UidGenerator {

  val UID_POOL_SIZE = 100
  val SELECT_UID = "SELECT uid FROM uids WHERE uidName = 'DEFAULT'"
  val UPDATE_UID = "UPDATE uids SET uid = uid + " + UID_POOL_SIZE + " WHERE uidName = 'DEFAULT'"
  val INSERT_UID = "INSERT INTO uids (id, uidName, uid) VALUES(NEXTVAL('uids_seq'), 'DEFAULT', " + (UID_POOL_SIZE + 1) + ")"

  var availableUids = List[UID]()

  override def nextUid: UID = {

    synchronized {
      if (availableUids.isEmpty) {
        loadNewUids()
      }

      val uid = availableUids.head
      availableUids = availableUids.tail
      uid

    }
  }

  def loadNewUids() {

    var uid: Long = 1
    try {
      uid = jdbcTemplate.queryForLong(SELECT_UID)
      jdbcTemplate.update(UPDATE_UID)
    } catch {
      case ex: EmptyResultDataAccessException => {
        jdbcTemplate.update(INSERT_UID)
      }
    }

    availableUids = Seq.iterate[UID](new UID(uid), UID_POOL_SIZE)((u => new UID(u.uid + 1))).toList

  }
}
