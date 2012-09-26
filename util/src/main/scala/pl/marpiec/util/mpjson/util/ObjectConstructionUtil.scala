package pl.marpiec.util.mpjson.util

import java.lang.reflect.Constructor
import scala.Array

/**
 * @author Marcin Pieciukiewicz
 */

object ObjectConstructionUtil {

  def createArrayInstance(elementsType:Class[_], size:Int): Array[_] = {
    java.lang.reflect.Array.newInstance(elementsType, size).asInstanceOf[Array[_]]
  }
  

  def createInstance(clazz: Class[_]): AnyRef = {

    val someConstructor: Constructor[_] = getPreferablyDefaultConstructor(clazz)
    val argsCount = someConstructor.getParameterTypes.size

    if (argsCount == 0) {
      return someConstructor.newInstance().asInstanceOf[AnyRef]
    } else {
      val arguments = prepareDefaultArguments(argsCount, someConstructor)
      return someConstructor.newInstance(arguments: _*).asInstanceOf[AnyRef]
    }

  }

  private def getPreferablyDefaultConstructor(clazz: Class[_]): Constructor[_] = {
    try {
      clazz.getConstructor() // try to get default constructor
    } catch {
      case e:NoSuchMethodException => clazz.getConstructors()(0) //otherwise get first constructor
    }
  }

  private def prepareDefaultArguments(argsCount: Int, someConstructor:Constructor[_]):Array[AnyRef] = {
    val arguments: Array[AnyRef] = new Array[AnyRef](argsCount)
    for (p <- 0 until argsCount) {
      val parameter = someConstructor.getParameterTypes()(p)

      if (parameter.isPrimitive) {
        arguments(p) = LanguageUtils.getDefaultValueForPrimitive(parameter)
      } else {
        arguments(p) = null
      }
    }
    arguments
  }

}
