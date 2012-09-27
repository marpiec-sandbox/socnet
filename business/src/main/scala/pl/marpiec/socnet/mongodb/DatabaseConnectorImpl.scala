package pl.marpiec.socnet.mongodb

import com.mongodb.util.JSON
import pl.marpiec.util.mpjson.MPJson
import pl.marpiec.util.{UID, JsonSerializer}
import pl.marpiec.cqrs.Aggregate
import com.mongodb.{Mongo, BasicDBObject}
import pl.marpiec.socnet.model.Article

/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseConnectorImpl {
  val mongoConn = new Mongo("localhost", 27017)
  val socnetDB = mongoConn.getDB("socnet")
  val collection = socnetDB.getCollection("articles")

  val jsonSerializer = new JsonSerializer

  def insertObject(obj: AnyRef) {
    val doc = JSON.parse(MPJson.serialize(obj)).asInstanceOf[BasicDBObject]
    val writeResult = collection.insert(doc)
    writeResult
  }
     /*
  def insertObjectWithId(id: UID, obj: AnyRef) {
    val doc = JSON.parse(MPJson.serialize(obj)).asInstanceOf[BasicDBObject]
    doc.append("_id", id.uid)
    val writeResult = collection.insert(doc)
    writeResult
  }

  def getObjectById[E](id: UID, clazz: Class[_]): E = {
    val query = new BasicDBObject
    query.append("_id", id.uid)
    val basicObject = collection.findOne(query)
    basicObject.removeField("_id")
    EjsonSerializer.fromJson(basicObject.toString, clazz).asInstanceOf[E]
  }

  def getAllObjects(clazz: Class[_]): List[AnyRef] = {
    var resultList = List[AnyRef]()
    val dbCursor = collection.find()
    while (dbCursor.hasNext) {
      val basicObject = dbCursor.next()
      basicObject.removeField("_id")
      resultList ::= jsonSerializer.fromJson(basicObject.toString, clazz)
    }
    resultList
  }    */

  def insertAggregate(aggregate: Aggregate) {
    val doc = JSON.parse(MPJson.serialize(aggregate)).asInstanceOf[BasicDBObject]
    doc.remove("id")
    doc.append("_id", aggregate.id.uid)
    val writeResult = collection.insert(doc)
    writeResult
  }

  def getAggregateById[E <: Aggregate](id: UID, clazz: Class[_ <: Aggregate]): E = {
    var query = new BasicDBObject
    query.append("_id", id.uid)
    var basicObject = collection.findOne(query).asInstanceOf[BasicDBObject]
    basicObject.removeField("_id")
    var aggregate = jsonSerializer.fromJson(basicObject.toString, clazz).asInstanceOf[E]
    aggregate.id = id
    aggregate
  }

  def getAllAggregates(clazz: Class[_ <: Aggregate]): List[Aggregate] = {
    var resultList = List[Aggregate]()
    val dbCursor = collection.find()
    while (dbCursor.hasNext) {
      val basicObject = dbCursor.next()
      val id = new UID(basicObject.get("_id").asInstanceOf[Long])
      basicObject.removeField("_id")
      val aggregate = jsonSerializer.fromJson(basicObject.toString, clazz)
      aggregate.id = id
      resultList ::= aggregate
    }
    resultList
  }
}
