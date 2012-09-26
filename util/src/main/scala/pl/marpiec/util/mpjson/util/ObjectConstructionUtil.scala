package pl.marpiec.util.mpjson.util

import scala.Array
import java.lang.reflect.{Field, Constructor}

/**
 * @author Marcin Pieciukiewicz
 */

object ObjectConstructionUtil {

  val unsafeObject = getPreparedUnsafeObject

  def createArrayInstance(elementsType: Class[_], size: Int): Array[_] = {
    java.lang.reflect.Array.newInstance(elementsType, size).asInstanceOf[Array[_]]
  }

  def createInstance(clazz: Class[_]): AnyRef = {

    val someConstructor: Constructor[_] = getPreferablyDefaultConstructor(clazz)
    val argsCount = someConstructor.getParameterTypes.size

    if (argsCount == 0) {
      return someConstructor.newInstance().asInstanceOf[AnyRef]
    } else {
      createInstanceWithoutCallingConstructor(clazz)
    }

  }

  private def getPreferablyDefaultConstructor(clazz: Class[_]): Constructor[_] = {
    try {
      clazz.getConstructor() // try to get default constructor
    } catch {
      case e: NoSuchMethodException => clazz.getConstructors()(0) //otherwise get first constructor
    }
  }

  private def getPreparedUnsafeObject: sun.misc.Unsafe = {
    try {
      val unsafeClass: Class[_] = classOf[sun.misc.Unsafe]
      val fiels: Field = unsafeClass.getDeclaredField("theUnsafe")
      fiels.setAccessible(true)
      fiels.get(null).asInstanceOf[sun.misc.Unsafe]
    } catch {
      case e: Exception => throw new IllegalStateException("Cannot create object without calling constructor\n" +
        "Thats probably because JRE is not sun/oracle implementation", e);
    }
  }

  def createInstanceWithoutCallingConstructor(clazz: Class[_]): AnyRef = {

    try {
      unsafeObject.allocateInstance(clazz)
    } catch {
      case e: Exception => throw new IllegalStateException("Cannot create object without calling constructor\n" +
        "Thats probably because JRE is not sun/oracle implementation", e);
    }

    /**DO NOT REMOVE - this is another way to do this */
    /*try {
      val rf = ReflectionFactory.getReflectionFactory()
      val objectDefaultConstructor = classOf[Object].getDeclaredConstructor()
      val intConstr = rf.newConstructorForSerialization(clazz, objectDefaultConstructor)
      clazz.cast(intConstr.newInstance()).asInstanceOf[AnyRef]
    } catch {
      case e: Exception => throw new IllegalStateException("Cannot create object without calling constructor\n" +
        "Thats probably because JRE is not sun/oracle implementation", e);
    } */
  }


}
