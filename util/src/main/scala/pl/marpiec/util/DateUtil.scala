package pl.marpiec.util

import org.joda.time.LocalDateTime

/**
 * @author Marcin Pieciukiewicz
 */

object DateUtil {

  def printDateTime(date: LocalDateTime): String = {
    date.toString("yyyy-MM-dd HH:mm")
  }

  def printTime(date: LocalDateTime): String = {
    date.toString("HH:mm")
  }

  def printDate(date: LocalDateTime): String = {
    date.toString("yyyy-MM-dd")
  }

}
