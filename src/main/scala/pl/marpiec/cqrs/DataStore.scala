package pl.marpiec.cqrs

import java.util.UUID

trait DataStore {

  def getEntity(entityClass:Class[_ <: CqrsEntity], uuid: UUID):CqrsEntity

  def addListener(entityClass:Class[_ <: CqrsEntity], listener:DataStoreListener)
}
