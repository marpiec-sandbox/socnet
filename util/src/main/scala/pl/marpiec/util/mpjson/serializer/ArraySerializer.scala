package pl.marpiec.util.mpjson.serializer

import pl.marpiec.util.mpjson.{JsonTypeSerializer, SerializerFactory}


object ArraySerializer extends JsonTypeSerializer {


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
        SerializerFactory.getSerializer(element).serialize(element, jsonBuilder)
    })


    jsonBuilder.append(']')
  }


}
