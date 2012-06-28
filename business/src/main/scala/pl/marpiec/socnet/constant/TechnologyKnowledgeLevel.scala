package pl.marpiec.socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}


/**
 * @author Marcin Pieciukiewicz
 */

object TechnologyKnowledgeLevel extends SEnumObject[TechnologyKnowledgeLevel] {
  val NONE = new TechnologyKnowledgeLevel("NONE", 0)
  val HEARED_ONLY = new TechnologyKnowledgeLevel("HEARED_ONLY", 1)
  val BASIC = new TechnologyKnowledgeLevel("BASIC", 2)
  val WORKED_WITH = new TechnologyKnowledgeLevel("WORKED_WITH", 3)
  val WORKED_FOR_LONG = new TechnologyKnowledgeLevel("WORKED_FOR_LONG", 4)
  val EXPERT = new TechnologyKnowledgeLevel("EXPERT", 5)

  val values = List(NONE, HEARED_ONLY, BASIC, WORKED_WITH, WORKED_FOR_LONG, EXPERT)

  def getValues = values

  def getByValue(value:Int):TechnologyKnowledgeLevel = values.find(level => level.value == value)
}

case class TechnologyKnowledgeLevel(name: String, value: Int) extends SEnum[TechnologyKnowledgeLevel] {
  def getName() = name
}