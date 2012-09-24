package pl.marpiec.util

import json.annotation.FirstSubType
import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Marcin Pieciukiewicz
 */

class InnerObject {
  var intValue:Int = _
  var stringValue:String = _ 
}

class MPSimpleDataObject {
  var longValue:Long = _
  var intValue:Int = _
  var stringValue:String = _
  var booleanValue:Boolean = _
  var innerObject:InnerObject = _
  var arrayObject:Array[String] = _
  var arrayPrimitive:Array[Long] = _
  var listObject:List[String] = _
  @FirstSubType(classOf[Int])
  var listPrimitive:List[Int] = _
}

@Test
class MPJsonTest {

  val sdo = new MPSimpleDataObject
  sdo.longValue = 1234567891234L
  sdo.intValue = 1111
  sdo.stringValue = "Hello Json \n\" parser\\serializer \""   //Hello Json " parser\deserializer "
  sdo.booleanValue = true

  sdo.innerObject = new InnerObject
  sdo.innerObject.intValue = -2222
  sdo.innerObject.stringValue = "inner string"

  sdo.arrayObject = Array[String]("Ala","ma","kota")
  sdo.arrayPrimitive = Array[Long](6, 8, 10)

  sdo.listObject = List[String]("Hello","json","serializer")
  sdo.listPrimitive = List[Int](15, 30, 1)

  val properJson = "{" +
    "longValue:1234567891234," +
    "intValue:1111," +
    "stringValue:\"Hello Json \\n\\\" parser\\\\serializer \\\"\"," +
    "booleanValue:true," +
    "innerObject:{" +
    "intValue:-2222," +
    "stringValue:\"inner string\"" +
    "}," +
    "arrayObject:[\"Ala\",\"ma\",\"kota\"],"+
    "arrayPrimitive:[6,8,10],"+
    "listObject:[\"Hello\",\"json\",\"serializer\"],"+
    "listPrimitive:[15,30,1]"+
    "}"


  def testSimpleDeserialization {

    var deserialized:MPSimpleDataObject = MPJson.deserialize(properJson, classOf[MPSimpleDataObject]).asInstanceOf[MPSimpleDataObject]

    assertEquals(deserialized.longValue, sdo.longValue)
    assertEquals(deserialized.intValue, sdo.intValue)
    assertEquals(deserialized.stringValue, sdo.stringValue)
    assertEquals(deserialized.booleanValue, sdo.booleanValue)

    assertNotNull(deserialized.innerObject)
    assertEquals(deserialized.innerObject.intValue, sdo.innerObject.intValue)
    assertEquals(deserialized.innerObject.stringValue, sdo.innerObject.stringValue)

    assertNotNull(deserialized.arrayObject)
    assertEquals(deserialized.arrayObject(0), sdo.arrayObject(0))
    assertEquals(deserialized.arrayObject(1), sdo.arrayObject(1))
    assertEquals(deserialized.arrayObject(2), sdo.arrayObject(2))

    assertNotNull(deserialized.arrayPrimitive)
    assertEquals(deserialized.arrayPrimitive(0), sdo.arrayPrimitive(0))
    assertEquals(deserialized.arrayPrimitive(1), sdo.arrayPrimitive(1))
    assertEquals(deserialized.arrayPrimitive(2), sdo.arrayPrimitive(2))

    assertNotNull(deserialized.listObject)
    assertEquals(deserialized.listObject(0), sdo.listObject(0))
    assertEquals(deserialized.listObject(1), sdo.listObject(1))
    assertEquals(deserialized.listObject(2), sdo.listObject(2))

    assertNotNull(deserialized.listPrimitive)
    assertEquals(deserialized.listPrimitive(0), sdo.listPrimitive(0))
    assertEquals(deserialized.listPrimitive(1), sdo.listPrimitive(1))
    assertEquals(deserialized.listPrimitive(2), sdo.listPrimitive(2))
  }


  def testSimpleSerialization {

    var serialized = MPJson.serialize(sdo)
    assertEquals(serialized, properJson)

  }

}
