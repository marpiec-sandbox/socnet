package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.SerializerFactory

object ArraySerializer extends SimpleSerializer {


  def serialize(obj: Any, jsonBuilder:StringBuilder) = {

    jsonBuilder.append('[')

    var isNotFirstField = false

    val array = obj.asInstanceOf[Array[_]]

    array.foreach(element => {

        if(isNotFirstField) {
          jsonBuilder.append(",")
        } else {
          isNotFirstField = true
        }
        SerializerFactory.getDeserializer(element).serialize(element, jsonBuilder)
    })


    jsonBuilder.append(']')
  }


}
