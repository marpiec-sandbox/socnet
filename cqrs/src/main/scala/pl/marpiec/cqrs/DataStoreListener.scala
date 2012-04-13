package pl.marpiec.cqrs


/**
 * @author Marcin Pieciukiewicz
 */

trait DataStoreListener {

  def startListeningToDataStore(dataStore: DataStore, entityClass: Class[_ <: CqrsEntity]) {
    dataStore.addListener(entityClass, this)
  }

  def onEntityChanged(entity: CqrsEntity)
}
