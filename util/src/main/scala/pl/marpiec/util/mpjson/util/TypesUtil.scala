package pl.marpiec.util.mpjson.util

import java.lang.reflect.{Field, ParameterizedType}
import pl.marpiec.util.json.annotation.{SecondSubType, FirstSubType}

/**
 * @author Marcin Pieciukiewicz
 */

object TypesUtil {

  def getSubElementsType(field:Field):Class[_] = {
    getSubElementsTypeForAnnotation(field, classOf[FirstSubType], 0)
  }

  def getDoubleSubElementsType(field:Field):(Class[_], Class[_]) = {
    (getSubElementsTypeForAnnotation(field, classOf[FirstSubType], 0),
      getSubElementsTypeForAnnotation(field, classOf[SecondSubType], 1))
  }


  private def getSubElementsTypeForAnnotation(field:Field, subTypeAnnotation:Class[_ <: java.lang.annotation.Annotation], typeIndex:Int):Class[_] = {
    var elementsType = field.getGenericType.asInstanceOf[ParameterizedType].getActualTypeArguments()(typeIndex).asInstanceOf[Class[_]]

    if(elementsType.equals(classOf[Object])) {
      val subtype = field.getAnnotation(subTypeAnnotation)
      if (subtype == null) {
        throw new IllegalStateException("No @"+subTypeAnnotation.getSimpleName+" defined for type of field "+field.getName)
      } else {
        //thats because annotations cannot have common interface :(
        elementsType = subTypeAnnotation.getMethod("value").invoke(subtype).asInstanceOf[Class[_]]
      }
    }

    elementsType
  }
  
  def getArraySubElementsType(arrayClass:Class[_]):Class[_] = {
    arrayClass.getComponentType
  }


}
