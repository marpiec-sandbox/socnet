package pl.marpiec.util

import org.testng.annotations.Test
import org.testng.Assert._

/**
 * @author Marcin Pieciukiewicz
 */

class SimpleDataObject {
  var longValue:Long = _
  var stringValue:String = _
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

@Test
class JsonSerializerTest {
  
  def testSimpleObjectSerializationAndDeserialization {
    val jsonSerializer = new JsonSerializer
    
    val sdo = new SimpleDataObject
    sdo.longValue = 4
    sdo.stringValue = "testString"
    
    val simpleJson = jsonSerializer.toJson(sdo)
    
    val sdoFromJson = jsonSerializer.fromJson(simpleJson, classOf[SimpleDataObject])
    
    assertTrue(sdoFromJson.isInstanceOf[SimpleDataObject])
    assertEquals(sdoFromJson.asInstanceOf[SimpleDataObject].longValue, sdo.longValue)
    assertEquals(sdoFromJson.asInstanceOf[SimpleDataObject].stringValue, sdo.stringValue)
  }
  
  def testEmptyOptionSerializationAndDeserialization {

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


  def testFilledOptionSerializationAndDeserialization {

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
}
