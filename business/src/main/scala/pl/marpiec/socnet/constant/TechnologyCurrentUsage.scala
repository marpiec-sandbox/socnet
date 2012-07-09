package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}

/**
 * @author Marcin Pieciukiewicz
 */
object TechnologyCurrentUsage extends SEnumObject[TechnologyKnowledgeLevel] {

  val MIN_VALUE = 1
  val MAX_VALUE = 4

  val NOT_USING = new TechnologyCurrentUsage("NOT_USING", 1, "Nie uzywam")
  val LEARNING = new TechnologyCurrentUsage("LEARNING", 2, "Ucze sie")
  val HOBBY_WORKING = new TechnologyCurrentUsage("HOBBY_WORKING", 3, "Uzywam hobbistycznie")
  val WORKING_PROFESSIONALLY = new TechnologyCurrentUsage("WORKING_PROFESSIONALLY", 4, "Uzywam komercyjnie")

  val values = List(NOT_USING, LEARNING, HOBBY_WORKING, WORKING_PROFESSIONALLY)

  def getValues = values

  def getByValue(value:Int):TechnologyCurrentUsage = values.find(level => level.value == value).getOrElse(null)
}

case class TechnologyCurrentUsage(name: String, value: Int, translation:String) extends SEnum[TechnologyCurrentUsage] {
  def getName() = name
}