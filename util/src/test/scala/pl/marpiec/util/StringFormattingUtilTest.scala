package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test
import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class StringFormattingUtilTest {

  def testSimpleDateToString() {
    val date = new LocalDateTime(2010, 5, 10, 12, 40, 20, 111)

    assertEquals(StringFormattingUtil.printDateTime(date), "2010-05-10 12:40")
    assertEquals(StringFormattingUtil.printTime(date), "12:40")
    assertEquals(StringFormattingUtil.printDate(date), "2010-05-10")
  }


  def testSimpleDoubleToStringWithDot() {
    assertEquals(StringFormattingUtil.printSimpleDoubleForJavascript(3.2412), "3.24")
    assertEquals(StringFormattingUtil.printSimpleDoubleForJavascript(123.2412), "123.24")
    assertEquals(StringFormattingUtil.printSimpleDoubleForJavascript(123), "123.00")
  }
}
