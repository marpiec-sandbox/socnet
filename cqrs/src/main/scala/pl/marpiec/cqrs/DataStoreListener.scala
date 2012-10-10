package pl.marpiec.cqrs


/**
 * @author Marcin Pieciukiewicz
 */

trait DataStoreListener[E <: Aggregate] {

  def startListeningToDataStore(dataStore: DataStore, entityClass: Class[_ <: Aggregate]) {
    dataStore.addListener(entityClass, this)
  }

  final def onAggregateChanged(aggregate:Aggregate) {
    onEntityChanged(aggregate.asInstanceOf[E])
  }

  def onEntityChanged(entity: E)
}
