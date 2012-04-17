package pl.marpiec.util


/**
 * @author Marcin Pieciukiewicz
 */

class UID(val uid: Long) extends Serializable with Comparable[UID] {

  def compareTo(o: UID) = uid.compareTo(o.uid)

  override def toString = uid.toString

  override def hashCode() = uid.hashCode()

  override def equals(obj: Any) = {
    if(obj.isInstanceOf[UID]) {
      val objUid: UID = obj.asInstanceOf[UID]
      objUid.uid.equals(uid)
    } else {
      false
    }
  }
}

object UID {
  def parseOrZero(str:String):UID = {
    try {
      new UID(str.toLong)
    } catch {
      case e:NumberFormatException => new UID(0)
      case e:Exception => throw new IllegalStateException("It shouldn't happen , ever.")
    }
  }
}

