package pl.marpiec.util.mpjson.util

import java.lang.reflect.Field

/**
 * @author Marcin Pieciukiewicz
 */

object ReflectionUtil {

  def getAllFields(clazz:Class[_]):Array[Field] = {
    if(clazz.getSuperclass.equals(classOf[Object])) {
      clazz.getDeclaredFields  
    } else {
      Array.concat(clazz.getDeclaredFields, getAllFields(clazz.getSuperclass))
    }
  }
  
  def getField(clazz:Class[_], fieldName:String):Field = {
    try {
      return clazz.getDeclaredField(fieldName)
    } catch {
      case e:NoSuchFieldException => {
        if (clazz.getSuperclass.equals(classOf[Object])) {
          return null
        } else {
          getField(clazz.getSuperclass, fieldName)
        }
      }
    }
  }
  
}
