package pl.marpiec.util

/**
 * ...
 * @author Marcin Pieciukiewicz
 */

object Conversion {
  def emptyIfZero(value: Int): String = {
    if (value == 0) {
      ""
    } else {
      value.toString
    }
  }
}
