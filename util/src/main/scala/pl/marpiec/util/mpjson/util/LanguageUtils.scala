package pl.marpiec.util.mpjson.util

/**
 * @author Marcin Pieciukiewicz
 */

object LanguageUtils {

  val DEFAULT_BYTE = java.lang.Byte.valueOf("0")
  val DEFAULT_SHORT = java.lang.Short.valueOf("0")
  val DEFAULT_INTEGER = java.lang.Integer.valueOf(0)
  val DEFAULT_LONG = java.lang.Long.valueOf(0L)
  val DEFAULT_FLOAT = java.lang.Float.valueOf(0.0f)
  val DEFAULT_DOUBLE = java.lang.Double.valueOf(0.0)
  val DEFAULT_BOOLEAN = java.lang.Boolean.FALSE
  val DEFAULT_CHARACTER = java.lang.Character.valueOf('\0')

  def getDefaultValueForPrimitive(clazz:Class[_]):AnyRef = {
    if (classOf[Int].equals(clazz)) {
      return DEFAULT_INTEGER
    } else if (classOf[Long].equals(clazz)) {
      return DEFAULT_LONG
    } else if (classOf[Boolean].equals(clazz)) {
      return DEFAULT_BOOLEAN
    } else if (classOf[Double].equals(clazz)) {
      return DEFAULT_DOUBLE
    } else if (classOf[Char].equals(clazz)) {
      return DEFAULT_CHARACTER
    } else  if (classOf[Float].equals(clazz)) {
      return DEFAULT_FLOAT
    } else if (classOf[Byte].equals(clazz)) {
      return DEFAULT_BYTE
    } else if (classOf[Short].equals(clazz)) {
      return DEFAULT_SHORT
    }
    throw new IllegalArgumentException("Passed class is not Java primitive, passed class: "+clazz)
  }
}
