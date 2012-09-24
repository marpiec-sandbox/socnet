package pl.marpiec.util.mpjson.util

/**
 * @author Marcin Pieciukiewicz
 */

object ScalaLanguageUtils {

  def getSetterName(fieldName:String) = fieldName + "_$eq"

  def getGetterName(fieldName:String) = fieldName

}
