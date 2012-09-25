package pl.marpiec.util.mpjson.serializer.common

import pl.marpiec.util.mpjson.{SerializerFactory, JsonTypeSerializer}

/**
 * @author Marcin Pieciukiewicz
 */

object IteratorSerializer extends JsonTypeSerializer {

  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('[')

    var isNotFirstField = false

    val iterator = obj.asInstanceOf[Iterator[_]]

    iterator.foreach(element => {

      if(isNotFirstField) {
        jsonBuilder.append(",")
      } else {
        isNotFirstField = true
      }
      SerializerFactory.getSerializer(element).serialize(element, jsonBuilder)
    })


    jsonBuilder.append(']')
  }


}
