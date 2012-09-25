package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{JsonTypeSerializer, SerializerFactory}
import pl.marpiec.util.mpjson.util.LanguageUtils


object BeanSerializer extends JsonTypeSerializer {


  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('{')

    val clazz = obj.asInstanceOf[AnyRef].getClass()
    val fields = clazz.getDeclaredFields

    var isNotFirstField = false

    fields.foreach(field => {

      field.setAccessible(true)

      val value = field.get(obj)

      if(value != null) {

        if(isNotFirstField) {
          jsonBuilder.append(",")
        } else {
          isNotFirstField = true
        }

        jsonBuilder.append(field.getName).append(':')
        SerializerFactory.getSerializer(value).serialize(value, jsonBuilder)
      }
    })


    jsonBuilder.append('}')
  }


}
