package pl.marpiec.util.mpjson.deserializer

import inner.AbstractJsonArrayDeserializer
import java.lang.reflect.Field
import pl.marpiec.util.mpjson.util.TypesUtil


/**
 * @author Marcin Pieciukiewicz
 */

object OptionDeserializer extends AbstractJsonArrayDeserializer[Option[_]] {

  protected def getSubElementsType(clazz: Class[_], field: Field) = TypesUtil.getSubElementsType(field)

  protected def createEmptyCollectionInstance(elementsType: Class[_]) = None

  protected def convertListIntoCollectionAndReturn(elementsType: Class[_], listInstance: List[Any]) = {
    Option(listInstance.head)
  }

}
