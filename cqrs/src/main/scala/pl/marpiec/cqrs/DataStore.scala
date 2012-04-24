package pl.marpiec.cqrs

import pl.marpiec.util.UID

trait DataStore {

  def getEntity(entityClass:Class[_ <: Aggregate], id: UID):Aggregate

  def addListener(entityClass:Class[_ <: Aggregate], listener:DataStoreListener)
}
