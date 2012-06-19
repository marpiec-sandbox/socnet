package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class DateUtilTest {

  def testSimpleDateToString() {
    val date = new LocalDateTime(2010, 5, 10, 12, 40, 20, 111)

    assertEquals(DateUtil.printDateTime(date), "2010-05-10 12:40")
    assertEquals(DateUtil.printTime(date), "12:40")
    assertEquals(DateUtil.printDate(date), "2010-05-10")
  }
}
