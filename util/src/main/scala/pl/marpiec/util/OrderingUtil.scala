package pl.marpiec.util

/**
 * @author Marcin Pieciukiewicz
 */

object OrderingUtil {

  val STRING_IGNORE_CASE_ORDERING = new Ordering[String] {
    def compare(x: String, y: String) = {
      val comparisonResult = StringFormattingUtil.toLowerCase(x).compareTo(StringFormattingUtil.toLowerCase(y))
      if (comparisonResult == 0) {
        x.compareTo(y)
      } else {
        comparisonResult
      }
    }
  }
  
}
