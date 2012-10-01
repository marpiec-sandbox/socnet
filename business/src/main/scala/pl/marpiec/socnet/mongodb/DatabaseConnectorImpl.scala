package pl.marpiec.socnet.mongodb

import com.mongodb.util.JSON
import pl.marpiec.util.mpjson.MPJson
import pl.marpiec.util.{UID, JsonSerializer}
import pl.marpiec.cqrs.Aggregate
import com.mongodb.{QueryBuilder, DBObject, Mongo, BasicDBObject}

/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseConnectorImpl(val collectionName: String) {
  val mongoConn = new Mongo("localhost", 27017)
  val socnetDB = mongoConn.getDB("socnet")
  val collection = socnetDB.getCollection(collectionName)

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
    val writeResult = collection.update(new BasicDBObject().append("_id", aggregate.id.uid), doc, true, false)
    writeResult
  }

  def getAggregateById[E <: Aggregate](id: UID, clazz: Class[_ <: Aggregate]): Option[E] = {
    val query = QueryBuilder.start("_id").is(id.uid).get()
    val basicObject = collection.findOne(query).asInstanceOf[BasicDBObject]
    if (basicObject == null) {
      None
    } else {
      basicObject.removeField("_id")
      val aggregate = jsonSerializer.fromJson(basicObject.toString, clazz).asInstanceOf[E]
      aggregate.id = id
      Option(aggregate)
    }

  }


  def getMultipleAggregatesByIds[E <: Aggregate](ids: List[UID], clazz: Class[_ <: Aggregate]): List[E] = {
    val queryBuilder = QueryBuilder.start("_id")
    ids.foreach(id => queryBuilder.in(id))
    val query = queryBuilder.get()

    val dbCursor = collection.find(query)

    var resultList = List[E]()

    while (dbCursor.hasNext) {
      val dbObject = dbCursor.next()
      val id = dbObject.get("_id").asInstanceOf[Long]
      dbObject.removeField("_id")
      val aggregate = jsonSerializer.fromJson(dbObject.toString, clazz).asInstanceOf[E]
      aggregate.id = new UID(id)
      resultList ::= aggregate
    }
    resultList
  }

  def getAllAggregates[E <: Aggregate](clazz: Class[_ <: Aggregate]): List[E] = {
    var resultList = List[E]()
    val dbCursor = collection.find()
    while (dbCursor.hasNext) {
      val basicObject = dbCursor.next()
      val id = new UID(basicObject.get("_id").asInstanceOf[Long])
      basicObject.removeField("_id")
      val aggregate = jsonSerializer.fromJson(basicObject.toString, clazz).asInstanceOf[E]
      aggregate.id = id
      resultList ::= aggregate
    }
    resultList
  }

  def findAggregateByQuery[E <: Aggregate](query: DBObject, clazz: Class[_ <: Aggregate]): Option[E] = {
    var basicObject = collection.findOne(query).asInstanceOf[BasicDBObject]
    if (basicObject == null) {
      None
    } else {
      val id = new UID(basicObject.get("_id").asInstanceOf[Long])
      basicObject.removeField("_id")
      val aggregate = jsonSerializer.fromJson(basicObject.toString, clazz).asInstanceOf[E]
      aggregate.id = id
      Option(aggregate)
    }
  }

  def findMultipleAggregatesByQuery[E <: Aggregate](query: DBObject, clazz: Class[_ <: Aggregate]): List[E] = {
    var dbCursor = collection.find(query)
    var resultList = List[E]()
    while (dbCursor.hasNext) {
      val basicObject = dbCursor.next()
      val id = new UID(basicObject.get("_id").asInstanceOf[Long])
      basicObject.removeField("_id")
      val aggregate = jsonSerializer.fromJson(basicObject.toString, clazz).asInstanceOf[E]
      aggregate.id = id
      resultList ::= aggregate
    }
    resultList
  }
}
