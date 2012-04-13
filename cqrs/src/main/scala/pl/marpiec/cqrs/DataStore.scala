package pl.marpiec.cqrs

import pl.marpiec.util.UID

trait DataStore {

  def getEntity(entityClass:Class[_ <: CqrsEntity], id: UID):CqrsEntity

  def addListener(entityClass:Class[_ <: CqrsEntity], listener:DataStoreListener)
}
