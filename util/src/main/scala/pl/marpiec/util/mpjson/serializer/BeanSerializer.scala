package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{SerializerFactory, JsonTypeSerializer}


/**
 * @author Marcin Pieciukiewicz
 */

object BeanSerializer extends JsonTypeSerializer {

  def serialize(obj: Any, jsonBuilder: StringBuilder) = {

    val option = obj.asInstanceOf[Option[_]]

    jsonBuilder.append('[')
    if(option.isDefined) {
      val value = option.get
      SerializerFactory.getSerializer(value).serialize(value, jsonBuilder)
    }
    jsonBuilder.append(']')

  }
}
