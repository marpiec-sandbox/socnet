package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

import org.testng.annotations.Test
import org.testng.Assert._
import org.joda.time.{Instant, LocalDate, LocalDateTime}

class JodaTimeObject {
  var localDate:LocalDate = _
  var localDateTime:LocalDateTime = _
  var instant:Instant = _
}

@Test
class JsonSerializer2Test {

  def testJodaTimeSerialization() {

    val jto = new JodaTimeObject
    jto.localDate = new LocalDate(1999, 3, 12)
    jto.localDateTime = new LocalDateTime(2012, 10, 20, 15, 40, 30, 753)
    jto.instant = new Instant(123456789)
    
    val json = new JsonSerializer
    val serialized = json.toJson(jto)

    assertEquals(serialized, "{localDate:\"1999-03-12\"," +
                              "localDateTime:\"2012-10-20 15:40:30:753\"," +
                              "instant:123456789}")

    val jtoDeserialized:JodaTimeObject = json.fromJson(serialized, classOf[JodaTimeObject])

    assertNotNull(jtoDeserialized)

    assertEquals(jtoDeserialized.localDate, jto.localDate)
    assertEquals(jtoDeserialized.localDateTime, jto.localDateTime)
    assertEquals(jtoDeserialized.instant, jto.instant)
  }


}
