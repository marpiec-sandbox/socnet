package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}


/**
 * @author Marcin Pieciukiewicz
 */

object TechnologyKnowledgeLevel extends SEnumObject[TechnologyKnowledgeLevel] {
  
  val MIN_VALUE = 0
  val MAX_VALUE = 5
  
  val NONE = new TechnologyKnowledgeLevel("NONE", 0, "Brak znajomosci")
  val HEARED_ONLY = new TechnologyKnowledgeLevel("HEARED_ONLY", 1, "Cos slyszalem")
  val BASIC = new TechnologyKnowledgeLevel("BASIC", 2, "Troche sie uczylem")
  val WORKED_WITH = new TechnologyKnowledgeLevel("WORKED_WITH", 3, "Przez chwil? pracowa?em")
  val WORKED_FOR_LONG = new TechnologyKnowledgeLevel("WORKED_FOR_LONG", 4, "Dlugo pracowa?em")
  val EXPERT = new TechnologyKnowledgeLevel("EXPERT", 5, "Jestem ekspereem")

  val values = List(NONE, HEARED_ONLY, BASIC, WORKED_WITH, WORKED_FOR_LONG, EXPERT)

  def getValues = values

  def getByValue(value:Int):TechnologyKnowledgeLevel = values.find(level => level.value == value).getOrElse(null)
}

case class TechnologyKnowledgeLevel(name: String, value: Int, translation:String) extends SEnum[TechnologyKnowledgeLevel] {
  def getName() = name
}