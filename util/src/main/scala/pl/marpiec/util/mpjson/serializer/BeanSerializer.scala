package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{JsonTypeSerializer, SerializerFactory}
import java.lang.reflect.AccessibleObject
import pl.marpiec.util.mpjson.util.ReflectionUtil

object BeanSerializer extends JsonTypeSerializer {


  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('{')

    val clazz = obj.asInstanceOf[AnyRef].getClass()
    val fields = ReflectionUtil.getAllFields(clazz)
    AccessibleObject.setAccessible(fields.asInstanceOf[Array[AccessibleObject]], true)

    var isNotFirstField = false

    fields.foreach(field => {

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
