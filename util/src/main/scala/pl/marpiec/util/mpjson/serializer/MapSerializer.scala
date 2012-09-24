package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{SerializerFactory, JsonTypeSerializer}

/**
 * @author Marcin Pieciukiewicz
 */

object MapSerializer extends JsonTypeSerializer {


  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('{')
    
    val map:Map[_,_] = obj.asInstanceOf[Map[_,_]]

    var isNotFirstField = false

    for ((key, value) <- map) {
      if(isNotFirstField) {
        jsonBuilder.append(",")
      } else {
        isNotFirstField = true
      }
      SerializerFactory.getSerializer(key).serialize(key, jsonBuilder)
      jsonBuilder.append(':')
      SerializerFactory.getSerializer(value).serialize(value, jsonBuilder)

    }

    jsonBuilder.append('}')
  }


}
