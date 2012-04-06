package pl.marpiec.cqrs

import pl.marpiec.socnet.model.User

/**
 * @author Marcin Pieciukiewicz
 */

trait DataStoreListener {

  def startListeningToDataStore(dataStore:DataStore, entityClass:Class[_ <: CqrsEntity]) {
    dataStore.addListener(entityClass, this)
  }

  def onEntityChanged(entity:CqrsEntity)
}
