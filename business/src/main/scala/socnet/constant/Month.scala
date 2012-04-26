package socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}


/**
 * @author Marcin Pieciukiewicz
 */

object Month extends SEnumObject[Month] {

  val JANUARY = new Month("JANUARY", "Styczen", 1)
  val FEBRUARY = new Month("FEBRUARY", "Luty", 2)
  val MARCH = new Month("MARCH", "Marzec", 3)
  val APRIL = new Month("APRIL", "Kwiecien", 4)
  val MAY = new Month("MAY", "Maj", 5)
  val JUNE = new Month("JUNE", "Czerwiec", 6)
  val JULY = new Month("JULY", "Lipiec", 7)
  val AUGUST = new Month("AUGUST", "Sierpien", 8)
  val SEPTEMBER = new Month("SEPTEMBER", "Wrzesien", 9)
  val OCTOBER = new Month("OCTOBER", "Pazdziernik", 10)
  val NOVEMBER = new Month("NOVEMBER", "Listopad", 11)
  val DECEMBER = new Month("DECEMBER", "Grudzien", 12)

  val values = List(JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER)

  def getValues() = values

  def getByOrderNumber(order:Int):Month = {
    for (value <- values) {
      if(value.order == order) {
        return value
      }
    }
    throw new IllegalArgumentException("Incorrect order value!")
  }
}

case class Month(val name:String, translation:String, order:Int) extends SEnum[Month] {
  def getName() = name
}