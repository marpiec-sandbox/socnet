package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.SerializerFactory

object ObjectSerializer extends SimpleSerializer {


  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('{')

    val clazz = obj.asInstanceOf[AnyRef].getClass()
    val fields = clazz.getDeclaredFields

    var isNotFirstField = false

    fields.foreach(field => {
      val getter = clazz.getMethod(field.getName)

      val value = getter.invoke(obj)

      if(value != null) {

        if(isNotFirstField) {
          jsonBuilder.append(",")
        } else {
          isNotFirstField = true
        }

        jsonBuilder.append(field.getName).append(':')
        SerializerFactory.getDeserializer(value).serialize(value, jsonBuilder)
      }
    })


    jsonBuilder.append('}')
  }


}
