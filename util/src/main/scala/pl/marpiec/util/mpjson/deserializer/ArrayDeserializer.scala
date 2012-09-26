package pl.marpiec.util.mpjson.deserializer

import inner.AbstractJsonArrayDeserializer
import java.lang.reflect.Field
import pl.marpiec.util.mpjson.util.{TypesUtil, ObjectConstructionUtil}


/**
 * @author Marcin Pieciukiewicz
 */

object ArrayDeserializer extends AbstractJsonArrayDeserializer[Array[_]] {

  protected def getSubElementsType(clazz: Class[_], field: Field) = TypesUtil.getArraySubElementsType(clazz)

  protected def createEmptyCollectionInstance(elementsType: Class[_]) = ObjectConstructionUtil.createArrayInstance(elementsType, 0)

  protected def convertListIntoCollectionAndReturn(elementsType: Class[_], listInstance: List[Any]) = {

    val array = ObjectConstructionUtil.createArrayInstance(elementsType, listInstance.size)
    var list = listInstance.reverse
    var p = 0

    while (list.nonEmpty) {
      java.lang.reflect.Array.set(array, p, list.head)
      list = list.tail
      p = p + 1
    }

    array
  }
}
