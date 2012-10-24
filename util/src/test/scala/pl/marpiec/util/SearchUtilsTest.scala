package pl.marpiec.util


import org.testng.Assert._
import org.testng.annotations.Test

/**
 * @author Marcin Pieciukiewicz
 */

@Test
class SearchUtilsTest {

  def testSimpleIndexCreation() {

    val queryWords = SearchUtils.queryToWordsList("Javę można lubić i szanować.")

    assertEquals(queryWords.size, 4)
    assertTrue(queryWords.contains("jave"))
    assertTrue(queryWords.contains("mozna"))
    assertTrue(queryWords.contains("lubic"))
    assertTrue(queryWords.contains("szanowac"))


  }

}
