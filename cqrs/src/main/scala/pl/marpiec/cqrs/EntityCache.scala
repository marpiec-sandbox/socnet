package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait EntityCache {

  def get(entityClass: Class[_ <: CqrsEntity], id:UID):Option[CqrsEntity]
  def put(entity:CqrsEntity)
}
