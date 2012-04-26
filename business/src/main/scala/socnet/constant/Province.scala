package socnet.constant

import pl.marpiec.util.senum.{SEnumObject, SEnum}

/**
 * @author Marcin Pieciukiewicz
 */

object Province extends SEnumObject[Province] {

  val DOLNOSLASKIE = new Province("DOLNOSLASKIE", "dolnoslaskie")
  val KUJAWSKO_POMORSKIE = new Province("KUJAWSKO_POMORSKIE", "kujawsko-pomorskie")
  val LUBELSKIE = new Province("LUBELSKIE", "lubelskie")
  val LUBUSKIE = new Province("LUBUSKIE", "lubuskie")
  val LODZKIE = new Province("LODZKIE", "lodzkie")
  val MALOPOLSKIE = new Province("MALOPOLSKIE", "malopolskie")
  val MAZOWIECKIE = new Province("MAZOWIECKIE", "mazowieckie")
  val OPOLSKIE = new Province("OPOLSKIE", "opolskie")
  val PODKARPACIE = new Province("PODKARPACIE", "podkarpackie")
  val PODLASKIE = new Province("PODLASKIE", "podlaskie")
  val POMORSKIE = new Province("POMORSKIE", "pomorskie")
  val SLASKIE = new Province("SLASKIE", "slaskie")
  val SWIETOKRZYSKIE = new Province("SWIETOKRZYSKIE", "swietokrzyskie")
  val WARMINSKO_MAZURSKIE = new Province("WARMINSKO_MAZURSKIE", "warminsko-mazurskie")
  val WIELKOPOLSKIE = new Province("WIELKOPOLSKIE", "wielkopolskie")
  val ZACHODNIOPOMORSKIE = new Province("ZACHODNIOPOMORSKIE", "zachodniopomorskie")

  val values = List(DOLNOSLASKIE, KUJAWSKO_POMORSKIE, LUBELSKIE, LODZKIE,
    MALOPOLSKIE, MAZOWIECKIE, OPOLSKIE, PODKARPACIE, PODLASKIE, POMORSKIE,
    SLASKIE, SWIETOKRZYSKIE, WARMINSKO_MAZURSKIE, WIELKOPOLSKIE, ZACHODNIOPOMORSKIE)

  def getByName(name: String):Province = {
    for (value <- values) {
      if(value.name == name) {
        return value
      }
    }
    throw new IllegalArgumentException("Incorrect name!")
  }
}

case class Province(name:String, translation:String) extends SEnum[Province] {
  def getName() = name
}
