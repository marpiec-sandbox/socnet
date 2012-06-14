package pl.marpiec.util

import org.testng.Assert._
import org.testng.annotations.Test
/**
 * @author Marcin Pieciukiewicz
 */

@Test
class BBCodeConverterTest {
  def testSimpleTagsConversion() {
    val input = "aaa[b]bbbb[/b]cccc[i]xssss[/i]sdsss[u]sdssf[/u]aaaaaa<script>alert</script>"
    val output = "aaa<b>bbbb</b>cccc<i>xssss</i>sdsss<u>sdssf</u>aaaaaa&lt;script&gt;alert&lt;/script&gt;"
    assertEquals(BBCodeConverter.convert(input), output)
  }
}
