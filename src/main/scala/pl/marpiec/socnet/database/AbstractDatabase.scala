package pl.marpiec.socnet.database

import exception.EntryAlreadyExistsException
import pl.marpiec.cqrs.{CqrsEntity, DataStoreListener, DataStore}
import collection.mutable.HashMap
import pl.marpiec.socnet.model.{Article, UserProfile}
import java.util.UUID


/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseIndex(val indexFunction: (CqrsEntity) => Any) {
  val index = new HashMap[Any, CqrsEntity]
}

class AbstractDatabase[E <: CqrsEntity](dataStore: DataStore) extends DataStoreListener {

  private val entityDatabase = new HashMap[UUID, E]
  private val indexes = new HashMap[String, DatabaseIndex]
  
  def addIndex(name:String, keyFunction:(CqrsEntity) => Any) {
    indexes += name -> new DatabaseIndex(keyFunction)
  }
  
  def onEntityChanged(cqrsEntity: CqrsEntity) {
    val entity = cqrsEntity.asInstanceOf[E]
    if (entity.version == 1) {
      add(entity)
    } else {
      update(entity)
    }
  }

  def add(entity: E) {
    this.synchronized {
      if (entityDatabase.get(entity.uuid).isDefined) {
        throw new EntryAlreadyExistsException
      } else {
        val copy = entity.copy.asInstanceOf[E]
        entityDatabase += entity.uuid -> copy
        
        indexes.values.foreach[Unit]((index:DatabaseIndex) => {
          index.index += index.indexFunction(entity) -> copy
        })
      }
    }
  }

  def update(entity: E) {
    this.synchronized {
      val entityOption = entityDatabase.get(entity.uuid)
      if (entityOption.isEmpty) {
        throw new IllegalStateException("No entity defined in database, entityId=" + entity.uuid)
      } else {

        val previousEntity = entityOption.get

        val copy = entity.copy.asInstanceOf[E]
        entityDatabase += entity.uuid -> copy

        indexes.values.foreach[Unit]((index:DatabaseIndex) => {

          val key = index.indexFunction(previousEntity)
          index.index.remove(key)

          index.index += index.indexFunction(entity) -> copy
        })
      }
    }
  }

  def getById(uuid: UUID):Option[E] = {
    entityDatabase.get(uuid) match {
      case Some(entity) => Option.apply(entity)
      case None => None
    }
  }
  
  def getByIndex(name:String, key:Any):Option[E] = {
    indexes.getOrElse(name, throw new IllegalStateException("No index "+name+" defined")).
      index.get(key).asInstanceOf[Option[E]]
  }

  def getAll: List[E] = {
    entityDatabase.values.toList
  }

}