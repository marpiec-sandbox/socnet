package socnet.constant

import pl.marpiec.util.senum.{SEnum, SEnumObject}


/**
 * @author Marcin Pieciukiewicz
 */

object Month extends SEnumObject[Month] {

  val JANUARY = new Month("JANUARY", "styczen", 1)
  val FEBRUARY = new Month("FEBRUARY", "luty", 2)
  val MARCH = new Month("MARCH", "marzec", 3)
  val APRIL = new Month("APRIL", "kwiecien", 4)
  val MAY = new Month("MAY", "maj", 5)
  val JUNE = new Month("JUNE", "czerwiec", 6)
  val JULY = new Month("JULY", "lipiec", 7)
  val AUGUST = new Month("AUGUST", "sierpien", 8)
  val SEPTEMBER = new Month("SEPTEMBER", "wrzesien", 9)
  val OCTOBER = new Month("OCTOBER", "pazdziernik", 10)
  val NOVEMBER = new Month("NOVEMBER", "listopad", 11)
  val DECEMBER = new Month("DECEMBER", "grudzien", 12)

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