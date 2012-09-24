package pl.marpiec.util

import json.annotation.{SecondSubType, FirstSubType, SubType}
import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author Marcin Pieciukiewicz
 */

class SimpleDataObject {
  var longValue:Long = _
  var stringValue:String = _
  var tuple:(String, String) = _
  @FirstSubType(classOf[Int]) @SecondSubType(classOf[Long])
  var tuplePrimitive:(Int, Long) = _
}

class OptionalDataObject {
  var intOption:Option[Int] = _
  var smallLongOption:Option[Long] = _
  var longOption:Option[Long] = _
  var doubleOption:Option[Double] = _
  var booleanOption:Option[Boolean] = _
  var stringOption:Option[String] = _
  var sdo:Option[SimpleDataObject] = _
}

class CollectionsDataObject {
  var stringsList: List[String] = _
  @SubType(classOf[Long])
  var longsList: List[Long] = _
  var emptyList: List[String] = _
  var emptyArray: Array[Long] = Array()
}

@Test
class JsonSerializerTest {
  
  def testSimpleObjectSerializationAndDeserialization() {
    val jsonSerializer = new JsonSerializer
    
    val sdo = new SimpleDataObject
    sdo.longValue = 4
    sdo.stringValue = "testString"
    sdo.tuple = ("test", "4")
    sdo.tuplePrimitive = (3, 15)
    
    val simpleJson = jsonSerializer.toJson(sdo)
    
    val sdoFromJson = jsonSerializer.fromJson(simpleJson, classOf[SimpleDataObject])
    
    assertTrue(sdoFromJson.isInstanceOf[SimpleDataObject])
    assertEquals(sdoFromJson.asInstanceOf[SimpleDataObject].longValue, sdo.longValue)
    assertEquals(sdoFromJson.asInstanceOf[SimpleDataObject].stringValue, sdo.stringValue)
    val (t1:String, t2:String) = sdoFromJson.asInstanceOf[SimpleDataObject].tuple
    assertEquals(t1, "test")
    assertEquals(t2, "4")
    val (p1:Int, p2:Long) = sdoFromJson.asInstanceOf[SimpleDataObject].tuplePrimitive
    assertEquals(p1, 3)
    assertEquals(p2, 15)
  }
  
  def testEmptyOptionSerializationAndDeserialization() {

    val jsonSerializer = new JsonSerializer

    val odo = new OptionalDataObject
    odo.intOption = None
    odo.longOption = None
    odo.doubleOption = None
    odo.booleanOption = None
    odo.stringOption = None

    val simpleJson = jsonSerializer.toJson(odo)

    val dataObject = jsonSerializer.fromJson(simpleJson, classOf[OptionalDataObject])

    assertTrue(dataObject.isInstanceOf[OptionalDataObject])

    val odoFromJson = dataObject.asInstanceOf[OptionalDataObject]

    assertTrue(odoFromJson.intOption.isEmpty)
    assertTrue(odoFromJson.longOption.isEmpty)
    assertTrue(odoFromJson.doubleOption.isEmpty)
    assertTrue(odoFromJson.booleanOption.isEmpty)
    assertTrue(odoFromJson.stringOption.isEmpty)
  }


  def testFilledOptionSerializationAndDeserialization() {

    val jsonSerializer = new JsonSerializer

    val odo = new OptionalDataObject
    odo.intOption = Option[Int](3)
    odo.smallLongOption = Option[Long](10L)
    odo.longOption = Option[Long](10000000000L) //that is larger than max int
    odo.doubleOption = Option[Double](123.321)
    odo.booleanOption = Option[Boolean](true)
    odo.stringOption = Option[String]("test")
    
    val sdo = new SimpleDataObject
    sdo.longValue = 4
    sdo.stringValue = "testString"
    odo.sdo = Option[SimpleDataObject](sdo)

    val simpleJson = jsonSerializer.toJson(odo)

    val dataObject = jsonSerializer.fromJson(simpleJson, classOf[OptionalDataObject])

    assertTrue(dataObject.isInstanceOf[OptionalDataObject])

    val odoFromJson = dataObject.asInstanceOf[OptionalDataObject]
    assertTrue(odoFromJson.intOption.isDefined)
    assertTrue(odoFromJson.smallLongOption.isDefined)
    assertTrue(odoFromJson.longOption.isDefined)
    assertTrue(odoFromJson.doubleOption.isDefined)
    assertTrue(odoFromJson.booleanOption.isDefined)
    assertTrue(odoFromJson.stringOption.isDefined)
    assertTrue(odoFromJson.sdo.isDefined)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].intOption.get, odo.intOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].smallLongOption.get, odo.smallLongOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].longOption.get, odo.longOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].doubleOption.get, odo.doubleOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].booleanOption.get, odo.booleanOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].stringOption.get, odo.stringOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].sdo.get.longValue, odo.sdo.get.longValue)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].sdo.get.stringValue, odo.sdo.get.stringValue)

  }
  
  
  def testCollectionsSerialization() {
    val jsonSerializer = new JsonSerializer

    val cdo = new CollectionsDataObject

    cdo.stringsList = List[String]("a", "b", "c")
    cdo.longsList = List[Long](5, 10, 20)
    cdo.emptyList = List[String]()
    
    val simpleJson = jsonSerializer.toJson(cdo)
    val dataObject = jsonSerializer.fromJson(simpleJson, classOf[CollectionsDataObject])

    val deserializedObject = dataObject.asInstanceOf[CollectionsDataObject]
    assertEquals(deserializedObject.stringsList.size, cdo.stringsList.size)
    assertEquals(deserializedObject.stringsList(0), cdo.stringsList(0))
    assertEquals(deserializedObject.stringsList(1), cdo.stringsList(1))
    assertEquals(deserializedObject.stringsList(2), cdo.stringsList(2))

    assertEquals(deserializedObject.longsList.size, cdo.longsList.size)
    assertEquals(deserializedObject.longsList(0), cdo.longsList(0))
    assertEquals(deserializedObject.longsList(1), cdo.longsList(1))
    assertEquals(deserializedObject.longsList(2), cdo.longsList(2))

    assertEquals(deserializedObject.emptyList.size, cdo.emptyList.size)
    assertEquals(deserializedObject.emptyArray.size, cdo.emptyArray.size)

  }

}
