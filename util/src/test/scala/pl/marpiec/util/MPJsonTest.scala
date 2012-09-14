package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Marcin Pieciukiewicz
 */

class MPSimpleDataObject {
  var longValue:Long = _
  var intValue:Int = _
  var stringValue:String = _
  var booleanValue:Boolean = _
}

@Test
class MPJsonTest {

  def testSimpleDeserialization {
    var sdo = new MPSimpleDataObject
    sdo.longValue = 1234567891234L
    sdo.intValue = 1111
    sdo.stringValue = "Hello Json \" parser\\serializer \""   //Hello Json " parser\serializer "
    sdo.booleanValue = true

    var serialized = "{longValue:1234567891234,intValue:1111,stringValue:\"Hello Json \\\" parser\\\\serializer \\\"\",booleanValue:true}"

    var deserialized:MPSimpleDataObject = MPJson.deserialize(serialized, classOf[MPSimpleDataObject]).asInstanceOf[MPSimpleDataObject]

    assertEquals(deserialized.longValue, sdo.longValue)
    assertEquals(deserialized.intValue, sdo.intValue)
    assertEquals(deserialized.stringValue, sdo.stringValue)
    assertEquals(deserialized.booleanValue, sdo.booleanValue)
  }


}
