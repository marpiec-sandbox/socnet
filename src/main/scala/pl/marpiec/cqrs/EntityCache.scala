package pl.marpiec.cqrs

import java.util.UUID

/**
 * @author Marcin Pieciukiewicz
 */

trait EntityCache {

  def get(entityClass: Class[_ <: CqrsEntity], uuid:UUID):Option[CqrsEntity]
  def put(entity:CqrsEntity)
}
