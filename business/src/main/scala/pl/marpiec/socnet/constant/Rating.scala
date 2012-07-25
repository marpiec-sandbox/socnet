package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnumObject, SEnum}


/**
 * @author Marcin Pieciukiewicz
 */

object Rating extends SEnumObject[Rating] {

  val ONE = new Province("ONE", "1")
  val TWO = new Province("TWO", "2")
  val THREE = new Province("THREE", "3")
  val FOUR = new Province("FOUR", "4")
  val FIVE = new Province("FIVE", "5")

  val values = List(ONE, TWO, THREE, FOUR, FIVE)

  def getValues = values
}


case class Rating(name: String, translation: String) extends SEnum[Rating] {
  def getName() = name
}
