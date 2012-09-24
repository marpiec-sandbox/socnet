package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

import org.joda.time.{LocalDateTime}
import org.testng.annotations.Test
import org.testng.Assert._

class JodaTimeObject {
  var localDateTime:LocalDateTime = _
}

@Test
class JsonSerializer2Test {

  def testJodaTimeSerialization() {

    val jto = new JodaTimeObject
    jto.localDateTime = new LocalDateTime(2012, 10, 20, 15, 40, 30, 753)
    
    val json = new JsonSerializer
    val serialized = json.toJson(jto)

    assertEquals(serialized, "{localDateTime:\"2012-10-20 15:40:30:753\"}")

    val jtoDeserialized:JodaTimeObject = json.fromJson(serialized, classOf[JodaTimeObject])

    assertNotNull(jtoDeserialized)

    assertEquals(jtoDeserialized.localDateTime, jto.localDateTime)
  }


}
