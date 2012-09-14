package pl.marpiec.util

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
}

@Test
class MPJsonTest {

  def testSimpleDeserialization {
    var sdo = new MPSimpleDataObject
    sdo.longValue = 1234567891234L
    sdo.intValue = 1111
    sdo.stringValue = "Hello Json \" parser\\serializer \""   //Hello Json " parser\serializer "
    sdo.booleanValue = true
    sdo.innerObject = new InnerObject
    sdo.innerObject.intValue = 2222
    sdo.innerObject.stringValue = "inner string"

    var serialized = "{" +
                      "longValue:1234567891234," +
                      "intValue:1111," +
                      "stringValue:\"Hello Json \\\" parser\\\\serializer \\\"\"," +
                      "booleanValue:true," +
                      "innerObject:{" +
                        "intValue:2222," +
                        "stringValue:\"inner string\"" +
                        "}"
                      "}"

    var deserialized:MPSimpleDataObject = MPJson.deserialize(serialized, classOf[MPSimpleDataObject]).asInstanceOf[MPSimpleDataObject]

    assertEquals(deserialized.longValue, sdo.longValue)
    assertEquals(deserialized.intValue, sdo.intValue)
    assertEquals(deserialized.stringValue, sdo.stringValue)
    assertEquals(deserialized.booleanValue, sdo.booleanValue)

    assertEquals(deserialized.innerObject.intValue, sdo.innerObject.intValue)
    assertEquals(deserialized.innerObject.stringValue, sdo.innerObject.stringValue)
  }


}
