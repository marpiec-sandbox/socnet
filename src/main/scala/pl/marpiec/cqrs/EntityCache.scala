package pl.marpiec.cqrs

/**
 * @author Marcin Pieciukiewicz
 */

trait EntityCache {

  def get(entityClass: Class[_ <: CqrsEntity], id:Int):Option[CqrsEntity]
  def put(entity:CqrsEntity)
}
