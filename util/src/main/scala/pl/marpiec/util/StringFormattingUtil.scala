package pl.marpiec.util

import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

object StringFormattingUtil {

  def printDateTime(date: LocalDateTime) = date.toString("yyyy-MM-dd HH:mm")

  def printTime(date: LocalDateTime) = date.toString("HH:mm")

  def printDate(date: LocalDateTime) = date.toString("yyyy-MM-dd")

  def printSimpleDoubleForJavascript(value: Double) = "%1.2f".format(value).replace(",", ".")


}
