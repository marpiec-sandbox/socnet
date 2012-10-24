package pl.marpiec.socnet.mongodb

import model.TextIndex
import pl.marpiec.util.{UID, JsonSerializer}
import com.mongodb.util.JSON
import pl.marpiec.util.mpjson.MPJson
import com.mongodb.{QueryBuilder, BasicDBObject, Mongo}

/**
 * @author Marcin Pieciukiewicz
 */

class DatabaseTextIndexConnectorImpl(val collectionName: String) {

  val mongoConn = new Mongo("localhost", 27017)
  val socnetDB = mongoConn.getDB("socnet")
  val collection = socnetDB.getCollection(collectionName)

  val jsonSerializer = new JsonSerializer

  def addIndex(index: TextIndex) {
    val doc = JSON.parse(MPJson.serialize(index)).asInstanceOf[BasicDBObject]
    collection.update(new BasicDBObject().append("documentId", index.documentId.uid), doc, true, false)
  }

  def getIdentifierByQuery(query: List[String]): List[UID] = {
    val mongoQuery = QueryBuilder.start("words").all(query.toArray).get()

    val dbCursor = collection.find(mongoQuery, new BasicDBObject().append("documentId", 1L))

    var resultList = List[UID]()

    while (dbCursor.hasNext) {
      val dbObject = dbCursor.next()
      dbObject.removeField("_id")
      val documentId = dbObject.get("documentId")
      resultList ::= new UID(documentId.toString.toLong)
    }

    resultList
  }

  def removeIndexByDocumentId(documentId: UID) {
    collection.remove(new BasicDBObject().append("documentId", documentId.uid))
  }


}
