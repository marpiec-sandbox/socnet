package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}

/**
 * @author Marcin Pieciukiewicz
 */




object TechnologyLikeLevel extends SEnumObject[TechnologyLikeLevel] {

  val MIN_VALUE = 1
  val MAX_VALUE = 5

  val VERY_GOOD = new TechnologyLikeLevel("VERY_GOOD", 5, "Bardzo fajna")
  val GOOD = new TechnologyLikeLevel("GOOD", 4, "Niezla")
  val NEUTRAL = new TechnologyLikeLevel("NEUTRAL", 3, "Neutralna")
  val BAD = new TechnologyLikeLevel("BAD", 2, "Kiepska")
  val VERY_BAD = new TechnologyLikeLevel("VERY_BAD", 1, "Fatalna")

  val values = List(VERY_GOOD, GOOD, NEUTRAL, BAD, VERY_BAD)

  def getValues = values

  def getByValue(value:Int):TechnologyLikeLevel = values.find(level => level.value == value).getOrElse(null)
}

case class TechnologyLikeLevel(name: String, value: Int, translation:String) extends SEnum[TechnologyLikeLevel] {
  def getName() = name
}