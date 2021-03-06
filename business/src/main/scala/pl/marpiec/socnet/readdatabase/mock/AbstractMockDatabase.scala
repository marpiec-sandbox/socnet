package pl.marpiec.socnet.readdatabase.mock

import pl.marpiec.cqrs.{Aggregate, DataStoreListener, DataStore}
import collection.mutable.HashMap
import pl.marpiec.util.UID
import pl.marpiec.socnet.readdatabase.exception.EntryAlreadyExistsException


/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseIndex(val indexFunction: (Aggregate) => Any) {
  val index = new HashMap[Any, Aggregate]
}

class AbstractDatabase[E <: Aggregate](dataStore: DataStore) extends DataStoreListener[E] {

  private val entityDatabase = new HashMap[UID, E]
  private val indexes = new HashMap[String, DatabaseIndex]

  def addIndex(name: String, keyFunction: (Aggregate) => Any) {
    indexes += name -> new DatabaseIndex(keyFunction)
  }

  def onEntityChanged(aggregate: E) {
    addOrUpdate(aggregate)
  }

  def add(entity: E) {
    this.synchronized {
      if (entityDatabase.get(entity.id).isDefined) {
        throw new EntryAlreadyExistsException
      } else {
        val copy = entity.copy.asInstanceOf[E]
        entityDatabase += entity.id -> copy

        indexes.values.foreach[Unit]((index: DatabaseIndex) => {
          index.index += index.indexFunction(entity) -> copy
        })
      }
    }
  }

  def addOrUpdate(entity: E) {
    this.synchronized {
      val previousEntityOption = entityDatabase.get(entity.id)

      val copy = entity.copy.asInstanceOf[E]
      entityDatabase += entity.id -> copy

      indexes.values.foreach[Unit]((index: DatabaseIndex) => {

        if (previousEntityOption.isDefined) {
          val previousEntity = previousEntityOption.get
          val key = index.indexFunction(previousEntity)
          index.index.remove(key)
        }

        index.index += index.indexFunction(entity) -> copy
      })

    }
  }

  def getById(id: UID): Option[E] = {
    entityDatabase.get(id) match {
      case Some(entity) => Option.apply(entity)
      case None => None
    }
  }

  def getByIndex(name: String, key: Any): Option[E] = {
    indexes.getOrElse(name, throw new IllegalStateException("No index " + name + " defined")).
      index.get(key).asInstanceOf[Option[E]]
  }

  def getAll: List[E] = {
    entityDatabase.values.toList
  }

}
