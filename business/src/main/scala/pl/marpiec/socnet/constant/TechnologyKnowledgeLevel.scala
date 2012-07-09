package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}


/**
 * @author Marcin Pieciukiewicz
 */

object TechnologyKnowledgeLevel extends SEnumObject[TechnologyKnowledgeLevel] {
  
  val MIN_VALUE = 1
  val MAX_VALUE = 5
  
  val HEARED_ONLY = new TechnologyKnowledgeLevel("HEARED_ONLY", 1, "Cos slyszalem")
  val BASIC = new TechnologyKnowledgeLevel("BASIC", 2, "Troche sie uczylem")
  val WORKED_WITH = new TechnologyKnowledgeLevel("WORKED_WITH", 3, "Przez chwile pracowalem")
  val WORKED_FOR_LONG = new TechnologyKnowledgeLevel("WORKED_FOR_LONG", 4, "Dlugo pracowalem")
  val EXPERT = new TechnologyKnowledgeLevel("EXPERT", 5, "Jestem ekspertem")

  val values = List(HEARED_ONLY, BASIC, WORKED_WITH, WORKED_FOR_LONG, EXPERT)

  def getValues = values

  def getByValue(value:Int):TechnologyKnowledgeLevel = values.find(level => level.value == value).getOrElse(null)
}

case class TechnologyKnowledgeLevel(name: String, value: Int, translation:String) extends SEnum[TechnologyKnowledgeLevel] {
  def getName() = name
}