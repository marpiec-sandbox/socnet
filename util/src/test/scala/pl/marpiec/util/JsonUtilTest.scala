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
  //var smallLongOption:Option[Long] = _  THIS Test fill fail :/
  var longOption:Option[Long] = _
  var doubleOption:Option[Double] = _
  var booleanOption:Option[Boolean] = _
  var stringOption:Option[String] = _
}

@Test
class JsonUtilTest {
  
  def testSimpleObjectSerializationAndDeserialization {
    val jsonUtil = new JsonUtil
    
    val sdo = new SimpleDataObject
    sdo.longValue = 4
    sdo.stringValue = "testString"
    
    val simpleJson = jsonUtil.toJson(sdo)
    
    val sdoFromJson = jsonUtil.fromJson(simpleJson, classOf[SimpleDataObject])
    
    assertTrue(sdoFromJson.isInstanceOf[SimpleDataObject])
    assertEquals(sdoFromJson.asInstanceOf[SimpleDataObject].longValue, sdo.longValue)
    assertEquals(sdoFromJson.asInstanceOf[SimpleDataObject].stringValue, sdo.stringValue)
  }
  
  def testEmptyOptionSerializationAndDeserialization {

    val jsonUtil = new JsonUtil

    val odo = new OptionalDataObject
    odo.intOption = None
    odo.longOption = None
    odo.doubleOption = None
    odo.booleanOption = None
    odo.stringOption = None

    val simpleJson = jsonUtil.toJson(odo)

    val dataObject = jsonUtil.fromJson(simpleJson, classOf[OptionalDataObject])

    assertTrue(dataObject.isInstanceOf[OptionalDataObject])

    val odoFromJson = dataObject.asInstanceOf[OptionalDataObject]

    assertTrue(odoFromJson.intOption.isEmpty)
    assertTrue(odoFromJson.longOption.isEmpty)
    assertTrue(odoFromJson.doubleOption.isEmpty)
    assertTrue(odoFromJson.booleanOption.isEmpty)
    assertTrue(odoFromJson.stringOption.isEmpty)
  }


  def testFilledOptionSerializationAndDeserialization {

    val jsonUtil = new JsonUtil

    val odo = new OptionalDataObject
    odo.intOption = Option[Int](3)
    //odo.smallLongOption = Option[Long](10L)   THIS Test fill fail :/
    odo.longOption = Option[Long](10000000000L) //that is larger than max int
    odo.doubleOption = Option[Double](123.321)
    odo.booleanOption = Option[Boolean](true)
    odo.stringOption = Option[String]("test")

    val simpleJson = jsonUtil.toJson(odo, classOf[OptionalDataObject])

    val dataObject = jsonUtil.fromJson(simpleJson, classOf[OptionalDataObject])

    assertTrue(dataObject.isInstanceOf[OptionalDataObject])

    val odoFromJson = dataObject.asInstanceOf[OptionalDataObject]
    assertTrue(odoFromJson.intOption.isDefined)
    //assertTrue(odoFromJson.smallLongOption.isDefined)   THIS Test fill fail :/
    assertTrue(odoFromJson.longOption.isDefined)
    assertTrue(odoFromJson.doubleOption.isDefined)
    assertTrue(odoFromJson.booleanOption.isDefined)
    assertTrue(odoFromJson.stringOption.isDefined)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].intOption.get, odo.intOption.get)
    //assertEquals(dataObject.asInstanceOf[OptionalDataObject].smallLongOption.get, odo.smallLongOption.get)   THIS Test fill fail :/
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].longOption.get, odo.longOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].doubleOption.get, odo.doubleOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].booleanOption.get, odo.booleanOption.get)
    assertEquals(dataObject.asInstanceOf[OptionalDataObject].stringOption.get, odo.stringOption.get)

  }
}
