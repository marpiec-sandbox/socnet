package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnumObject, SEnum}


/**
 * @author Marcin Pieciukiewicz
 */

object Rating extends SEnumObject[Rating] {

  val ONE = new Rating("ONE", "1", 1)
  val TWO = new Rating("TWO", "2", 2)
  val THREE = new Rating("THREE", "3", 3)
  val FOUR = new Rating("FOUR", "4", 4)
  val FIVE = new Rating("FIVE", "5", 5)

  val values = List(ONE, TWO, THREE, FOUR, FIVE)

  def getValues = values
}


case class Rating(name: String, translation: String, numericValue:Int) extends SEnum[Rating] {
  def getName() = name
}
