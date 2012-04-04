package pl.marpiec.cqrs

trait DataStore {

  def getEntity(entityClass:Class[_ <: CqrsEntity], id: Int):CqrsEntity

}
