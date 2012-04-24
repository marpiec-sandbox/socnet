package pl.marpiec.cqrs

import pl.marpiec.util.UID

/**
 * @author Marcin Pieciukiewicz
 */

trait AggregateCache {

  def get(entityClass: Class[_ <: Aggregate], id:UID):Option[Aggregate]
  def put(entity:Aggregate)
}
