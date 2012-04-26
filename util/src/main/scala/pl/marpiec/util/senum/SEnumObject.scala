package pl.marpiec.util.senum

/**
 * @author Marcin Pieciukiewicz
 */

abstract class SEnumObject[T] {
  def getByName(name: String): T
}
