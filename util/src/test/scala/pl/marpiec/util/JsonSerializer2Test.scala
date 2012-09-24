package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

import org.testng.annotations.Test
import org.testng.Assert._
import org.joda.time.{Instant, LocalDate, LocalDateTime}
import senum.{SEnum, SEnumObject}

object SimpleEnum extends SEnumObject[SimpleEnum] {

  val ONE = new SimpleEnum("ONE", "1", 1)
  val TWO = new SimpleEnum("TWO", "2", 2)
  val THREE = new SimpleEnum("THREE", "3", 3)

  val values = List(ONE, TWO, THREE)

  def getValues = values
}

case class SimpleEnum(name: String, translation: String, numericValue:Int) extends SEnum[SimpleEnum] {
  def getName() = name
}

class JodaTimeObject {
  var localDate:LocalDate = _
  var localDateTime:LocalDateTime = _
  var instant:Instant = _
  var simpleEnum:SimpleEnum = _
}

@Test
class JsonSerializer2Test {

  def testJodaTimeSerialization() {

    val jto = new JodaTimeObject
    jto.localDate = new LocalDate(1999, 3, 12)
    jto.localDateTime = new LocalDateTime(2012, 10, 20, 15, 40, 30, 753)
    jto.instant = new Instant(123456789)
    jto.simpleEnum = SimpleEnum.TWO
    
    val json = new JsonSerializer
    val serialized = json.toJson(jto)

    assertEquals(serialized, "{localDate:\"1999-03-12\"," +
                              "localDateTime:\"2012-10-20 15:40:30:753\"," +
                              "instant:123456789," +
                              "simpleEnum:\"TWO\"}")

    val jtoDeserialized:JodaTimeObject = json.fromJson(serialized, classOf[JodaTimeObject])

    assertNotNull(jtoDeserialized)

    assertEquals(jtoDeserialized.localDate, jto.localDate)
    assertEquals(jtoDeserialized.localDateTime, jto.localDateTime)
    assertEquals(jtoDeserialized.instant, jto.instant)
    assertEquals(jtoDeserialized.simpleEnum, jto.simpleEnum)
  }


}
